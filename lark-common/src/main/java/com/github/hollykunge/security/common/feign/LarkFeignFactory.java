package com.github.hollykunge.security.common.feign;

import com.github.hollykunge.security.common.dictionary.HttpReponseStatusEnum;
import com.github.hollykunge.security.common.exception.server.FeignExecutException;
import com.github.hollykunge.security.common.msg.BaseResponse;
import java.lang.reflect.Proxy;

/**
 * @author: zhhongyu
 * @description: lark-feign单例工厂
 * @since: Create in 15:19 2020/5/14
 */
public class LarkFeignFactory {
    private static LarkFeignFactory ourInstance = new LarkFeignFactory();

    public static LarkFeignFactory getInstance() {
        return ourInstance;
    }

    private LarkFeignFactory() {
    }

    public <T> T loadFeign(Object feignService) {
        Class<?> clazz = feignService.getClass();
        return (T)Proxy.newProxyInstance(clazz.getClassLoader(),clazz.getInterfaces(),(proxy,method,args)->{
            Object response = method.invoke(feignService, args);
            if(response instanceof BaseResponse){
                BaseResponse feignResponse = (BaseResponse) response;
                if(feignResponse.getStatus() == HttpReponseStatusEnum.SYSTEM_ERROR.value()){
                    throw new FeignExecutException(feignResponse.getMessage());
                }
            }
            return response;
        });
    }
}