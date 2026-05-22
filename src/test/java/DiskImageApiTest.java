import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.example.Models.TestData.TestData;
import org.example.Utils.CompareImagesUtil;
import org.example.Utils.PathManager;
import org.example.Utils.Readers.TestDataReader;
import org.example.Utils.TextGeneratorUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Epic("Yandex Disk API")
@Feature("Изображения")
public class DiskImageApiTest extends BaseTest {

    @Test
    @Story("Загрузка изображения")
    @Description("Загрузка картики на диск и сравнения с локальным файлом")
    public void uploadImageTest() throws IOException {
        TestData testData = TestDataReader.getTestData();

        String imageDiskPath = testData.getImageApi().getDiskImagePath();
        byte[] expectedImageBytes = Files.readAllBytes(PathManager.getPath(testData.getImageApi().getLocalImagePath()));

        step("Загружаем какртинку на Диск", () -> {
            Response uploadImageResponse = yandexDiskApiClient.uploadImageFile(imageDiskPath, expectedImageBytes);
            assertEquals(201, uploadImageResponse.statusCode(), "Не удалось загрузить картинку на Диск");
        });

        step("Проверяем, что картинка появилась на Диске", () -> {
            Response getImageResponse = yandexDiskApiClient.getResource(imageDiskPath);
            assertEquals(200, getImageResponse.statusCode(), "Картинка не найдена на Диске");
        });

        byte[] actualImageBytes = step("Скачиваем картинку с Диска", () ->
                yandexDiskApiClient.downloadFileBytes(imageDiskPath)
        );

        step("Проверяем, что загруженная картинка совпадает с локальной", () ->{
            boolean areImagesEqual = CompareImagesUtil.areImagesEqual(actualImageBytes, expectedImageBytes);
            assertTrue(areImagesEqual, "Картинка на Диске не совпадает с локальной картинкой");
        });

        step("Удаляем картинку с Диска", () -> {
            Response deleteImageResponse = yandexDiskApiClient.deleteFile(imageDiskPath);
            assertEquals(204, deleteImageResponse.statusCode(), "Не удалось удалить картинку с Диска");
        });
    }


    @Test
    public void changeFileExtensionTest(){
        TestData testData = TestDataReader.getTestData();

        String filePath = testData.getImageApi().getTextFilePath();
        String changedFilePath = filePath.substring(0, filePath.lastIndexOf(".")) + testData.getImageApi().getNewExtension();
        String expectedFileName = testData.getImageApi().getDefaultFilename()+testData.getImageApi().getNewExtension();

        step("Загружаем текстовый файл на Диск", () -> {
            String expectedText = TextGeneratorUtil.generateText(100);
            Response uploadFileResponse = yandexDiskApiClient.uploadTextFile(filePath, expectedText);
            assertEquals(201, uploadFileResponse.statusCode(), "Не удалось загрузить текстовый файл");
        });

        step("Проверяем, что текстовый файл загружен", () -> {
            Response getTxtFileResponse = yandexDiskApiClient.getResource(filePath);
            assertEquals(200, getTxtFileResponse.statusCode(), "Текстовый файл не найден на Диске");
            assertEquals("new_file.txt", getTxtFileResponse.jsonPath().getString("name"), "Имя файла не совпадает с ожидаемым");
        });

        step("Меняем расширение файла на jpg", () -> {
            Response changeExtensionResponse = yandexDiskApiClient.changeExtensions(filePath, testData.getImageApi().getNewExtension());
            assertEquals(201, changeExtensionResponse.statusCode(), "Не удалось изменить расширение файла");
        });

        step("Проверяем, что текстового файла с таким именем больше нет на диске", () -> {
            Response getOldFileResponse = yandexDiskApiClient.getResource(filePath);
            assertEquals(404, getOldFileResponse.statusCode(), "Текстовый файл все ещё существует");
        });

        step("Проверяем что имя файла изменилось", () -> {
            Response getChangedFileResponse = yandexDiskApiClient.getResource(changedFilePath);
            assertEquals(200, getChangedFileResponse.statusCode(), "Файл с новым расширением не найден");
            assertEquals(expectedFileName, getChangedFileResponse.jsonPath().getString("name"), "Имя файла не совпадает с ожидаемым");
        });

        step("Удаляем файл", () -> {
            Response deleteFileResponse = yandexDiskApiClient.deleteFile(changedFilePath);
            assertEquals(204, deleteFileResponse.statusCode(), "Не удалось удалить файл с новым расширением");
        });
    }
}
