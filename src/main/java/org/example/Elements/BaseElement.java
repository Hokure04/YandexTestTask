package org.example.Elements;

import org.example.Driver.DriverManager;
import org.example.Utils.WaitUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public abstract class BaseElement {

    private By locator;

    public BaseElement(By locator) {
        this.locator = locator;
    }

    public WebElement findElement() {
        WaitUtil.waitUntilDisplayed(locator);
        return DriverManager.getDriver().findElement(locator);
    }

    public boolean isDisplayed(){
        WaitUtil.waitUntilDisplayed(locator);
        return findElement().isDisplayed();
    }
}
