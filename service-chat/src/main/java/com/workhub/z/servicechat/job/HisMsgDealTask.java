package com.workhub.z.servicechat.job;

import com.workhub.z.servicechat.service.JobService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description 历史消息处理1成功，0+失败消息,-1调用存储过程失败
 * @Author zhuqz
 * @Date 2019-06-12
 */
public class HisMsgDealTask extends QuartzJobBean {
    @Resource
    private JobService jobService;
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println("=========定时任务处理历史消息执行========="+df.format(new Date()));
        try {
            String res = jobService.backupHisMsg();
            System.out.println("=========定时任务处理历史消息执行结果========="+res);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}