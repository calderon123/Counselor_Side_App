package com.example.counselor_side_app.models;

public class UserMentee  {


    private String imageURL;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdMentee() {
        return id;
    }

    public void setIdMentee(String idMentee) {
        this.id = idMentee;
    }

    private String id;
    private String status;
    private String fullname;
    private String email;
    private String password;



    public UserMentee(String fullname, String email,String password,String imageURL) {
        this.fullname = fullname;
        this.email = email;

    }
    public UserMentee(){

    }

    public UserMentee(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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
