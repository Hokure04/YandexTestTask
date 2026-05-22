import io.qameta.allure.Description;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.example.Models.TestData.TestData;
import org.example.Utils.Readers.TestDataReader;
import org.example.Utils.TextGeneratorUtil;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiskFileListApiTest extends BaseTest{

    @Test
    @Story("Получение списка файлов по имени")
    @Description("Проверка, что после получения списка упорядоченных имен загруженный файл находится первым")
    public void getFilesOrderedByNameTest() {
        TestData testData = TestDataReader.getTestData();

        String filePath = testData.getFileListApi().getSortedFilePath();
        String expectedFileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String expectedText = TextGeneratorUtil.generateText(100);

        step("Загружаем файл на Диск", () -> {
            Response uploadFileResponse = yandexDiskApiClient.uploadTextFile(filePath, expectedText);
            assertEquals(201, uploadFileResponse.statusCode(), "Не удалось загрузить файл");
        });

        Response filesResponse = step("Получаем список файлов, упорядоченный по имени", () ->
                yandexDiskApiClient.getFilesOrderedByName()
        );

        step("Проверяем, что список файлов получен", () ->
                assertEquals(200, filesResponse.statusCode(), "Не удалось получить список файлов")
        );

        String firstFileName = filesResponse.jsonPath().getString("items[0].name");

        step("Проверяем, что файл aaa.txt находится первым в списке", () ->
                assertEquals(expectedFileName, firstFileName, "Первый файл в списке не совпадает с ожидаемым")
        );

        step("Удаляем созданный файл", () -> {
            Response deleteFileResponse = yandexDiskApiClient.deleteFile(filePath);
            assertEquals(204, deleteFileResponse.statusCode(), "Не удалось удалить файл");
        });
    }

    @Test
    @Story("Получение списка последних загруженных файлов")
    @Description("Проверка, что после загрузки файла он является последним загруженным файлом, а дата загрузки совпадает с датой файла")
    public void getLastUploadedFilesTest() {
        TestData testData = TestDataReader.getTestData();

        String filePath = testData.getFileListApi().getLastUploadedFilePath();
        String expectedFileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String expectedText = TextGeneratorUtil.generateText(100);

        step("Загружаем файл на Диск", () -> {
            Response uploadFileResponse = yandexDiskApiClient.uploadTextFile(filePath, expectedText);
            assertEquals(201, uploadFileResponse.statusCode(), "Не удалось загрузить файл");
        });

        Response getFileResponse = step("Получаем информацию о загруженном файле", () ->
                yandexDiskApiClient.getResource(filePath)
        );

        String expectedCreatedDate = getFileResponse.jsonPath().getString("created");
        Response lastUploadedResponse = step("Получаем список последних загруженных файлов", () ->
                yandexDiskApiClient.getLastUploadedFiles()
        );

        step("Проверяем, что список получен успешно", () ->
                assertEquals(200, lastUploadedResponse.statusCode(), "Не удалось получить список файлов")
        );

        String actualFileName = lastUploadedResponse.jsonPath().getString("items[0].name");
        String actualCreatedDate = lastUploadedResponse.jsonPath().getString("items[0].created");

        step("Проверяем, что загруженный файл находится первым в списке", () ->
                assertEquals(expectedFileName, actualFileName, "Файл не является последним загруженным")
        );

        step("Проверяем, что дата загрузки совпадает", () ->
                assertEquals(expectedCreatedDate, actualCreatedDate, "Дата загрузки файла не совпадает с ожидаемой")
        );

        step("Удаляем созданный файл", () -> {
            Response deleteFileResponse = yandexDiskApiClient.deleteFile(filePath);
            assertEquals(204, deleteFileResponse.statusCode(), "Не удалось удалить файл");
        });
    }
}
