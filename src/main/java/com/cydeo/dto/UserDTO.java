package com.cydeo.dto;

import com.cydeo.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotBlank(message = "First name is required")
    @Size(max = 15, min = 2, message = "First name length should be min 2, max 15")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 15, min = 2, message = "Last name length should be min 2, max 15")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Email(message = "Username should be proper E-mail format")
    private String userName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{4,}", message = "Password must be at least 4 characters long and include at least one digit, one lowercase letter, and one uppercase letter.")
    private String passWord;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    @NotBlank(message = "Password is required")
    private String confirmPassWord;

    private boolean enabled;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\d{10}$", message = "Phone should be 10-digit number")
    private String phone;

    @NotNull(message = "Role is required")
    private RoleDTO role;

    @NotNull(message = "Gender is required")
    private Gender gender;

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
        checkConfirmPassword();
    }

    public String getConfirmPassWord() {
        return confirmPassWord;
    }

    public void setConfirmPassWord(String confirmPassWord) {
        this.confirmPassWord = confirmPassWord;
        checkConfirmPassword();
    }

    private void checkConfirmPassword() {
        if (this.passWord == null || this.confirmPassWord == null) {
            return;
        } else if (!this.passWord.equals(this.confirmPassWord)) {
            this.confirmPassWord = null;
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

}
