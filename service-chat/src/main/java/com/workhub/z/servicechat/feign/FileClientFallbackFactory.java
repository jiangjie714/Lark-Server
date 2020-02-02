package com.workhub.z.servicechat.feign;

import com.github.hollykunge.security.common.msg.ObjectRestResponse;
import feign.Response;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 远程调用附件上传异常反馈
 */
@Slf4j
@Component
public class FileClientFallbackFactory implements FallbackFactory<IFileUploadService> {
    @Override
    public IFileUploadService create(Throwable cause) {

        return new IFileUploadService() {
            @Override
            public ObjectRestResponse removeFile(String fileId) {
                log.error("fallback fastdfs delete; file delete reason was: " + cause.getMessage());
                return new ObjectRestResponse().rel(false).msg("service is now closed");
            }
        };
    }
}

