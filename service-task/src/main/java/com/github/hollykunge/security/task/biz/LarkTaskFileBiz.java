package com.github.hollykunge.security.task.biz;

import com.github.hollykunge.security.common.biz.BaseBiz;
import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.msg.TableResultResponse;
import com.github.hollykunge.security.common.util.Query;
import com.github.hollykunge.security.common.vo.FileInfoVO;
import com.github.hollykunge.security.task.constant.TaskCommon;
import com.github.hollykunge.security.task.entity.LarkFile;
import com.github.hollykunge.security.task.feign.LarkProjectFileFeign;
import com.github.hollykunge.security.task.mapper.LarkFileMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import feign.Response;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class LarkTaskFileBiz extends BaseBiz<LarkFileMapper, LarkFile> {

    @Autowired
    private LarkProjectFileFeign larkProjectFileFeign;

    @Autowired
    private LarkFileMapper larkFileMapper;
    @Override
    protected String getPageName() {
        return null;
    }

    /**
     * 上传任务关联文件
     * @param file 文件
     * @param taskCode 任务id
     * @param projectCode 项目id
     * @return
     */
    public ObjectRestResponse<LarkFile> taskFileUpload(MultipartFile file,String taskCode,String projectCode,String userId) {
        ObjectRestResponse<FileInfoVO> restResponse = larkProjectFileFeign.taskFileUpload(file);
        FileInfoVO taskFile = restResponse.getResult();
        String fileFullPath = taskFile.getFullPath();
        String fileName = file.getOriginalFilename();
        //获取上传文件后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //文件大小
        Long fileSize= file.getSize();
        LarkFile larkFile = new LarkFile();
        larkFile.setTitle(taskFile.getFileName());
        larkFile.setId(taskFile.getFileId());
        larkFile.setFileSize(fileSize.intValue());
        larkFile.setProjectCode(projectCode);
        larkFile.setTaskCode(taskCode);
        larkFile.setCreateBy(userId);
        larkFile.setDeleted("0");
        larkFile.setPathName(fileFullPath);
        larkFile.setExtension(suffixName);
        mapper.insertSelective(larkFile);
        return new ObjectRestResponse<>().data(larkFile).rel(true);
    }


    /**
     * task 文件关联下载
     * @param fileId
     * @return
     */
    public void taskFileDownload(String fileId, HttpServletResponse servletResponse) throws IOException {
        LarkFile larkFile = mapper.selectByPrimaryKey(fileId);
        if(ObjectUtils.isEmpty(larkFile)){
            throw new BaseException("文件不存在！");
        }
        Response response = larkProjectFileFeign.taskFileDownload(larkFile.getId());
        Response.Body body = response.body();
        InputStream inputStream = body.asInputStream();
        byte[] data = new byte[10240];
        while (true) {
            int len = inputStream.read(data);
            if (len < 0) {
                break;
            }
        }
        inputStream.read(data);
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(larkFile.getTitle(), "UTF-8"));
        ServletOutputStream outputStream = servletResponse.getOutputStream();
        IOUtils.write(data, outputStream);
        larkFile.setId(fileId);
        if(StringUtils.isEmpty(larkFile.getDownloads())){
            larkFile.setDownloads(TaskCommon.NUMBER_ONE);
        }else{
            larkFile.setDownloads(larkFile.getDownloads()+1);
        }
        mapper.updateByPrimaryKeySelective(larkFile);
    }


    /**
     * 根据taskCode 或者 ProjectCode 获取文件列表
     * @param query
     * @return
     */
    public TableResultResponse<LarkFile> taskFileList(Query query) {
        Page<Object> result = PageHelper.startPage(query.getPageNo(), query.getPageSize());
        Object projectCode = query.get("projectCode");
        Object taskCode = query.get("taskCode");
        List<LarkFile> larkFiles = larkFileMapper.getTaskFileList(projectCode,taskCode);
        return new TableResultResponse<>(result.getPageSize(), result.getPageNum(), result.getPages(), result.getTotal(), larkFiles);
    }


}