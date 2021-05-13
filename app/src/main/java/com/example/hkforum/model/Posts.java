package com.example.hkforum.model;

public class Posts {

    public String strDistrict, strUsername, strTitle, strContent;

    public Posts(){

    }

    public Posts(String strDistrict, String strUsername , String strTitle, String strContent){
        this.strDistrict = strDistrict;
        this.strUsername = strUsername;
        this.strTitle = strTitle;
        this.strContent = strContent;
    }
    public String getStrDistrict() {
        return strDistrict;
    }

    public void setStrDistrict(String strDistrict) {
        this.strDistrict = strDistrict;
    }

    public String getStrUsername() {
        return strUsername;
    }

    public void setStrUsername(String strUsername) {
        this.strUsername = strUsername;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public void setStrTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public String getStrContent() {
        return strContent;
    }

    public void setStrContent(String strContent) {
        this.strContent = strContent;
    }
}
