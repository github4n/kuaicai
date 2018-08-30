package com.caipiao.config;

import com.caipiao.job.OpenRecordJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangjj
 * @create 2018-08-13 11:53
 **/
@Configuration
public class JobConfig {
    @Bean
    public JobDetail quartzDetail(){
        return JobBuilder
                .newJob(OpenRecordJob.class)
                .withIdentity("openRecordJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger quartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(60)  //设置时间周期单位秒
                .withMisfireHandlingInstructionNextWithRemainingCount()
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(quartzDetail())
                .withIdentity("openRecordJob")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
