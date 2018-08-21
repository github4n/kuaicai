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
    public JobDetail teatQuartzDetail(){
        return JobBuilder.newJob(OpenRecordJob.class).withIdentity("openRecordJob").storeDurably().build();
    }

    @Bean
    public Trigger testQuartzTrigger(){
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(600)  //设置时间周期单位秒
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(teatQuartzDetail())
                .withIdentity("openRecordJob")
                .withSchedule(scheduleBuilder)
                .build();
    }
}
