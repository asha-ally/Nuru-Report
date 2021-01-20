package com.openinstitute.nuru.Database;

public class User {
    private int userId;
    private String  user_email;
    private String user_name;
    private String user_profile;
    private String user_number;
    private String user_password;
    private  String user_dp;
    private  String user_date;
    private  String user_conf_pass;

    public User(int userId, String user_email, String user_name, String user_profile, String user_number, String user_password,String user_conf_pass) {
        this.userId = userId;
        this.user_email = user_email;
        this.user_name = user_name;
        this.user_profile = user_profile;
        this.user_number = user_number;
        this.user_password = user_password;
        this.user_dp = user_dp;
        this.user_date =  user_date;
        this.user_conf_pass=  user_conf_pass;
    }

    public User() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public String getUser_number() {
        return user_number;
    }

    public void setUser_number(String user_number) {
        this.user_number = user_number;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_dp() {
        return user_dp;
    }

    public void setUser_dp(String user_dp) {
        this.user_dp = user_dp;
    }

    public String getUser_date() {
        return user_date;
    }

    public void setUser_date(String user_date) {
        this.user_date = user_date;
    }

    public String getUser_conf_pass() {
        return user_conf_pass;
    }

    public void setUser_conf_pass(String user_conf_pass) {
        this.user_conf_pass = user_conf_pass;
    }
}
