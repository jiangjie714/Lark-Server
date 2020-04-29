package com.workhub.z.servicechat.job;

import com.workhub.z.servicechat.config.Common;
import com.workhub.z.servicechat.service.JobService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnUsedFileDealTask  extends QuartzJobBean {
    private static Logger log = LoggerFactory.getLogger(UnUsedFileDealTask.class);
    @Resource
    private JobService jobService;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        log.info("处理无用的附件定时任务:"+df.format(new Date()));
        try {
            jobService.dealUnUsedFileList();
            log.info("处理无用的附件定时任务结束");
        }catch (Exception e){
            e.printStackTrace();
            log.error("处理无用附件错误："+ Common.getExceptionMessage(e));
        }
    }
}
