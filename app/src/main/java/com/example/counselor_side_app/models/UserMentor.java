package com.example.counselor_side_app.models;

public class UserMentor {

    private String id;
    private String status;
    private String imageURL;
    private String fullname;
    private String expertise;
    private String rate;
    private String availability;
    private String email;
    private String date_reg;
    private String address;
    private String phone_number;
    private String fb_mail;
    private String isApproved;

    public UserMentor(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public UserMentor(String address, String phone_number, String fb_mail) {
        this.address = address;
        this.phone_number = phone_number;
        this.fb_mail = fb_mail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getFb_mail() {
        return fb_mail;
    }

    public void setFb_mail(String fb_mail) {
        this.fb_mail = fb_mail;
    }

    public UserMentor(){

    }

    public UserMentor(String id, String status, String imageURL, String fullname, String expertise, String rate, String availability, String email, String date_reg) {
        this.id = id;
        this.status = status;
        this.imageURL = imageURL;
        this.fullname = fullname;
        this.expertise = expertise;
        this.rate = rate;
        this.availability = availability;
        this.email = email;
        this.date_reg = date_reg;
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

    public String getImageUrl() {
        return imageURL;
    }

    public void setImageUrl(String imageUrl) {
        this.imageURL = imageUrl;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate_reg() {
        return date_reg;
    }

    public void setDate_reg(String date_reg) {
        this.date_reg = date_reg;
    }
}
