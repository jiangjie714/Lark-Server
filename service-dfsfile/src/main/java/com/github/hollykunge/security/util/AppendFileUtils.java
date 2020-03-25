package com.github.hollykunge.security.util;

import com.ace.cache.api.impl.CacheRedis;
import com.alibaba.fastjson.JSONObject;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.comtants.FileComtants;
import com.github.hollykunge.security.vo.FileAppendInfoVO;
import com.github.tobato.fastdfs.domain.fdfs.FileInfo;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.AppendFileStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhhongyu
 * @description: 文件分块上传
 * @since: Create in 13:41 2019/8/13
 */
@Slf4j
@Component
public class AppendFileUtils {

    @Autowired
    private AppendFileStorageClient appendFileStorageClient;
    @Autowired
    private FastDFSClientWrapper fastDFSClientWrapper;

    /**
     * 所在组卷
     */
    @Value("${fdfs.groupName}")
    private String groupName = "group1";

    @Value("${upload.sensitiveFile.original}")
    private String sensitiveOriginalFile;
    @Autowired
    private CacheRedis cacheRedis;

    private DateTime sentiveStartDate;

    private DateTime sentiveEndDate;

    private DateTime uploadStartDate;

    private DateTime uploadEndDate;


    /**
     * 分块上传第一个文件
     *
     * @param file
     * @return path
     * @throws IOException
     */
    public String uploadFile(MultipartFile file) throws IOException {
        StorePath group = appendFileStorageClient.uploadAppenderFile(groupName, file.getInputStream(), file.getSize(), FilenameUtils.getExtension(file.getOriginalFilename()));
        return group.getPath();
    }

    /**
     * 采用文件流加密文件(分块中的n-1块必须为8的倍数的长度，第n块可以为任意长度)
     *
     * @param file
     * @param fileKey   整体文件唯一性key
     * @param personKey 上传人员唯一性key（防止高并发下不同上传人上传同样的文件出现文件拼接错误）
     * @param currentNo 当前文件块
     * @param totalSize 总文件块数
     * @return 成功上传块数和文件所在服务器全路径
     * @throws Exception
     */
    public Map<String, Object> uploadCipherSensitiveFile(MultipartFile file,
                                                         String fileKey,
                                                         String personKey,
                                                         String currentNo,
                                                         String totalSize) throws Exception {
        if (Integer.parseInt(currentNo) > Integer.parseInt(totalSize)) {
            throw new BaseException("currentNo more then totalSize...");
        }
        if (StringUtils.isEmpty(personKey)) {
            throw new BaseException("分块上传上传人唯一性不能为空...");
        }
        String appendPersonKey = FileComtants.REDIS_KEY_CON_APPEND_FILE
                + personKey
                + FileComtants.REDIS_KEY_CON_APPEND_FILE;
        log.info(fileKey);
        Map<String, Object> result = new HashMap<String, Object>(256);
        //先从缓存中获取已经传成功的文件块(包含文件唯一性，上传人唯一性)
        String fileAppendInfoJson = cacheRedis.get(FileComtants.REDIS_KEY_APPEND_FILE
                + appendPersonKey
                + fileKey
        );
        if (StringUtils.isEmpty(fileAppendInfoJson)) {
            //防止缓存穿透设置null值，时长30分钟
            cacheRedis.set(FileComtants.REDIS_KEY_APPEND_FILE, null, 30);
        }
        FileAppendInfoVO fileAppendInfoVO = JSONObject.parseObject(fileAppendInfoJson, FileAppendInfoVO.class);
        if (fileAppendInfoVO != null && Integer.parseInt(fileAppendInfoVO.getSuccessSize())
                >= Integer.parseInt(currentNo)) {
            result.put("isSuccessNo", fileAppendInfoVO.getSuccessSize());
            result.put("path", fileAppendInfoVO.getFilePath());
            return result;
        }
        String path = null;
        String fullPath = null;
        //是传的是第一块以后的文件,path已经存在在缓存中
        if (fileAppendInfoVO != null) {
            fullPath = fileAppendInfoVO.getFilePath();
            path = fileAppendInfoVO.getFilePath();
            path = path.substring(path.indexOf("/") + 1, path.length());
        }
        sentiveStartDate = new DateTime();
        FileDeEncrypt deEncrypt = new FileDeEncrypt(FileComtants.ENCRYPT_ROLE);
        byte[] bytes = deEncrypt.encryptFile(file.getBytes());
        sentiveEndDate = new DateTime();
        log.info("加密文件时间为：");
        this.getDatePoor(sentiveStartDate, sentiveEndDate);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        //1.传第一块的文件到服务器
        if (StringUtils.isEmpty(path)) {
            return this.uploadFirstPart(inputStream, bytes.length, fileKey, appendPersonKey, currentNo, totalSize);
        }
        //2.续传文件到服务器
        return this.appendFile(inputStream, bytes.length, fileKey, appendPersonKey, path, fullPath, currentNo, totalSize);
    }


