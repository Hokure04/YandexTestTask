import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.example.Models.TestData.TestData;
import org.example.Utils.RandomUtil;
import org.example.Utils.Readers.TestDataReader;
import org.example.Utils.TextGeneratorUtil;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiskAsyncOperationApiTest extends BaseTest{

    @Test
    @Story("Получение статуса асинхронной операции")
    @Description("Проверка получения статуса асинхронной операции после удаления папки")
    public void getOperationStatusTest() {
        TestData testData = TestDataReader.getTestData();

        int min = 1000;
        int max = 10000;

        String folderPath = String.format(testData.getFileApi().getTestFolder(), RandomUtil.getRandomNumberAsString(min, max));
        String filePath = folderPath + "/" + testData.getFileApi().getFileName();
        String expectedText = TextGeneratorUtil.generateText(20);

        step("Создаем папку", () -> {
            Response response = yandexDiskApiClient.createFolder(folderPath);
            assertEquals(201, response.statusCode(), "Не удалось создать папку");
        });

        step("Загружаем файл в папку", () -> {
            Response response = yandexDiskApiClient.uploadTextFile(filePath, expectedText);
            assertEquals(201, response.statusCode(), "Не удалось загрузить файл");
        });

        Response deleteFolderResponse = step("Удаляем папку", () ->
                yandexDiskApiClient.deleteFile(folderPath)
        );

        step("Проверяем, что удаление запущено как асинхронная операция", () ->
                assertEquals(202, deleteFolderResponse.statusCode(), "Удаление не было запущено как асинхронная операция")
        );

        String operationHref = deleteFolderResponse.jsonPath().getString("href");
        String operationId = operationHref.substring(operationHref.lastIndexOf("/") + 1);

        Response operationStatusResponse = step("Получаем статус асинхронной операции", () ->
                yandexDiskApiClient.getOperationStatus(operationId)
        );

        step("Проверяем, что статус операции получен", () ->
                assertEquals(200, operationStatusResponse.statusCode(), "Не удалось получить статус операции")
        );
    }
}
