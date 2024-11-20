package com.banquemisr.challenge05.test;

import com.banquemisr.challenge05.exception.userException.ResourceNotFoundException;
import com.banquemisr.challenge05.mapper.TaskMapper;
import com.banquemisr.challenge05.model.Task;
import com.banquemisr.challenge05.model.User;
import com.banquemisr.challenge05.payload.task.request.CreateTaskDto;
import com.banquemisr.challenge05.payload.task.request.GetTaskDTO;
import com.banquemisr.challenge05.payload.task.request.UpdateTaskDTO;
import com.banquemisr.challenge05.repository.TaskRepository;
import com.banquemisr.challenge05.repository.UserRepository;
import com.banquemisr.challenge05.service.EmailService;
import com.banquemisr.challenge05.service.impl.TaskServiceImpl;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TaskServiceImplTest {

    @MockBean
    private TaskRepository taskRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TaskMapper taskMapper;
    @MockBean
    private EmailService emailService;
    @Autowired
    private TaskServiceImpl taskService;


    @Test
    public void testCreateTaskSuccess() {
        CreateTaskDto createTaskDto = new CreateTaskDto();
        createTaskDto.setTitle("New Task");
        createTaskDto.setUserId(1L);

        Task task = new Task();
        task.setTitle("New Task");
        User user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskMapper.toTaskEntity(createTaskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toTaskResponse(task)).thenReturn(createTaskDto);

        CreateTaskDto result = taskService.create(createTaskDto);

        assertNotNull(result);
        verify(emailService).sendEmail(eq("user@example.com"), eq("Task Assigned"), anyString());
    }

    @Test
    public void testUpdateTaskNotFound() {
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO();
        updateTaskDTO.setTitle("Updated Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.update(1L, updateTaskDTO);
        });
    }

    @Test
    public void testGetTaskByIdFound() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");

        GetTaskDTO getTaskDTO = new GetTaskDTO();

        getTaskDTO.setTitle("Task 1");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toGetDTO(task)).thenReturn(getTaskDTO);

        GetTaskDTO result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Task 1", result.getTitle());
    }

    @Test
    public void testGetAllTasks() {
        Task task = new Task();
        task.setTitle("Task");

        List<Task> tasks = List.of(task);

        when(taskRepository.findAll()).thenReturn(tasks);
        when(taskMapper.toGetDTO(any())).thenReturn(new GetTaskDTO());

        List<GetTaskDTO> results = tasks.stream().map(taskMapper::toGetDTO).collect(Collectors.toList());
        assertEquals(1, results.size());
    }


    @Test
    public void testDeleteTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.delete(1L);
        });
    }
}
