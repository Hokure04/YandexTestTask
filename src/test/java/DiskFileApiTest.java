import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.example.Models.TestData;
import org.example.Utils.RandomUtil;
import org.example.Utils.TestDataReader;
import org.example.Utils.TextGeneratorUtil;
import org.example.Utils.WaitUtil;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Epic("Yandex Disk API")
@Feature("Файлы")
public class DiskFileApiTest extends BaseTest {

    @Test
    @Story("Переименование файла внутри папки")
    @Description("Создание папки и файла внутри, переименование файла, удаление папки с файлом")
    public void yandexDiskApiSimpleTest() {
        TestData testData = TestDataReader.getTestData();
        int min = 1000;
        int max = 10000;
        String folderPath = String.format(testData.getApiTestFolder(), RandomUtil.getRandomNumberAsString(min, max));
        String filePath = folderPath + "/" + testData.getApiTestFileName();
        String renamedFilePath = folderPath + "/" + testData.getApiTestRenamedFileName();


        step("Создаем папку", () -> {
            Response createFolderResponse = yandexDiskApiClient.createFolder(folderPath);
            assertEquals(201, createFolderResponse.statusCode(), "Не удалось создать папку");
        });


        step("Проверяем, что папка создана", () -> {
            Response getFolderResponse = yandexDiskApiClient.getResource(folderPath);
            assertEquals(200, getFolderResponse.statusCode(), "Созданная папка не найдена");
            assertEquals("dir", getFolderResponse.jsonPath().getString("type"), "Ресурс не является папкой");
            assertEquals(folderPath, getFolderResponse.jsonPath().getString("name"), "Название папки не совпадает с ожидаемым");
        });


        step("Загружаем текстовый файл", () -> {
            String expectedText = TextGeneratorUtil.generateText(20);
            Response uploadFileResponse = yandexDiskApiClient.uploadTextFile(filePath, expectedText);
            assertEquals(201, uploadFileResponse.statusCode(), "Не удалось загрузить файл");
        });

        step("Проверяем, что файл находится на Диске", () -> {
            Response getFileResponse = yandexDiskApiClient.getResource(filePath);
            assertEquals(200, getFileResponse.statusCode(), "Файл не найден");
            assertEquals("file", getFileResponse.jsonPath().getString("type"), "Ресурс не является файлом");
            assertEquals(testData.getApiTestFileName(), getFileResponse.jsonPath().getString("name"), "Имя файла не совпадает с ожидаемым");
        });


        step("Переименовываем файл", () -> {
            Response moveFileResponse = yandexDiskApiClient.moveResource(filePath, renamedFilePath);
            assertEquals(201, moveFileResponse.statusCode(), "Не удалось переименовать файл");
        });


        step("Проверяем файл по новому имени", () -> {
            Response getRenamedFileResponse = yandexDiskApiClient.getResource(renamedFilePath);
            assertEquals(200, getRenamedFileResponse.statusCode(), "Переименованный файл не найден");
            assertEquals(testData.getApiTestRenamedFileName(), getRenamedFileResponse.jsonPath().getString("name"), "Имя файла не сопадает с ожидаемым");
        });


        step("Удаляем папку со всем содержимым", () -> {
            Response deleteFolderResponse = yandexDiskApiClient.deleteFile(folderPath);
            assertEquals(202, deleteFolderResponse.statusCode(), "Не удалось удалить папку");
        });


        step("Проверяем, что папка удалена", () -> {
            Response getDeletedFolderResponse = yandexDiskApiClient.getResource(folderPath);
            assertEquals(404, getDeletedFolderResponse.statusCode(), "Папка всё ещё существует");
        });
    }

