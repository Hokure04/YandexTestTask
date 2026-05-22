package org.example.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserData {

    @JsonProperty("token")
    private String token;

    public String getToken(){
        return this.token;
    }
}
