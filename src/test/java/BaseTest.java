import org.example.Config.BrowserConfig;
import org.example.Models.Settings;
import org.example.Utils.SettingsReader;
import org.example.Utils.YandexDiskApiClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

public abstract class BaseTest {
    protected YandexDiskApiClient yandexDiskApiClient;

    @BeforeEach
    public void setUp(){
        BrowserConfig.configure();
        yandexDiskApiClient = new YandexDiskApiClient();
        open(SettingsReader.getSettings().getAppUrl());
    }

    @AfterEach
    public void tearDown(){
        closeWebDriver();
    }
}
