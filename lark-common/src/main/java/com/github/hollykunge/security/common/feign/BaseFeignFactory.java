package com.github.hollykunge.security.common.feign;


import com.github.hollykunge.security.common.dictionary.HttpReponseStatusEnum;
import com.github.hollykunge.security.common.msg.FeignListResponse;
import com.github.hollykunge.security.common.msg.FeignObjectReponse;
import feign.hystrix.FallbackFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: zhhongyu
 * @description: 公共的feign工厂类
 * @since: Create in 16:21 2020/4/28
 */
public class BaseFeignFactory<Base extends BaseHystix> extends BaseHystix implements FallbackFactory<Base> {

    @Autowired
    private Base baseHystrix;

    @Override
    public Base create(Throwable throwable) {
        baseHystrix.setThrowable(throwable);
        return baseHystrix;
    }
    protected FeignListResponse getHystrixListResponse(){
        FeignListResponse feignListResponse = new FeignListResponse(throwable.getMessage(), 0, null);
        feignListResponse.setStatus(HttpReponseStatusEnum.SYSTEM_ERROR.value());
        return feignListResponse;
    }
    protected FeignObjectReponse getHystrixObjectReponse(){
        FeignObjectReponse feignObjectReponse = new FeignObjectReponse();
        feignObjectReponse.setStatus(HttpReponseStatusEnum.SYSTEM_ERROR.value());
        return feignObjectReponse;
    }
}
