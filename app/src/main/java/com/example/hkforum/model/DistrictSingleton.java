package com.example.hkforum.model;

public class DistrictSingleton {
    private String strDistrict;

    private static DistrictSingleton instance;
    private DistrictSingleton(){

    }

    public static DistrictSingleton getInstance(){
        if (instance == null){
            instance = new DistrictSingleton();
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
