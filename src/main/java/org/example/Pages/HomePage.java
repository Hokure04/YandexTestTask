package org.example.Pages;

import org.example.Elements.Button;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    private final Button headerLoginButton = new Button(By.xpath("//*[@id = 'header-login-button']"));

    public HomePage(){
        super(By.xpath("//*[@data-test='hero']"));
    }

    public void clickLoginButton(){
        headerLoginButton.click();
    }
}
