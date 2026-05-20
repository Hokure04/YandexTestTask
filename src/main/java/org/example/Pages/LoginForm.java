package org.example.Pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class LoginForm extends BasePage{
    private final SelenideElement loginField = $("#passp-field-login");
    private final SelenideElement passwordField = $("#passp-field-passwd");

    private final SelenideElement loginButton = $x("//button[@id='passp:sign-in']");
    private final SelenideElement enterByPasswordButton = $x("//button[contains(@class, 'PasswordButton')]");
    private final SelenideElement getNewCodeButton = $x("//button[@data-t='button:pseudo']");
    private final SelenideElement smsOptionButton = $x("//*[contains(text(), 'смс')]");

    public LoginForm() {
        super($x("//*[@class='passp-auth-content']"), "Login form");
    }

    public void inputLogin(String login) {
        loginField.shouldBe(visible).setValue(login);
    }

    public void inputPassword(String password) {
        passwordField.shouldBe(visible).setValue(password);
    }

    public void clickLoginButton() {
        loginButton.shouldBe(visible).click();
    }

    public void clickEnterByPasswordButton() {
        enterByPasswordButton.shouldBe(visible).click();
    }

    public void clickGetNewCodeButton() {
        getNewCodeButton.shouldBe(visible).click();
    }

    public void clickSmsOptionButton() {
        smsOptionButton.shouldBe(visible).click();
    }
}
