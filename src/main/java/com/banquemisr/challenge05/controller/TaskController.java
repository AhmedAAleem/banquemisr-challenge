package com.banquemisr.challenge05.controller;

import com.banquemisr.challenge05.enums.TaskPriority;
import com.banquemisr.challenge05.enums.TaskStatus;
import com.banquemisr.challenge05.model.criteria.TaskSearchCriteria;
import com.banquemisr.challenge05.payload.task.request.CreateTaskDto;
import com.banquemisr.challenge05.payload.task.request.GetTaskDTO;
import com.banquemisr.challenge05.payload.task.request.UpdateTaskDTO;
import com.banquemisr.challenge05.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${api.prefix.tasks}")
@AllArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<CreateTaskDto> createTask(@Valid @RequestBody CreateTaskDto createTaskDto) {
        CreateTaskDto createdTask = taskService.create(createTaskDto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetTaskDTO> getTaskById(@PathVariable Long id) {
        GetTaskDTO getTaskDTO = taskService.getTaskById(id);
        return ResponseEntity.ok(getTaskDTO);
    }

    @GetMapping
    public ResponseEntity<Page<GetTaskDTO>> getAllTasks(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "priority", required = false) TaskPriority priority,
            @RequestParam(value = "status", required = false) TaskStatus status,
            @RequestParam(value = "dueDateFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDateFrom,
            @RequestParam(value = "dueDateTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueDateTo,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        TaskSearchCriteria criteria = TaskSearchCriteria.builder()
                .title(title)
                .description(description)
                .priority(priority)
                .status(status)
                .dueDateFrom(dueDateFrom)
                .dueDateTo(dueDateTo)
                .sortBy(sortBy)
                .page(page)
                .size(size)
                .build();

        Page<GetTaskDTO> tasks = taskService.getAll(criteria);

        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateTaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody UpdateTaskDTO updateTaskDTO) {
        UpdateTaskDTO updatedTaskDTO = taskService.update(id, updateTaskDTO);
        return ResponseEntity.ok(updatedTaskDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
