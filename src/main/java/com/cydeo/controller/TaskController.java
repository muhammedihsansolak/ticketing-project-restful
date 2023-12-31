package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.TaskDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/v1/task")
@Tag(name="Task Controller", description = "Task API")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @RolesAllowed({"Manager"})
    @Operation(summary = "Get all tasks")
    public ResponseEntity<ResponseWrapper> getTasks(){
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .message("Tasks are successfully retrieved!")
                .code(HttpStatus.OK.value())
                .data(taskService.listAllTasks())
                .build()
        );
    }

    @GetMapping("/{taskId}")
    @RolesAllowed({"Manager"})
    @Operation(summary = "Get task by id")
    public ResponseEntity<ResponseWrapper> getTaskById
            (@PathVariable("taskId")Long taskId)
    {
        TaskDTO taskDTO = taskService.findById(taskId);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .message("Task "+ taskDTO.getId() +" is successfully retrieved!")
                .code(HttpStatus.OK.value())
                .data(taskDTO)
                .build()
        );
    }

    @PostMapping
    @RolesAllowed({"Manager"})
    @Operation(summary = "Creates task")
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO taskDTO)
    {
        taskService.save(taskDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseWrapper.builder()
                        .success(true)
                        .message("Task is successfully created!")
                        .code(HttpStatus.CREATED.value())
                        .build()
                );
    }

    @PutMapping
    @RolesAllowed({"Manager"})
    @Operation(summary = "Updates task")
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody TaskDTO taskDTO){
        taskService.update(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .success(true)
                .message("Task is successfully updated!")
                .code(HttpStatus.CREATED.value())
                .data(taskDTO)
                .build()
        );
    }

    @DeleteMapping("/{id}")
    @RolesAllowed({"Manager"})
    @Operation(summary = "Deletes task")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("id")Long taskId){
        taskService.delete(taskId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseWrapper.builder()
                        .success(true)
                        .message("Task is successfully deleted!")
                        .code(HttpStatus.OK.value())
                        .build()
                );
    }

    @GetMapping("/employee/pending-tasks")
    @RolesAllowed({"Employee"})
    @Operation(summary = "Retrieves employee's pending tasks")
    public ResponseEntity<ResponseWrapper> employeePendingTasks(){
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .message("Tasks are successfully retrieved!")
                .code(HttpStatus.OK.value())
                .data(taskService.listAllTasksByStatusIsNot(Status.COMPLETE)) //john@employee.com
                .build()
        );
    }

    @PutMapping("/employee/update")
    @RolesAllowed({"Employee"})
    @Operation(summary = "Updates employee's pending tasks")
    public ResponseEntity<ResponseWrapper> employeeUpdateTask(@RequestBody TaskDTO taskDTO){
        taskService.update(taskDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseWrapper.builder()
                        .success(true)
                        .message("Task is successfully updated!")
                        .code(HttpStatus.CREATED.value())
                        .build()
                );
    }

    @GetMapping("/employee/archive")
    @RolesAllowed({"Employee"})
    @Operation(summary = "Retrieves employee's completed tasks")
    public ResponseEntity<ResponseWrapper> employeeArchiveTasks(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseWrapper.builder()
                        .success(true)
                        .message("Completed tasks are successfully retrieved!")
                        .code(HttpStatus.OK.value())
                        .data(taskService.listAllTasksByStatus(Status.COMPLETE))
                        .build()
                );
    }

}
