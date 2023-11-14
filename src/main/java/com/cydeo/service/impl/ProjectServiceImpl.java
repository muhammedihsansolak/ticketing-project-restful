package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final TaskService taskService;
    private final MapperUtil mapper;

    @Override
    public ProjectDTO getByProjectCode(String code) {
        Project project = projectRepository.findByProjectCode(code);
        return mapper.convert(project,new ProjectDTO());
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        List<Project> list = projectRepository.findAll(Sort.by("projectCode"));
        return list.stream()
                .map(project -> mapper.convert(project, new ProjectDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(ProjectDTO dto) {
        dto.setProjectStatus(Status.OPEN);
        Project project = mapper.convert(dto,new Project());
        projectRepository.save(project);
    }

    @Override
    public void update(ProjectDTO dto) {
        Project foundProject = projectRepository.findByProjectCode(dto.getProjectCode());
        Project projectToUpdate = mapper.convert(dto, new Project() );
        projectToUpdate.setId(foundProject.getId());
        projectToUpdate.setProjectStatus(foundProject.getProjectStatus());
        projectRepository.save(projectToUpdate);
    }

    @Override
    public void delete(String code) {
        Project project = projectRepository.findByProjectCode(code);
        project.setIsDeleted(true);
        project.setProjectCode(project.getProjectCode() + "-" + project.getId());  // SP03-4
        projectRepository.save(project);
        taskService.deleteByProject(mapper.convert(project, new ProjectDTO()));
    }

    @Override
    public void complete(String code) {
        Project project = projectRepository.findByProjectCode(code);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);
        taskService.completeByProject(mapper.convert(project, new ProjectDTO()));
    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SimpleKeycloakAccount details = (SimpleKeycloakAccount) authentication.getDetails();
        String username = details.getKeycloakSecurityContext().getToken().getPreferredUsername();//found the username from token

        UserDTO currentUserDTO = userService.findByUserName(username);
        User currentUser = mapper.convert(currentUserDTO, new User());
        List<Project> projectList = projectRepository.findAllByAssignedManager(currentUser);

        return projectList.stream().map(project -> {
            ProjectDTO obj = mapper.convert(project, new ProjectDTO());
            obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
            obj.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));
            return obj;
            }).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> listAllNonCompletedByAssignedManager(UserDTO assignedManager) {
        List<Project> projects = projectRepository.findAllByProjectStatusIsNotAndAssignedManager(
                        Status.COMPLETE,
                        mapper.convert(assignedManager, new User()));
        return projects.stream()
                .map(project -> mapper.convert(project, new ProjectDTO()))
                .collect(Collectors.toList());
    }
}
