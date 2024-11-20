package com.banquemisr.challenge05.service;

import com.banquemisr.challenge05.model.criteria.TaskSearchCriteria;
import com.banquemisr.challenge05.payload.task.request.CreateTaskDto;
import com.banquemisr.challenge05.payload.task.request.GetTaskDTO;
import com.banquemisr.challenge05.payload.task.request.UpdateTaskDTO;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;

public interface TaskService {
    CreateTaskDto create(CreateTaskDto createTaskDto);

    UpdateTaskDTO update(Long id, UpdateTaskDTO updateTaskDTO);

    GetTaskDTO getTaskById(Long id);

    @Cacheable(value = "tasks", key = "#criteria.toString()")
    Page<GetTaskDTO> getAll(TaskSearchCriteria criteria);

    void delete(Long id);

}
