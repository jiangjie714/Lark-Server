package com.github.hollykunge.security.task.feign;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.vo.FileInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author fansq
 * @since 20-4-16
 * @deprecation taskProject 调用 文件服务
 */
@FeignClient("service-dfsfile")
public interface LarkProjectFileFeign {

    /**
     * 模板图片上传
     * @param file
     * @return
     */
    @RequestMapping(value = "/fdfs/file/upload",method = RequestMethod.POST)
    ObjectRestResponse<FileInfoVO> projectTemplateCover(@RequestParam("file") MultipartFile file);

    /**
     * task关联文件上传
     * @param file
     * @return
     */
    @RequestMapping(value = "/fdfs/file/sensitiveUpload2",method = RequestMethod.POST)
    ObjectRestResponse<FileInfoVO> taskFileUpload(@RequestParam("file") MultipartFile file);

    /**
     * task关联文件下载
     * @param fileId
     */
    @RequestMapping(value ="/fdfs/file/sensitiveDownload2",method = RequestMethod.GET)
    void taskFileDownload(@RequestParam String fileId);
}
