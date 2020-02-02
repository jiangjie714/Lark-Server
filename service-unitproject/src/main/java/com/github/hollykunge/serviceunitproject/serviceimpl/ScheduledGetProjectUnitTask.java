package com.github.hollykunge.serviceunitproject.serviceimpl;


import com.github.hollykunge.serviceunitproject.service.ProjectUnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class ScheduledGetProjectUnitTask implements SchedulingConfigurer {

    @Autowired
    ProjectUnitService projectUnitService;
    @Value("${spring.cronb}")
    private String cron;

    public ScheduledGetProjectUnitTask() {
        //默认情况是：每5秒执行一次.
        cron = cron;
     /*   new Thread(new Runnable() {
            // 开启新线程模拟外部更改了任务执行周期.
            @Override
            public void run() {
                try {
                    // 让线程睡眠 15秒.
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }
                //修改为：每10秒执行一次.
                cron = "0/10 * * * * *";
                System.err.println("cron change to:"+cron);
            }
        }).start();;*/
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                //任务逻辑代码部分.

                System.out.println("TaskCronChange task is running ... " + cron);
                projectUnitService.saveErbuProject();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                log.info("======定时执行主数据同步任务成功======" + dateFormat.format(date));
            }
        };

        Trigger trigger = new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                //任务触发，可修改任务的执行周期.
                CronTrigger trigger = new CronTrigger(cron);
                Date nextExec = trigger.nextExecutionTime(triggerContext);
                return nextExec;
            }
        };
        scheduledTaskRegistrar.addTriggerTask(task, trigger);
    }
}

