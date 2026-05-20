package org.example.Pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

public class DiskPage extends BasePage {

    private final SelenideElement sortButton = $x("//*[contains(@class, 'listing-sort')]//button[contains(@class, 'Button2_size_m')]");
    private final SelenideElement dateUpdateOption = $x("//*[@class='Menu-Text' and contains(text(), 'Дате изменения')]");
    private final SelenideElement typeOption = $x("//*[@class='Menu-Text' and contains(text(), 'Типу')]");
    private final SelenideElement trashBin = $x("//*[@id='/trash']");
    private final SelenideElement notification = $x("//*[contains(@class, 'notification_shown')]");

    public DiskPage() {
        super($x("//*[@class='root__content-container']"), "Disk page");
    }

    private SelenideElement fileByName(String filename) {
        return $x("//*[contains(@class,'listing-item__title') and contains(@aria-label,'" + filename + "')]");
    }

    public boolean isFilePresent(String filename) {
        return fileByName(filename)
                .shouldBe(Condition.visible, Duration.ofSeconds(20))
                .exists();
    }

    public boolean isFileAbsent(String filename) {
        return fileByName(filename)
                .shouldNotBe(Condition.visible, Duration.ofSeconds(20))
                .exists();
    }

    public void openFile(String filename) {
        fileByName(filename)
                .shouldBe(Condition.visible)
                .doubleClick();
    }

    public String getActualFileName(String filename) {
        return fileByName(filename)
                .shouldBe(Condition.visible, Duration.ofSeconds(20))
                .getText();
    }

    public void sortFiles() {
        sortButton.shouldBe(Condition.visible).click();
        dateUpdateOption.shouldBe(Condition.visible).click();

        sortButton.shouldBe(Condition.visible).click();
        typeOption.shouldBe(Condition.visible).click();
    }

    public void moveFileToTrash(String filename) {
        SelenideElement file = fileByName(filename).shouldBe(Condition.visible);

        new Actions(getWebDriver())
                .dragAndDrop(file, trashBin.shouldBe(Condition.visible))
                .perform();
    }

    public void waitNotificationDisappear() {
        notification.shouldNotBe(Condition.visible, Duration.ofSeconds(20));
    }
}