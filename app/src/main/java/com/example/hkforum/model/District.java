package com.example.hkforum.model;

public class District {
    private String strDistrict;

    private static District instance;
    private District(){

    }

    public static District getInstance(){
        if (instance == null){
            instance = new District();
        }
        return instance;
    }

    public String getStrDistrict(){
        return strDistrict;
    }

    public void setStrDistrict(String district){
        strDistrict = district;
    }
}
