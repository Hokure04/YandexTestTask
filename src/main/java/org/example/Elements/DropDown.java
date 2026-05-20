package org.example.Elements;

import org.openqa.selenium.By;

public class DropDown extends BaseElement{

    public DropDown(By locator){
        super(locator);
    }

    public void open(){
        findElement().click();
    }
}
