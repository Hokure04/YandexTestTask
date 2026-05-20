package org.example.Pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;

public abstract class BasePage {
    private final SelenideElement pageLocator;
    private final String pageName;

    protected BasePage(SelenideElement pageLocator, String pageName){
        this.pageLocator = pageLocator;
        this.pageName = pageName;
    }

    public void waitForaPageOpened(){
        pageLocator.shouldBe(visible, Duration.ofSeconds(20));
    }

    public boolean isPageOpened(){
        return pageLocator.isDisplayed();
    }

    public String getPageName(){
        return pageName;
    }
}
