package com.banquemisr.challenge05.config;

import com.banquemisr.challenge05.schedular.EmailReminderScheduler;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail emailReminderJobDetail() {
        return JobBuilder.newJob(EmailReminderScheduler.class)
                .withIdentity("emailReminderJob", "emailJobsGroup")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger emailReminderTrigger(JobDetail emailReminderJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(emailReminderJobDetail)
                .withIdentity("emailReminderTrigger", "emailJobsGroup")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInHours(1)
                        .repeatForever())
                .build();
    }
}
