package org.example.Config;

import com.codeborne.selenide.Configuration;
import org.example.Utils.SettingsReader;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

public class BrowserConfig {

    public static void configure(){
        String browser = SettingsReader.getSettings().getBrowser();
        Configuration.browser = browser;
        Configuration.browserSize = "";
        Configuration.timeout = SettingsReader.getSettings().getTimeout();

        switch(browser.toLowerCase()){
            case "chrome" -> configureChrome();
            case "firefox" -> configureFirefox();
            default -> throw new IllegalArgumentException("Не возможно запустить данный браузер: " + browser);
        }
    }

    private static void configureChrome(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        Configuration.browserCapabilities = options;
    }

    private static void configureFirefox(){
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("-private");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        Configuration.browserCapabilities = options;
    }
}
