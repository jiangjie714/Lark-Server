//package com.github.hollykunge.security.admin.api.hystrix;
//
//import com.github.hollykunge.security.admin.api.log.LogInfo;
//import com.github.hollykunge.security.admin.api.service.AdminLogServiceFeignClient;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//
///**
// * @author: zhhongyu
// * @description: hystrix降级
// * @since: Create in 15:37 2019/12/27
// */
//@Slf4j
//@Component
//public class AdminLogServiceFallback implements AdminLogServiceFeignClient {
//    @Override
//    public void saveLog(LogInfo info) {
//        log.error("服务中的方法{}出现错误,已经被降级处理...","saveLog");
//        return;
//    }
//}
