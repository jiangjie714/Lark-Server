package com.github.hollykunge.security.common.feign;

import com.github.hollykunge.security.common.msg.FeignResponse;
import org.springframework.context.ApplicationEvent;

/**
 * @author: zhhongyu
 * @description: feignresponse事件
 * @since: Create in 9:02 2020/5/14
 */
public class FeignResponseEvent extends ApplicationEvent {
    private FeignResponse feignResponse;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public FeignResponseEvent(Object source,FeignResponse feignResponse) {
        super(source);
        this.feignResponse = feignResponse;
    }

    public FeignResponse getFeignResponse() {
        return feignResponse;
    }

    public void setFeignResponse(FeignResponse feignResponse) {
        this.feignResponse = feignResponse;
    }
}
