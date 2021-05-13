package com.example.hkforum.model;

public class UsernameSingleton {
    private String strUsername;

    private static UsernameSingleton instance;
    private UsernameSingleton(){

    }

    public static UsernameSingleton getInstance(){
        if (instance == null){
            instance = new UsernameSingleton();
        }
        return instance;
    }

    public String getStrUsername(){
        return strUsername;
    }

    public void setStrUsername(String username){
        strUsername = username;
    }
}
