package com.banquemisr.challenge05.repository;

import com.banquemisr.challenge05.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    @Query("SELECT t FROM Task t WHERE t.dueDate BETWEEN :now AND :oneHourFromNow AND t.status IN ('TODO', 'IN_PROGRESS')")
    List<Task> findPendingTasksByDeadline(LocalDateTime now, LocalDateTime oneHourFromNow);


}
