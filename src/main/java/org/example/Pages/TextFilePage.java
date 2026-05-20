package org.example.Pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class TextFilePage extends BasePage {

    private final SelenideElement textContent = $x("//*[contains(@class, 'page_text')]");

    public TextFilePage() {
        super($x("//*[contains(@class, 'page_text')]"), "Text file page");
    }

    public String getText() {
        return textContent.shouldBe(visible).getText();
    }
}