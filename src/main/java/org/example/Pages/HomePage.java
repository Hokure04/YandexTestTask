package org.example.Pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class HomePage extends BasePage {

    private final SelenideElement headerLoginButton = $("#header-login-button");

    public HomePage(){
        super($x("//*[@data-test='hero']"), "Home page");
    }

    public void clickLoginButton(){
        headerLoginButton.shouldBe(visible).click();
    }
}
