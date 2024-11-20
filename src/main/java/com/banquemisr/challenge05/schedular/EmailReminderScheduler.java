package com.banquemisr.challenge05.schedular;

import com.banquemisr.challenge05.service.TaskNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailReminderScheduler implements Job {

    private final TaskNotificationService taskNotificationService;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            log.info("Executing Email Reminder Job");
            taskNotificationService.sendPendingTaskReminders();
        } catch (Exception e) {
            log.error("Error executing email reminder job", e);
            throw new JobExecutionException(e);
        }
    }
}