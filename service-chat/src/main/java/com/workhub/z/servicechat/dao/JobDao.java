package com.workhub.z.servicechat.dao;

import java.util.List;

/**
 * 定时任务
 *
 * @author zhuqz
 * @since 2019-06-12
 */
public interface JobDao {
    /**
     * 定时任务-处理无用附件
     *
     */
    List<String> getUnUsedFileList();
    /**
     * 定时任务-删除无用附件
     *
     */
    int delUnUsedFileList();
}