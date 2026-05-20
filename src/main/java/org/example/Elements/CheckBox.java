package org.example.Elements;

import org.openqa.selenium.By;

public class CheckBox extends BaseElement{

    public CheckBox(By locator){
        super(locator);
    }

    public boolean isSelected(){
        return findElement().isSelected();
    }

    public void check(){
        findElement().click();
    }


}