    @Test
    @Story("Изменение размера диска")
    @Description("Проверка, изменения занятого места на диске после загрузки файла")
    public void diskSpaceTest(){
        TestData testData = TestDataReader.getTestData();

        int min = 1000;
        int max = 10000;
        String filePath = String.format(testData.getApiTestFileName(),RandomUtil.getRandomNumberAsString(min, max));

        Response diskInfoBeforeResponse = step("Получаем информацию о месте на Диске", () ->
             yandexDiskApiClient.getDiskInfo()
        );

        step("Проверяем, что информация о Диске получена", () ->
                assertEquals(200, diskInfoBeforeResponse.statusCode(), "Не удалось получить информацию о Диске")
        );

        long usedSpaceBefore = diskInfoBeforeResponse.jsonPath().getLong("used_space");
        long totalSpaceBefore = diskInfoBeforeResponse.jsonPath().getLong("total_space");
        long freeSpaceBefore = totalSpaceBefore - usedSpaceBefore;
        step("Занято места до загрузки: " + usedSpaceBefore, () -> {});
        step("Свободно места до загрузки: " + freeSpaceBefore, () -> {});

        String expectedText = TextGeneratorUtil.generateText(100);
        step("Загружаем текстовый файл", () -> {
            Response uploadFileResponse = yandexDiskApiClient.uploadTextFile(filePath, expectedText);
            assertEquals(201, uploadFileResponse.statusCode(), "Не удалось загрузить файл");
        });

        Response getFileResponse = step("Получаем информацию о файле", () ->
                yandexDiskApiClient.getResource(filePath)
        );

        step("Проверяем, что файл найден", () ->
                assertEquals(200, getFileResponse.statusCode(), "Файл не найден")
        );

        long fileSize = getFileResponse.jsonPath().getLong("size");
        step("Размер файла: " + fileSize, () -> {});

        boolean isUsedSpaceChanged = step("Ожидаем обновления занятого места на Диске", () ->
                WaitUtil.waitForUsedSpaceChange(yandexDiskApiClient::getDiskInfo, usedSpaceBefore, fileSize)
        );

        step("Проверяем, что занятое место изменилось на размер файла", () ->
                assertTrue(isUsedSpaceChanged, "Занятое место не изменилось на размер файла")
        );

        Response diskInfoAfterResponse = step("Получаем информацию о месте на Диске после загрузки файла", () ->
                yandexDiskApiClient.getDiskInfo()
        );

        step("Проверяем, что информация о Диске получена после загрузки файла", () ->
                assertEquals(200, diskInfoAfterResponse.statusCode(), "Не удалось получить информацию о Диске")
        );

        long usedSpaceAfter = diskInfoAfterResponse.jsonPath().getLong("used_space");
        long totalSpaceAfter = diskInfoAfterResponse.jsonPath().getLong("total_space");
        long freeSpaceAfter = totalSpaceAfter - usedSpaceAfter;

        step("Занято места после загрузки: " + usedSpaceAfter);
        step("Свободно места после загрузки: " + freeSpaceAfter);

        step("Проверяем, что занятое место увеличилось ровно на размер файла", () -> {
            assertEquals(fileSize, usedSpaceAfter - usedSpaceBefore, "Занятое место изменилось не на размер файла");
        });

        step("Проверяем, что свободное место уменьшилось ровно на размер файла", () -> {
            assertEquals(fileSize, freeSpaceBefore - freeSpaceAfter, "Свободное место изменилось не на размер файла");
        });

        step("Удаляем созданный файл", () -> {
            Response deleteFileResponse = yandexDiskApiClient.deleteFile(filePath);
            assertEquals(204, deleteFileResponse.statusCode(), "Не удалось удалить файл");
        });
    }

    @Test
    @Story("Копирование файла")
    @Description("Создание текстого файла, копирование данного файла и проверка совпадения текста")
    public void testFileCopy() {
        TestData testData = TestDataReader.getTestData();

        String sourceFilePath = testData.getApiCopyFile();
        String copiedFilePath = testData.getNewFileTxt();
        String expectedText = TextGeneratorUtil.generateText(100);

        step("Создаем текстовый файл с рандомным текстом", () -> {
            Response uploadSourceFileResponse = yandexDiskApiClient.uploadTextFile(sourceFilePath, expectedText);
            assertEquals(201, uploadSourceFileResponse.statusCode(), "Не удалось создать исходный файл");
        });

        Response getSourceFileResponse = step("Проверяем, что исходный файл создан", () ->
                yandexDiskApiClient.getResource(sourceFilePath)
        );

        step("Проверяем, что исходный файл найден", () -> {
            assertEquals(200, getSourceFileResponse.statusCode(), "Исходный файл не найден");
            assertEquals("file", getSourceFileResponse.jsonPath().getString("type"), "Ресурс не является файлом");
        });

        step("Копируем файл", () -> {
            Response copyFileResponse = yandexDiskApiClient.copyResource(sourceFilePath, copiedFilePath);
            assertEquals(201, copyFileResponse.statusCode(), "Не удалось скопировать файл");
        });

        Response getCopiedFileResponse = step("Проверяем, что скопированный файл создан", () ->
                yandexDiskApiClient.getResource(copiedFilePath)
        );

        step("Проверяем, что скопированный файл найден", () -> {
            assertEquals(200, getCopiedFileResponse.statusCode(), "Скопированный файл не найден");
            assertEquals("file", getCopiedFileResponse.jsonPath().getString("type"), "Ресурс не является файлом");
        });

        step("Проверяем, что размер исходного и скопированного файла совпадает", () -> {
            long sourceFileSize = getSourceFileResponse.jsonPath().getLong("size");
            long copiedFileSize = getCopiedFileResponse.jsonPath().getLong("size");
            assertEquals(sourceFileSize, copiedFileSize, "Размер исходного и скопированного файла отличается");
        });

        byte[] sourceFileBytes = step("Скачиваем исходный файл", () ->
                yandexDiskApiClient.downloadFileBytes(sourceFilePath)
        );

        byte[] copiedFileBytes = step("Скачиваем скопированный файл", () ->
                yandexDiskApiClient.downloadFileBytes(copiedFilePath)
        );

        String sourceFileText = new String(sourceFileBytes);
        String copiedFileText = new String(copiedFileBytes);

        step("Проверяем, что текст внутри файлов совпадает", () -> {
            assertEquals(sourceFileText, copiedFileText, "Текст внутри исходного и скопированного файла отличается");
        });

        step("Проверяем, что текст скопированного файла совпадает с ожидаемым", () -> {
            assertEquals(expectedText, copiedFileText, "Текст скопированного файла не совпадает с ожидаемым");
        });
    }
}