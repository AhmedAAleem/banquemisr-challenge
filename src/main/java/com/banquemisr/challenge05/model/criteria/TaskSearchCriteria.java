package com.banquemisr.challenge05.model.criteria;

import com.banquemisr.challenge05.enums.TaskPriority;
import com.banquemisr.challenge05.enums.TaskStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskSearchCriteria {

    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDateTime dueDateFrom;
    private LocalDateTime dueDateTo;
    private String sortBy;
    private int page;
    private int size;
    private Long assignedUserId;

}
