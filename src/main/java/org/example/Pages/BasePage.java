package org.example.Pages;

import org.example.Elements.BaseElement;
import org.openqa.selenium.By;

public abstract class BasePage {
    private final BaseElement baseElement;

    public BasePage(By locator){
        this.baseElement = new PageWraper(locator);
    }

    public boolean isPageOpen(){
        
    }
}
