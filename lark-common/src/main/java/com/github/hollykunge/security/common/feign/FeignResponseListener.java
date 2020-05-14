package com.github.hollykunge.security.common.feign;

import com.github.hollykunge.security.common.exception.BaseException;
import com.github.hollykunge.security.common.msg.FeignResponse;
import org.springframework.context.ApplicationListener;

/**
 * @author: zhhongyu
 * @description: feignResponse 监听
 * @since: Create in 9:10 2020/5/14
 */
public class FeignResponseListener implements ApplicationListener<FeignResponseEvent> {
    @Override
    public void onApplicationEvent(FeignResponseEvent event) {
        FeignResponse feignResponse = event.getFeignResponse();
        if(feignResponse.getStatus() == 500){
            throw new BaseException("接口调用异常");
        }
    }
}
