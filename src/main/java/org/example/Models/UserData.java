package org.example.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserData {
    @JsonProperty("login")
    private String login;

    @JsonProperty("password")
    private String password;

    @JsonProperty("token")
    private String token;

    public void setLogin(String login){
        this.login = login;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getLogin(){
        return this.login;
    }

    public String getPassword(){
        return this.password;
    }

    public String getToken(){
        return this.token;
    }
}
