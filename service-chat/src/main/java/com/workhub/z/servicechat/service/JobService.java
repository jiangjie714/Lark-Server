package com.workhub.z.servicechat.service;

/**
 * 定时任务
 *
 * @author zhuqz
 * @since 2019-06-12
 */
public interface JobService {

    /**
     * 执行定时任务-处理历史消息记录
     *
     */
    public String backupHisMsg() throws Exception;
    /**
     * 定时任务-处理无用附件
     *
     */
    void dealUnUsedFileList() throws Exception;
}