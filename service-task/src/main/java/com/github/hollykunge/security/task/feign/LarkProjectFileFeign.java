package com.github.hollykunge.security.task.feign;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import com.github.hollykunge.security.common.vo.FileInfoVO;
import com.github.hollykunge.security.task.config.FeignMultipartFileConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author fansq
 * @since 20-4-16
 * @deprecation taskProject 调用 文件服务
 */
@FeignClient(value = "service-dfsfile",configuration = FeignMultipartFileConfig.class)
public interface LarkProjectFileFeign {

    /**
     * 模板图片上传
     * @param file
     * @return
     */
    @RequestMapping(value = "/fdfs/file/upload",method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ObjectRestResponse<FileInfoVO> projectTemplateCover(@RequestPart("file") MultipartFile file);

    /**
     * task关联文件上传
     * @param file
     * @return
     */
    @RequestMapping(value = "/fdfs/file/sensitiveUpload2",method = RequestMethod.POST
            ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE
           )
    ObjectRestResponse<FileInfoVO> taskFileUpload(@RequestPart("file") MultipartFile file);

    /**
     * task关联文件下载
     * @param fileId
     */
    @RequestMapping(value ="/fdfs/file/sensitiveDownload2",method = RequestMethod.GET,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
            )
    void taskFileDownload(@RequestPart String fileId);
}
