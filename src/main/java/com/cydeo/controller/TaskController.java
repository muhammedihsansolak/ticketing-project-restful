package com.cydeo.controller;

import com.cydeo.dto.ResponseWrapper;
import com.cydeo.dto.TaskDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @RolesAllowed({"Manager"})
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
