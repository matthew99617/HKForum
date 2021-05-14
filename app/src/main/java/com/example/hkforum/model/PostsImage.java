package com.example.hkforum.model;

public class PostsImage {

    public String District, Username, Title, Text, imageUrl;

    public PostsImage(){

    }

    public PostsImage(String District, String Username , String Title, String Text, String imageUrl){
        this.District = District;
        this.Username = Username;
        this.Title = Title;
        this.Text = Text;
        this.imageUrl = imageUrl;
    }
    public String getShowUsername() {
        return Username;
    }

    public String getShowTitle() {
        return Title;
    }

    public String getShowText() {
        return Text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
