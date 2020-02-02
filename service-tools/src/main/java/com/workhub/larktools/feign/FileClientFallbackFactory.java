package com.workhub.larktools.feign;

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
public class FileClientFallbackFactory implements FallbackFactory<IFastDFSService> {
    @Override
    public IFastDFSService create(Throwable cause) {

        return new IFastDFSService() {
            @Override
            public ObjectRestResponse sensitiveUpload(MultipartFile file) {
                log.error("fallback fastdfs upload; file upload reason was: " + cause.getMessage());
                return new ObjectRestResponse().rel(false).msg("service is now closed");
            }
            @Override
            public ObjectRestResponse uploadAppendFile(MultipartFile file) {
                log.error("fallback fastdfs uploadAppendFile ; file upload reason was: " + cause.getMessage());
                return new ObjectRestResponse().rel(false).msg("service is now closed");
            }
            @Override
            public Response sensitiveDownload(String fileId) {
                log.error("fallback fastdfs download; file upload reason was: " + cause.getMessage());
                return  null;
            }
            @Override
            public ObjectRestResponse removeFile(String fileId) {
                log.error("fallback fastdfs delete; file delete reason was: " + cause.getMessage());
                return new ObjectRestResponse().rel(false).msg("service is now closed");
            }
        };
    }
}

