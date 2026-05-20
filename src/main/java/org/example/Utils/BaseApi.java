package org.example.Utils;


import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public abstract class BaseApi {

    protected BaseApi(String baseUrl){
        RestAssured.baseURI = baseUrl;
    }

    protected RequestSpecification request(){
        return RestAssured.given();
    }
}
