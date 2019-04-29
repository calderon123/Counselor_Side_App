package com.example.counselor_side_app.models;

public class UserMentee  {


    private String imageURL;
    private String id;
    private String status;
    private String fullname;
    private String email;
    private String password;



    public UserMentee(){

    }

    public UserMentee(String imageURL, String id, String status, String fullname, String email, String password) {
        this.imageURL = imageURL;
        this.id = id;
        this.status = status;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
