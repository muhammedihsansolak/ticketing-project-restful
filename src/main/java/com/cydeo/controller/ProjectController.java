package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/v1/project")
@Tag(name="Project Controller", description = "Project API")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @RolesAllowed({"Manager"})
    @Operation(summary = "Get all projects")
    public ResponseEntity<ResponseWrapper> getProjects(){
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .message("Projects are successfully retrieved!")
                .code(HttpStatus.OK.value())
                .data(projectService.listAllProjects())
                .build()
        );
    }

    @GetMapping("/{projectCode}")
    @RolesAllowed({"Manager"})
    @Operation(summary = "Get project by project code")
    public ResponseEntity<ResponseWrapper> getProjectByProjectCode
            (@PathVariable("projectCode")String projectCode)
    {
        ProjectDTO projectDTO = projectService.getByProjectCode(projectCode);
        return ResponseEntity.ok(ResponseWrapper.builder()
                .success(true)
                .message("Project "+ projectDTO.getProjectName() +" is successfully retrieved!")
                .code(HttpStatus.OK.value())
                .data(projectDTO)
                .build()
        );
    }

    @PostMapping
    @RolesAllowed({"Admin","Manager"})
    @Operation(summary = "Creates project")
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO){
        projectService.save(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .success(true)
                .message("Project "+ projectDTO.getProjectName() +" is successfully created!")
                .code(HttpStatus.CREATED.value())
                .data(projectDTO)
                .build()
        );
    }

    @PutMapping
    @RolesAllowed({"Admin","Manager"})
    @Operation(summary = "Updates project")
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO){
        projectService.update(projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseWrapper.builder()
                .success(true)
                .message("Project "+ projectDTO.getProjectName() +" is successfully updated!")
                .code(HttpStatus.CREATED.value())
                .data(projectDTO)
                .build()
        );
    }

    @DeleteMapping("{projectCode}")
    @RolesAllowed({"Admin","Manager"})
    @Operation(summary = "Deletes project")
    public ResponseEntity<ResponseWrapper> deleteProject
            (@PathVariable("projectCode")String projectCode)
    {
        projectService.delete(projectCode);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.builder()
                .success(true)
                .message("Project "+ projectCode+" is successfully deleted!")
                .code(HttpStatus.OK.value())
                .build()
        );
    }

    @GetMapping("/manager/project-status")
    @RolesAllowed({"Manager"})
    @Operation(summary = "Retrieves all projects based on assigned manager")
    public ResponseEntity<ResponseWrapper> getProjectByManager(){
        projectService.listAllProjectDetails();
        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.builder()
                .success(true)
                .message("Project are successfully retrieved!")
                .code(HttpStatus.OK.value())
                .data(projectService.listAllProjectDetails()) //harold@manager.com
                .build()
        );
    }

    @PutMapping("/manager/complete/{projectCode}")
    @RolesAllowed({"Manager"})
    @Operation(summary = "Completes project")
    public ResponseEntity<ResponseWrapper> completeProject
            (@PathVariable("projectCode")String projectCode)
    {
        projectService.complete(projectCode);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseWrapper.builder()
                .success(true)
                .message("Project "+ projectCode+" is successfully completed!")
                .code(HttpStatus.OK.value())
                .build()
        );
    }

}
