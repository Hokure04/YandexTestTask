package org.example.Driver;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverManager {
    public static WebDriver driver;

    public static WebDriver getDriver(){
        if(driver == null){
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--incognito");
            options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
        }
        return driver;
    }

    public static void closeDriver(){
        if (driver != null){
            driver.quit();
            driver = null;
        }
    }
}
