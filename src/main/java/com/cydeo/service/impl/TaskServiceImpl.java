package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final MapperUtil mapper;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserService userService, MapperUtil mapper) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        List<Task> taskList = taskRepository.findAll(Sort.by("taskSubject"));
        return taskList.stream()
                .map(task -> mapper.convert(task, new TaskDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(TaskDTO dto) {
        dto.setTaskStatus(Status.OPEN);
        dto.setAssignedDate(LocalDate.now());
        Task task = mapper.convert(dto, new Task());
        taskRepository.save(task);
    }

    @Override
    public void update(TaskDTO dto) {
        Optional<Task> task = taskRepository.findById(dto.getId());
        Task convertedTask  = mapper.convert(task, new Task());
        if(task.isPresent()){
            convertedTask.setTaskStatus(dto.getTaskStatus() == null ? task.get().getTaskStatus() : dto.getTaskStatus());
            convertedTask.setAssignedDate(task.get().getAssignedDate());
            taskRepository.save(convertedTask);
        } else throw new TicketingProjectException("Task not found with id: "+dto.getId());
    }

    @Override
    public void delete(Long id) {
        Optional<Task> foundTask = taskRepository.findById(id);
        if(foundTask.isPresent()){
            foundTask.get().setIsDeleted(true);
            taskRepository.save(foundTask.get());
        }
    }

    @Override
    public TaskDTO findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()){
            return mapper.convert(task, new TaskDTO());
        } else throw new TicketingProjectException("Task not found with id: "+id);
    }

    @Override
    public int totalNonCompletedTask(String projectCode) {
        return taskRepository.totalNonCompletedTasks(projectCode);
    }

    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }

    @Override
    public void deleteByProject(ProjectDTO projectDTO) {
        Project projectToDelete = mapper.convert(projectDTO, new Project());
        List<Task> tasks = taskRepository.findAllByProject(projectToDelete); //related tasks deleted as well as project deleted
        tasks.forEach(task -> delete(task.getId()));
    }
    //CascadeType.DELETE can be used instead of this method if we apply hard delete

    @Override
    public void completeByProject(ProjectDTO projectDTO) {
        Project project = mapper.convert(projectDTO, new Project());
        List<Task> tasks = taskRepository.findAllByProject(project);
        tasks.stream()
                .map(task -> mapper.convert(task, new TaskDTO()))//related tasks completed as well as project completed
                .forEach(taskDTO -> {
                    taskDTO.setTaskStatus(Status.COMPLETE);
                    update(taskDTO);
        });
    }

    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SimpleKeycloakAccount details = (SimpleKeycloakAccount) authentication.getDetails();
        String username = details.getKeycloakSecurityContext().getToken().getPreferredUsername();

        UserDTO loggedInUser = userService.findByUserName(username);
        List<Task> tasks = taskRepository
                .findAllByTaskStatusIsNotAndAssignedEmployee
                        (status, mapper.convert(loggedInUser, new User()));

        return tasks.stream()
                .map(task -> mapper.convert(task, new TaskDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByStatus(Status status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SimpleKeycloakAccount details = (SimpleKeycloakAccount) authentication.getDetails();
        String username = details.getKeycloakSecurityContext().getToken().getPreferredUsername();

        UserDTO loggedInUser = userService.findByUserName(username);
        List<Task> tasks = taskRepository.
                findAllByTaskStatusAndAssignedEmployee
                        (status, mapper.convert(loggedInUser, new User()));

        return tasks.stream()
                .map(task -> mapper.convert(task, new TaskDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllNonCompletedByAssignedEmployee(UserDTO assignedEmployee) {
        List<Task> tasks = taskRepository
                .findAllByTaskStatusIsNotAndAssignedEmployee
                        (Status.COMPLETE, mapper.convert(assignedEmployee, new User()));
        return tasks.stream()
                .map(task -> mapper.convert(task, new TaskDTO()))
                .collect(Collectors.toList());
    }

}
