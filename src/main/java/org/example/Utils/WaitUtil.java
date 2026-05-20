package org.example.Utils;

import org.example.Driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtil {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    public static boolean waitUntilDisplayed(By locator){
        try{
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), TIMEOUT);
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        }catch (TimeoutException ex){
            return false;
        }
    }

    public static void waitUntilClickable(By locator){
        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), TIMEOUT);
        wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
}
