package com.workhub.z.servicechat.job;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description 定时器配置
 * @Author zhuqz
 * @Date 2019-06-12
 */
@Configuration
public class QuartzConfig {
    /**
     * 定时任务查询是否需要审批权限
     */
    @Value("${require_approve_authority_job_cron}")
    private String require_approve_authority_job_cron;
    /*@Value("${deal_unused_file_job_cron}")
    private String deal_unused_file_job_cron;*/

    //前端不需要审批权限推送
    @Bean
    public JobDetail approveAuthorityPush(){
        return JobBuilder.newJob(RequireApproveAuthorityTask.class).withIdentity("approveAuthorityPush").storeDurably().build();
    }
    @Bean
    public Trigger pushApproveAuthorityToFront(){
        return TriggerBuilder.newTrigger().forJob(approveAuthorityPush())
                .withIdentity("approveAuthorityPush")
                .withSchedule(CronScheduleBuilder.cronSchedule(require_approve_authority_job_cron))
                .build();
    }
    //----------------------------处理无用附件定时任务-------------------------------------------------------------
   /* @Bean
    public JobDetail unUsedFileQuartz() {
        return JobBuilder.newJob(UnUsedFileDealTask.class).withIdentity("unUsedFileQuartz").storeDurably().build();
    }

    @Bean
    public Trigger dealUnUsedFileTrigger() {
        return TriggerBuilder.newTrigger().forJob(unUsedFileQuartz())
                .withIdentity("unUsedFileQuartz")
                .withSchedule(CronScheduleBuilder.cronSchedule(deal_unused_file_job_cron))
                .build();
    }*/

}