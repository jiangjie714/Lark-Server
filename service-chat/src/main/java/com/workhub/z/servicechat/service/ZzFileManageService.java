package com.workhub.z.servicechat.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 附件上传下载等相关功能service
 *
 * @author zhuqz
 * @since 2019-06-04 10:22:34
 */
public interface ZzFileManageService {
    public Map<String, String> singleFileUpload(MultipartFile file) throws Exception;

    public String delUploadFile(String path) throws Exception;

    public HttpServletResponse downloadFile(HttpServletResponse response, String filePath, String fileName) throws Exception;
}
