package com.banquemisr.challenge05.service.impl;

import com.banquemisr.challenge05.exception.userException.ResourceNotFoundException;
import com.banquemisr.challenge05.repository.TaskRepository;
import com.banquemisr.challenge05.repository.UserRepository;
import com.banquemisr.challenge05.mapper.TaskMapper;
import com.banquemisr.challenge05.model.Task;
import com.banquemisr.challenge05.model.User;
import com.banquemisr.challenge05.model.criteria.TaskSearchCriteria;
import com.banquemisr.challenge05.model.specification.TaskSpecification;
import com.banquemisr.challenge05.payload.task.request.CreateTaskDto;
import com.banquemisr.challenge05.payload.task.request.GetTaskDTO;
import com.banquemisr.challenge05.payload.task.request.UpdateTaskDTO;
import com.banquemisr.challenge05.service.EmailService;
import com.banquemisr.challenge05.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public CreateTaskDto create(CreateTaskDto createTaskDto) {
        log.info("Start Creating a new task with title: {}", createTaskDto.getTitle());

        Task task = taskMapper.toTaskEntity(createTaskDto);
        User user = null;
        if (createTaskDto.getUserId() != null) {
            user = userRepository.findById(createTaskDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + createTaskDto.getUserId()));
            task.setAssignedTo(user);
        } else if (createTaskDto.getUsername() != null) {
            user = userRepository.findByUserName(createTaskDto.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + createTaskDto.getUsername()));
            task.setAssignedTo(user);
        }
        Task createdTask = taskRepository.save(task);
        if (user != null) {
            emailService.sendEmail(
                    user.getEmail(),
                    "Task Assigned",
                    "You have been assigned a new task: " + task.getTitle());
        }
        log.info("End Creating a new task with title: {}", createTaskDto.getTitle());
        return taskMapper.toTaskResponse(createdTask);
    }

    @Override
    public UpdateTaskDTO update(Long id, UpdateTaskDTO updateTaskDTO) {
        log.info("Start Updating Task with Id:{},and request body :{}", id, updateTaskDTO);
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        taskMapper.updateEntityFromDTO(updateTaskDTO, existingTask);

        User user = null;
        if (updateTaskDTO.getUserId() != null) {
            user = userRepository.findById(updateTaskDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + updateTaskDTO.getUserId()));
            existingTask.setAssignedTo(user);
        } else if (updateTaskDTO.getUsername() != null) {
            user = userRepository.findByUserName(updateTaskDTO.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + updateTaskDTO.getUsername()));
            existingTask.setAssignedTo(user);
        }

        Task updatedTask = taskRepository.save(existingTask);

        if (user != null) {
            emailService.sendEmail(
                    user.getEmail(),
                    "Task Updated",
                    "Your task has been updated: " + existingTask.getTitle());
        }
        log.info("End Updating Task with Id:{},and request body :{}", id, updateTaskDTO);
        return taskMapper.toUpdateDTO(updatedTask);
    }

    @Override
    public GetTaskDTO getTaskById(Long id) {
        log.info("Start get Task with By Id:{}", id);
        Task task = taskRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("No Task found with id: " + id));
        log.info("End get Task with By Id:{}", id);
        return taskMapper.toGetDTO(task);
    }

    @Override
    public Page<GetTaskDTO> getAll(TaskSearchCriteria criteria) {
        log.info("Start Searching for Task with criteria:{}", criteria);
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), Sort.by(criteria.getSortBy()));
        Specification<Task> specification = new TaskSpecification(criteria);
        Page<Task> tasks = taskRepository.findAll(specification, pageable);
        log.info("Start Searching for Task with criteria:{}", criteria);
        return tasks.map(taskMapper::toGetDTO);
    }

    @Override
    public void delete(Long id) {
        log.info("Start Deleting task with Id:{}", id);
        Task task = taskRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("No Task found with id: " + id));
        taskRepository.delete(task);
        log.info("End Deleting task with Id:{}", id);
    }

}

