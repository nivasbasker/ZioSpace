package com.zio.ziospace.data;

public class ModelUser {
    String fullname, username, email, phoneno, password, token = "";

    public ModelUser() {
    }

    public ModelUser(String username, String fullname, String email, String phoneno, String password, String token) {
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.phoneno = phoneno;
        this.password = password;
        this.token = token;
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

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
