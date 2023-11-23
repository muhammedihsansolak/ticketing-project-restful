package com.cydeo.dto;

import com.cydeo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

    private Long id;

    @NotNull(message = "Project is required")
    private ProjectDTO project;

    @NotNull(message = "Assigned employee is required")
    private UserDTO assignedEmployee;

    @NotBlank(message = "Task subject is required")
    private String taskSubject;

    @NotBlank(message = "Task detail is required")
    private String taskDetail;

    private Status taskStatus;
    private LocalDate assignedDate;

}
