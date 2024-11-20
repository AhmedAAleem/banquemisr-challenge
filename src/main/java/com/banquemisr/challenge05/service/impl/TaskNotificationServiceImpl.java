package com.banquemisr.challenge05.service.impl;

import com.banquemisr.challenge05.exception.email.EmailServiceException;
import com.banquemisr.challenge05.repository.TaskRepository;
import com.banquemisr.challenge05.model.Task;
import com.banquemisr.challenge05.service.EmailService;
import com.banquemisr.challenge05.service.TaskNotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TaskNotificationServiceImpl implements TaskNotificationService {

    private final EmailService emailService;
    private final TaskRepository taskRepository;


    @Override
    public void sendPendingTaskReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourFromNow = now.plusHours(1);
        List<Task> tasks = taskRepository.findPendingTasksByDeadline(now, oneHourFromNow);

        if (tasks.isEmpty()) {
            log.info("No tasks with upcoming deadlines.");
            return;
        }
        tasks.forEach(task -> notifyUser(task.getAssignedTo().getEmail(), task.getTitle(), task.getDueDate().toString()));
    }

    @Override
    public void notifyUser(String email, String taskTitle, String deadline) {
        String subject = "Task Deadline Reminder: " + taskTitle;
        String body = String.format("Your task '%s' is due on %s.", taskTitle, deadline);
        try {
            log.info("Preparing to send task reminder email to: {}", email);
            emailService.sendEmail(email, subject, body);
            log.info("Task reminder email sent successfully to: {}", email);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new EmailServiceException(e.getMessage());
        }
    }
}

