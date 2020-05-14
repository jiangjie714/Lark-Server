package com.github.hollykunge.security.common.feign;


import com.github.hollykunge.security.common.dictionary.HttpReponseStatusEnum;
import com.github.hollykunge.security.common.msg.FeignListResponse;
import com.github.hollykunge.security.common.msg.FeignObjectReponse;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: zhhongyu
 * @description: 公共的feign工厂类
 * @since: Create in 16:21 2020/4/28
 */
@Slf4j
public class BaseFeignFactory<Base extends BaseHystix> extends BaseHystix implements FallbackFactory<Base> {

    @Autowired
    private Base baseHystrix;

    @Override
    public Base create(Throwable throwable) {
        baseHystrix.setThrowable(throwable);
        return baseHystrix;
    }
    protected FeignListResponse getHystrixListResponse(){
        log.error("调用通信接口失败，服务已被降级处理",throwable);
        FeignListResponse feignListResponse = new FeignListResponse(throwable.getMessage(), 0, null);
        feignListResponse.setStatus(HttpReponseStatusEnum.SYSTEM_ERROR.value());
        feignListResponse.setMessage(throwable.getMessage());
        return feignListResponse;
    }
    protected FeignObjectReponse getHystrixObjectReponse(){
        log.error("调用通信接口失败，服务已被降级处理",throwable);
        FeignObjectReponse feignObjectReponse = new FeignObjectReponse();
        feignObjectReponse.setMessage(throwable.getMessage());
        feignObjectReponse.setStatus(HttpReponseStatusEnum.SYSTEM_ERROR.value());
        return feignObjectReponse;
    }
}
