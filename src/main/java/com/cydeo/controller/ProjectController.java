package com.cydeo.controller;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.ResponseWrapper;
import com.cydeo.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @RolesAllowed({"Manager"})
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
