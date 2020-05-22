package com.github.hollykunge.security.common.feign;


import lombok.extern.slf4j.Slf4j;

/**
 * 一个发送admin错误日志的任务
 * @author zhhongyu
 * @since 2020.5.19
 */
@Slf4j
public class SendAdminMessageTask implements Runnable {
    private AdminErrorFeign errorLogFeign;
    private ErrorMessageEntity errorMessageEntity;

    public SendAdminMessageTask(AdminErrorFeign errorLogFeign, ErrorMessageEntity errorMessageEntity) {
        this.errorLogFeign = errorLogFeign;
        this.errorMessageEntity = errorMessageEntity;
    }
    @Override
    public void run() {
        try{
            errorLogFeign.saveLog(errorMessageEntity);
        }catch (Exception e){
            log.error("系统调用admin，类ErrorLogRest，saveLog 方法失败");
            log.error("系统记录的错误日志为：{}",errorMessageEntity.toString());
        }
    }
}
