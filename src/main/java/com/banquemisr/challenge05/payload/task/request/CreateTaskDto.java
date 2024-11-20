package com.banquemisr.challenge05.payload.task.request;

import com.banquemisr.challenge05.enums.TaskPriority;
import com.banquemisr.challenge05.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTaskDto {

    @NotBlank(message = "Please insert task title")
    @Size(max = 100, message = "Title maximum length is 100 characters")
    @JsonProperty("title")
    private String title;

    @Size(max = 400, message = "Description maximum length is 400 characters")
    @JsonProperty("description")
    private String description;

    @NotNull(message = "Priority is required")
    @JsonProperty("priority")
    private TaskPriority priority;

    @NotNull(message = "Due date is required")
    @JsonProperty("dueDate")
    private LocalDateTime dueDate;

    @NotNull(message = "Status is required")
    @JsonProperty("status")
    private TaskStatus status;

    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("username")
    private String username;

}
