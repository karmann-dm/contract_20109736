package com.karmanno.verificator.model;

public class User {
    private String username;
    private String password;
    private UserStatus userStatus;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.userStatus = UserStatus.UNVERIFIED;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public String toString() {
        return getUsername() + ":" + getPassword();
    }
}
