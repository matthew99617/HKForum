package com.example.hkforum.model;

public class Comment {
    public String Title, Comment, Username;

    public Comment(){

    }

    public Comment(String Title, String comment, String username) {
        this.Title = Title;
        this.Comment = comment;
        this.Username = username;
    }

    public String getShowTitle(){
        return Title;
    }

    public String getShowComment() {
        return Comment;
    }

    public String getShowUsername() {
        return Username;
    }
}
