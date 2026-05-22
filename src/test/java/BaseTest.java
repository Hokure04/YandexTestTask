import org.example.Utils.Api.YandexDiskApiClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.Selenide.closeWebDriver;

public abstract class BaseTest {
    protected YandexDiskApiClient yandexDiskApiClient;


    @BeforeEach
    public void setUp(){
        yandexDiskApiClient = new YandexDiskApiClient();
    }

    @AfterEach
    public void tearDown(){
        closeWebDriver();
    }
}
