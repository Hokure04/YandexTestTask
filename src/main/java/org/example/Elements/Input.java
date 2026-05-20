package org.example.Elements;

import org.openqa.selenium.By;

public class Input extends BaseElement{

    public Input(By locator){
        super(locator);
    }


    public void inputText(String text){
        findElement().sendKeys(text);
    }

    public String getText(){
        return findElement().getDomAttribute("value");
    }
}
