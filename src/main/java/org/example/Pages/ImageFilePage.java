package org.example.Pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class ImageFilePage extends BasePage {

    private final SelenideElement closeButton = $x("//button[contains(@class, 'slider__button_close')]");

    public ImageFilePage() {
        super($x("//*[@class='slider__items']"), "Image file page");
    }

    public void closeImage() {
        closeButton.shouldBe(visible).click();
    }
}
