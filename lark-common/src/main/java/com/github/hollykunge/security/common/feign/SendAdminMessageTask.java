package com.github.hollykunge.security.common.feign;


/**
 * 一个发送admin错误日志的任务
 * @author zhhongyu
 * @since 2020.5.19
 */
public class SendAdminMessageTask implements Runnable {
    private AdminErrorFeign errorLogFeign;
    private ErrorMessageEntity errorMessageEntity;

    public SendAdminMessageTask(AdminErrorFeign errorLogFeign, ErrorMessageEntity errorMessageEntity) {
        this.errorLogFeign = errorLogFeign;
        this.errorMessageEntity = errorMessageEntity;
    }
    @Override
    public void run() {
        errorLogFeign.saveLog(errorMessageEntity);
    }
}
