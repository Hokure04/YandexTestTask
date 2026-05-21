package org.example.Utils;

import com.codeborne.selenide.Selenide;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public final class TabUtil {

    public static String switchToLastTab() {
        String originalTab = getWebDriver().getWindowHandle();

        Set<String> handles = getWebDriver().getWindowHandles();
        List<String> tabs = new ArrayList<>(handles);

        getWebDriver().switchTo().window(tabs.get(tabs.size() - 1));

        return originalTab;
    }

    public static void closeCurrentTabAndReturn(String originalTab) {
        Selenide.closeWindow();
        getWebDriver().switchTo().window(originalTab);
    }
}