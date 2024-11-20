package com.banquemisr.challenge05.service;

public interface TaskNotificationService {
    void notifyUser(String email, String taskTitle, String deadline);

    void sendPendingTaskReminders();
}
