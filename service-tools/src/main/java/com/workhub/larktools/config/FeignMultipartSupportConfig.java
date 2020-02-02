package com.workhub.larktools.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

//@Configuration
public class FeignMultipartSupportConfig  implements RequestInterceptor {
    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder();
    }
    @Override
    public void apply(RequestTemplate template) {
        //template.header("path","123123");
        ServletRequestAttributes attributes = null;
        try {
            attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    //上传附件需要排除这个
                    if(!"content-type".equals(name)){
                        template.header(name, values);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
