package com.cydeo.service.impl;

import com.cydeo.annotation.DefaultExceptionMessage;
import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.KeycloakService;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapper;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final KeycloakService keycloakService;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapper, @Lazy ProjectService projectService, @Lazy TaskService taskService, KeycloakServiceImpl keycloakService) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.projectService = projectService;
        this.taskService = taskService;
        this.keycloakService = keycloakService;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        List<User> userList = userRepository.findAllByIsDeletedOrderByFirstNameDesc(false);
        return userList.stream()
                .map(user -> mapper.convert(user, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        User user = userRepository.findByUserNameAndIsDeleted(username, false);
        return mapper.convert(user, new UserDTO());
    }

    @Override
    public void save(UserDTO user) {
        User userEntity = mapper.convert(user, new User());
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        keycloakService.userCreate(user);
    }

    @Override
    public UserDTO update(UserDTO user) {
        User foundUser = userRepository.findByUserNameAndIsDeleted(user.getUserName(), false);
        User userToUpdate = mapper.convert(user, new User());
        userToUpdate.setId(foundUser.getId());
        userRepository.save(userToUpdate);
        return findByUserName(user.getUserName());
    }

    @Override
    @DefaultExceptionMessage(defaultMessage = "Failed to delete user!")
    public void delete(String username) {
        User foundUser = userRepository.findByUserNameAndIsDeleted(username, false);
        if (checkIfUserCanBeDeleted(foundUser)) {
            foundUser.setIsDeleted(true);
            foundUser.setUserName(foundUser.getUserName() + "-" + foundUser.getId());
            userRepository.save(foundUser);
            keycloakService.delete(username);
        }else throw new TicketingProjectException("User cannot be deleted!");
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        List<User> users = userRepository.findByRoleDescriptionIgnoreCaseAndIsDeleted(role, false);
        return users.stream()
                .map(user -> mapper.convert(user, new UserDTO()))
                .collect(Collectors.toList());
    }

    private boolean checkIfUserCanBeDeleted(User user) {
        UserDTO convertedUser = mapper.convert(user, new UserDTO());
        switch (convertedUser.getRole().getDescription()) {
            case "Manager":
                List<ProjectDTO> projectDTOList = projectService.listAllNonCompletedByAssignedManager(convertedUser);
                return projectDTOList.size() == 0; //if there is any project assigned, manager cannot be deleted
            case "Employee":
                List<TaskDTO> taskDTOList = taskService.listAllNonCompletedByAssignedEmployee(convertedUser);
                return taskDTOList.size() == 0; //if there is any task assigned, employee cannot be deleted
            default:
                return true;
        }
    }
}
