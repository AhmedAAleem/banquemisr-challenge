package com.banquemisr.challenge05.mapper;

import com.banquemisr.challenge05.payload.task.request.CreateTaskDto;
import com.banquemisr.challenge05.payload.task.request.GetTaskDTO;
import com.banquemisr.challenge05.payload.task.request.UpdateTaskDTO;
import com.banquemisr.challenge05.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(target = "userId", source = "assignedTo.id")
    @Mapping(target = "username", source = "assignedTo.username")
    GetTaskDTO toGetDTO(Task task);

    @Mapping(target = "assignedTo", ignore = true)
    Task toTaskEntity(CreateTaskDto createTaskDTO);

    @Mapping(target = "userId", source = "assignedTo.id")
    CreateTaskDto toTaskResponse(Task task);

    @Mapping(target = "userId", source = "assignedTo.id")
    @Mapping(target = "username", source = "assignedTo.username")
    UpdateTaskDTO toUpdateDTO(Task task);

    @Mapping(target = "assignedTo", ignore = true)
    void updateEntityFromDTO(UpdateTaskDTO dto, @MappingTarget Task entity);

}
