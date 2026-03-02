package com.project.restapi.models;

import java.util.Objects;

public class UpdatePlayerResponse {

    private Long id;
    private Integer age;
    private String gender;
    private String login;
    private String role;
    private String screenName;

    public UpdatePlayerResponse() {}

    public UpdatePlayerResponse(Long id, Integer age, String gender, String login, String role, String screenName) {
        this.id = id;
        this.age = age;
        this.gender = gender;
        this.login = login;
        this.role = role;
        this.screenName = screenName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UpdatePlayerResponse that = (UpdatePlayerResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(age, that.age) && Objects.equals(gender, that.gender) && Objects.equals(login, that.login) && Objects.equals(role, that.role) && Objects.equals(screenName, that.screenName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, gender, login, role, screenName);
    }

    @Override
    public String toString() {
        return "UpdatePlayerResponse{" +
                "id=" + id +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", login='" + login + '\'' +
                ", role='" + role + '\'' +
                ", screenName='" + screenName + '\'' +
                '}';
    }
}
