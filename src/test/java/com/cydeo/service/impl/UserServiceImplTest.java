package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import com.cydeo.exception.TicketingProjectException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.KeycloakService;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private MapperUtil mapper;

    @Mock
    private ProjectService projectService;

    @Mock
    private TaskService taskService;

    @Mock
    private KeycloakServiceImpl keycloakService;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    User user;
    UserDTO userDTO;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("user");
        user.setPassWord("abc1");
        user.setEnabled(true);
        user.setRole(new Role("Manager"));

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setUserName("user");
        userDTO.setPassWord("abc1");
        userDTO.setEnabled(true);

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setDescription("Manager");

        userDTO.setRole(roleDTO);
    }

    private List<User> getUsers(){
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Emily");
        return List.of(user, user2);
    }

    private List<UserDTO> getUserDTOs(){
        UserDTO UserDTO2 = new UserDTO();
        UserDTO2.setId(2L);
        UserDTO2.setFirstName("Emily");
        return List.of(userDTO, UserDTO2);
    }

    private User getUser(String role){
        User user3 = new User();
        user3.setUserName("user3");
        user3.setEnabled(true);
        user3.setIsDeleted(false);
        user3.setRole(new Role(role));

        return user3;
    }

    @Test
    public void should_list_all_users(){
        //given
        //stub
        when(userRepository.findAllByIsDeletedOrderById(false)).thenReturn(getUsers());
        when(mapper.convert(getUsers().get(0), new UserDTO())).thenReturn(userDTO);
        when(mapper.convert(getUsers().get(1),new UserDTO())).thenReturn(getUserDTOs().get(1));
//        when(mapper.convert(any(),any())).thenReturn(userDTO, getUserDTOs().get(1));

        //when
        List<UserDTO> expectedList = getUserDTOs();

        List<UserDTO> actualList = userService.listAllUsers();

        //then
//        assertEquals(expectedList, actualList);
        assertThat(actualList).isEqualTo(expectedList);

        verify(userRepository, times(1)).findAllByIsDeletedOrderById(false);
        verify(mapper, times(2)).convert(any(),any());
    }

    @Test
    public void should_throw_noSuchElementException_when_user_not_found(){
//        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(null); no need to return null since it is mock object
//        assertThrows(RuntimeException.class,()-> userService.findByUserName("some"));
        Throwable exception = assertThrowsExactly(NoSuchElementException.class, () -> userService.findByUserName("someUserName"));

        assertEquals("User Not Exists",exception.getMessage());
    }

    @Test
    public void should_not_throw_exception_when_user_found(){
        when(userRepository.findByUserNameAndIsDeleted(anyString(), anyBoolean())).thenReturn(user);
        when(mapper.convert(any(),any())).thenReturn(userDTO);
        assertDoesNotThrow(() -> userService.findByUserName("someUserName"));
    }

    @Test
    public void should_encode_user_password_on_save_operation(){
        when(mapper.convert(any(),any())).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("Encoded Password");
        when(userRepository.save(any())).thenReturn(user);
        when(mapper.convert(any(),any())).thenReturn(userDTO);


        userService.save(userDTO);

        verify(passwordEncoder).encode(anyString());
    }

    @Test
    public void should_delete_manager_if_no_assigned_project(){

        User manager = getUser("Manager");

        when(userRepository.findByUserNameAndIsDeleted(anyString(),anyBoolean())).thenReturn(manager);
        when(mapper.convert(any(), any())).thenReturn(userDTO);
        when(projectService.listAllNonCompletedByAssignedManager(any())).thenReturn(new ArrayList<>());

        userService.delete(manager.getUserName());

        assertTrue(manager.getIsDeleted());
        assertNotEquals("user3",manager.getUserName());
    }

    @Test
    public void should_delete_employee_if_no_assigned_task(){
        UserDTO userDTO1 = new UserDTO();
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setDescription("Employee");
        userDTO1.setRole(roleDTO);
        User employee = getUser("Employee");

        when(userRepository.findByUserNameAndIsDeleted(anyString(),anyBoolean())).thenReturn(employee);
        when(mapper.convert(any(), any())).thenReturn(userDTO1);
        when(taskService.listAllNonCompletedByAssignedEmployee(any())).thenReturn(new ArrayList<>());

        userService.delete(employee.getUserName());

        assertTrue(employee.getIsDeleted());
        assertNotEquals("user3", employee.getUserName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Manager", "Employee"})
    public void should_delete_user_if_no_assigned(String role){
        UserDTO userDTO1 = new UserDTO();
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setDescription("Employee");
        userDTO1.setRole(roleDTO);

        User user1 = getUser(role);

        when(userRepository.findByUserNameAndIsDeleted(anyString(),anyBoolean())).thenReturn(user1);
        lenient().when(mapper.convert(any(), any())).thenReturn(userDTO);
        lenient().when(mapper.convert(any(), any())).thenReturn(userDTO1);
        when(userRepository.save(any())).thenReturn(user1);

        lenient().when(taskService.listAllNonCompletedByAssignedEmployee(any())).thenReturn(new ArrayList<>());
        lenient().when(projectService.listAllNonCompletedByAssignedManager(any())).thenReturn(new ArrayList<>());

        userService.delete(user1.getUserName());

        assertTrue(user1.getIsDeleted());
        assertNotEquals("user3", user1.getUserName());
    }

    @Test
    public void should_throw_exception_when_deleting_manager_with_assigned_projects(){
        User manager = getUser("Manager");

        when(userRepository.findByUserNameAndIsDeleted(anyString(),anyBoolean())).thenReturn(manager);
        when(mapper.convert(any(), any())).thenReturn(userDTO);
        when(projectService.listAllNonCompletedByAssignedManager(userDTO)).thenReturn(List.of(new ProjectDTO()));

        Throwable throwable = catchThrowable(() -> userService.delete(manager.getUserName()));

        assertEquals(TicketingProjectException.class, throwable.getClass());
        assertEquals("User has assigned roles/projects. Cannot be deleted!", throwable.getMessage());
    }

    @Test
    public void should_throw_exception_when_deleting_employee_with_assigned_tasks(){
        User employee = getUser("Employee");

        UserDTO userDTO1 = new UserDTO();
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setDescription("Employee");
        userDTO1.setRole(roleDTO);

        when(userRepository.findByUserNameAndIsDeleted(anyString(),anyBoolean())).thenReturn(employee);
        when(mapper.convert(any(), any())).thenReturn(userDTO1);
        when(taskService.listAllNonCompletedByAssignedEmployee(userDTO1)).thenReturn(List.of(new TaskDTO()));

        Throwable throwable = catchThrowable(() -> userService.delete(employee.getUserName()));

        assertEquals(TicketingProjectException.class, throwable.getClass());
        assertEquals("User has assigned roles/projects. Cannot be deleted!", throwable.getMessage());
    }

}