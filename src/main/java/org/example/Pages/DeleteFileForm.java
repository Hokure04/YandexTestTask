package org.example.Pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class DeleteFileForm extends BasePage {

    private final SelenideElement deleteButton =
            $x("//button[contains(@class,'confirmation-dialog__button_submit')]");

    public DeleteFileForm() {
        super($x("//*[@class='dialog__wrap']"), "Delete file form");
    }

    public void acceptDelete() {
        deleteButton.shouldBe(visible).click();
    }
}