package org.example.TestData;

public class UserData {
    public String login;
    public String password;
    public String token;

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
