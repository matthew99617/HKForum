package com.example.hkforum.model;

public class User {
    public String firstName, lastName, userName, email, password;

    public User(){

    }

    public User(String firstName, String lastName, String email, String userName, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
    }
}