    /**
     * 文件分块上传第一块文件到服务器处理
     *
     * @param inputStream
     * @param length
     * @param fileKey
     * @param appendKey
     * @param currentNo
     * @param totalSize
     * @return
     */
    private synchronized Map<String, Object> uploadFirstPart(ByteArrayInputStream inputStream,
                                                             int length,
                                                             String fileKey,
                                                             String appendKey,
                                                             String currentNo,
                                                             String totalSize) throws Exception{
        uploadStartDate = new DateTime();
        StorePath storePath = appendFileStorageClient.uploadAppenderFile(groupName, inputStream, length, FilenameUtils.getExtension(sensitiveOriginalFile));
        try {
            //1.1 缓存成功的文件块信息，文件块有效时长30分钟
            FileAppendInfoVO fileAppendInfoCache = this.tranferVO(fileKey, currentNo, storePath.getFullPath(), totalSize);
            cacheRedis.set(FileComtants.REDIS_KEY_APPEND_FILE
                            + appendKey
                            + fileKey,
                    JSONObject.toJSONString(fileAppendInfoCache),
                    30);
            //1.2该缓存值用于如果用户分块时，没有全部上传文件，可能存在垃圾文件片，删除这种情况下的垃圾文件
            cacheRedis.set(FileComtants.REDIS_KEY_PRE_APPEND_FILE
                            + appendKey
                            + fileKey,
                    JSONObject.toJSONString(fileAppendInfoCache),
                    35, "该缓存值35分钟失效，防止存在垃圾文件片情况");
            uploadEndDate = new DateTime();
            log.info("上传文件并进行redies时间为：");
            this.getDatePoor(uploadStartDate, uploadEndDate);
        } catch (Exception e) {
            fastDFSClientWrapper.deleteFile(storePath.getFullPath());
            throw e;
        }
        Map<String, Object> result = new HashMap<String, Object>(256);
        result.put("isSuccessNo", currentNo);
        result.put("path", storePath.getFullPath());
        return result;
    }

    /**
     * 续传文件
     *
     * @param inputStream 文件加密流
     * @param length      长度
     * @param fileKey     文件唯一性
     * @param appendKey   上传人唯一性
     * @param path        续传path（不带卷）
     * @param fullPath    全路径
     * @param currentNo   当前文件块
     * @param totalSize   整体文件块数
     * @return
     */
    private synchronized Map<String, Object> appendFile(ByteArrayInputStream inputStream,
                                                        int length,
                                                        String fileKey,
                                                        String appendKey,
                                                        String path,
                                                        String fullPath,
                                                        String currentNo,
                                                        String totalSize)throws Exception {
        //解析fullpath
        if(!StringUtils.isEmpty(fullPath)){
            groupName = fullPath.substring(0,fullPath.indexOf("/"));
        }
        uploadStartDate = new DateTime();
        //查询原始文件信息
        FileInfo fileInfo = appendFileStorageClient.queryFileInfo(groupName, path);
        log.info("上传成功的文件长度为{}",fileInfo.getFileSize());
        appendFileStorageClient.appendFile(groupName, path, inputStream, length);
        try {
            //3.缓存成功的文件块信息,文件块有效时长30分钟
            FileAppendInfoVO fileAppendInfoCache = this.tranferVO(fileKey, currentNo, fullPath, totalSize);
            cacheRedis.set(FileComtants.REDIS_KEY_APPEND_FILE
                            + appendKey
                            + fileKey,
                    JSONObject.toJSONString(fileAppendInfoCache), 30);
            //4. 该缓存值用于如果用户分块时，没有全部上传文件，可能存在垃圾文件片，删除这种情况下的垃圾文件
            cacheRedis.set(FileComtants.REDIS_KEY_PRE_APPEND_FILE
                            + appendKey
                            + fileKey,
                    JSONObject.toJSONString(fileAppendInfoCache),
                    35, "该缓存值35分钟失效，防止存在垃圾文件片情况");
            uploadEndDate = new DateTime();
            log.info("续传文件并进行redies时间为：");
            this.getDatePoor(uploadStartDate, uploadEndDate);
        } catch (Exception e) {
            //针对redis连接不上，fastdfs服务好用的情况，如果fast不好使的情况续传就异常了直接返回给前端
            appendFileStorageClient.truncateFile(groupName,path,fileInfo.getFileSize());
            throw e;
        }
        Map<String, Object> result = new HashMap<String, Object>(256);
        result.put("isSuccessNo", currentNo);
        result.put("path", fullPath);
        return result;
    }

    public String getDatePoor(DateTime startTime, DateTime endTime) {
        Interval interval = new Interval(startTime, endTime);
        log.info("响应时间:{}毫秒", interval.toDurationMillis());
        return "";
    }

    /**
     * 缓存清除文件
     *
     * @param path
     * @param truncatedFileSize
     */
    public void truncateFile(String path, long truncatedFileSize) {
        appendFileStorageClient.truncateFile(groupName, path, truncatedFileSize);
    }

    /**
     * 文件分块要拼接的文件
     *
     * @param file
     * @param path
     * @throws IOException
     */
    private void appendFile(MultipartFile file, String path) throws IOException {
        appendFileStorageClient.appendFile(groupName, path, file.getInputStream(), file.getSize());
    }

    private FileAppendInfoVO tranferVO(String md5Key, String currentNo, String path, String totalSize) {
        FileAppendInfoVO fileAppendInfoVO = new FileAppendInfoVO();
        fileAppendInfoVO.setSuccessSize(currentNo);
        fileAppendInfoVO.setFilePath(path);
        fileAppendInfoVO.setMd5Key(md5Key);
        fileAppendInfoVO.setTotalSize(totalSize);
        return fileAppendInfoVO;
    }

}
