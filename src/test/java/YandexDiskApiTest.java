import io.restassured.response.Response;
import org.example.Models.TestData;
import org.example.Utils.RandomUtil;
import org.example.Utils.TestDataReader;
import org.example.Utils.TextGeneratorUtil;
import org.example.Utils.WaitUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class YandexDiskApiTest extends BaseTest {

    @Test
    public void yandexDiskApiSimpleTest() {
        TestData testData = TestDataReader.getTestData();
        int min = 1000;
        int max = 10000;
        String folderPath = String.format(testData.getApiTestFolder(), RandomUtil.getRandomNumberAsString(min, max));
        String filePath = folderPath + "/" + testData.getApiTestFileName();
        String renamedFilePath = folderPath + "/" + testData.getApiTestRenamedFileName();


        System.out.println("Создаем папку");
        Response createFolderResponse = yandexDiskApiClient.createFolder(folderPath);
        assertEquals(201, createFolderResponse.statusCode(), "Не удалось создать папку");


        System.out.println("Проверяем, что папка создана");
        Response getFolderResponse = yandexDiskApiClient.getResource(folderPath);
        assertEquals(200, getFolderResponse.statusCode(), "Созданная папка не найдена");
        assertEquals("dir", getFolderResponse.jsonPath().getString("type"), "Ресурс не является папкой");
        assertEquals(folderPath, getFolderResponse.jsonPath().getString("name"), "Название папки не совпадает с ожидаемым");


        System.out.println("Загружаем текстовый файл");
        String expectedText = TextGeneratorUtil.generateText(20);
        Response uploadFileResponse = yandexDiskApiClient.uploadTextFile(filePath, expectedText);
        assertEquals(201, uploadFileResponse.statusCode(), "Не удалось загрузить файл");

        System.out.println("Проверяем, что файл находится на Диске");
        Response getFileResponse = yandexDiskApiClient.getResource(filePath);
        assertEquals(200, getFileResponse.statusCode(), "Файл не найден");
        assertEquals("file", getFileResponse.jsonPath().getString("type"), "Ресурс не является файлом");
        assertEquals(testData.getApiTestFileName(), getFileResponse.jsonPath().getString("name"), "Имя файла не совпадает с ожидаемым");


        System.out.println("Переименовываем файл");
        Response moveFileResponse = yandexDiskApiClient.moveResource(filePath, renamedFilePath);
        assertEquals(201, moveFileResponse.statusCode(), "Не удалось переименовать файл");


        System.out.println("Проверяем файл по новому имени");
        Response getRenamedFileResponse = yandexDiskApiClient.getResource(renamedFilePath);
        assertEquals(200, getRenamedFileResponse.statusCode(), "Переименованный файл не найден");
        assertEquals(testData.getApiTestRenamedFileName(), getRenamedFileResponse.jsonPath().getString("name"), "Имя переименованного файла не сопадает с ожидаемым");


        System.out.println("Удаляем папку со всем содержимым");
        Response deleteFolderResponse = yandexDiskApiClient.deleteFile(folderPath);
        assertEquals(202, deleteFolderResponse.statusCode(), "Не удалось удалить папку");


        System.out.println("Проверяем, что папка удалена");
        Response getDeletedFolderResponse = yandexDiskApiClient.getResource(folderPath);
        assertEquals(404, getDeletedFolderResponse.statusCode(), "Папка всё ещё существует");
    }

    @Test
    public void diskSpaceTest(){
        TestData testData = TestDataReader.getTestData();

        int min = 1000;
        int max = 10000;

        String filePath = String.format(testData.getApiTestFileName(),RandomUtil.getRandomNumberAsString(min, max));

        System.out.println("Получаем информацию о месте на Диске до загрузки файла");
        Response diskInfoBeforeResponse = yandexDiskApiClient.getDiskInfo();
        assertEquals(200, diskInfoBeforeResponse.statusCode(), "Не удалось получить информацию о Диске");

        long usedSpaceBefore = diskInfoBeforeResponse.jsonPath().getLong("used_space");
        long totalSpaceBefore = diskInfoBeforeResponse.jsonPath().getLong("total_space");
        long freeSpaceBefore = totalSpaceBefore - usedSpaceBefore;
        System.out.println("Занято места до загрузки: " + usedSpaceBefore);
        System.out.println("Свободно места до загрузки: " + freeSpaceBefore);

        System.out.println("Загружаем текстовый файл");
        String expectedText = TextGeneratorUtil.generateText(100);
        Response uploadFileResponse = yandexDiskApiClient.uploadTextFile(filePath, expectedText);
        assertEquals(201, uploadFileResponse.statusCode(), "Не удалось загрузить файл");

        System.out.println("Получаем информацию о загруженном файле");
        Response getFileResponse = yandexDiskApiClient.getResource(filePath);
        assertEquals(200, getFileResponse.statusCode(), "Файл не найден");

        long fileSize = getFileResponse.jsonPath().getLong("size");
        System.out.println("Размер файла: " + fileSize);

        System.out.println("Ожидаем обновления занятого места на Диске");
        boolean isUsedSpaceChanged = WaitUtil.waitForUsedSpaceChange(yandexDiskApiClient::getDiskInfo, usedSpaceBefore, fileSize);
        assertTrue(isUsedSpaceChanged, "Занятое место не изменилось на размер файла");

        System.out.println("Получаем информацию о месте на Диске после загрузки файла");
        Response diskInfoAfterResponse = yandexDiskApiClient.getDiskInfo();
        assertEquals(200, diskInfoAfterResponse.statusCode(), "Не удалось получить информацию о Диске");
        long usedSpaceAfter = diskInfoAfterResponse.jsonPath().getLong("used_space");
        long totalSpaceAfter = diskInfoAfterResponse.jsonPath().getLong("total_space");
        long freeSpaceAfter = totalSpaceAfter - usedSpaceAfter;
        System.out.println("Занято места после загрузки: " + usedSpaceAfter);
        System.out.println("Свободно места после загрузки: " + freeSpaceAfter);

        System.out.println("Проверяем, что занятое место увеличилось ровно на размер файла");
        assertEquals(fileSize, usedSpaceAfter - usedSpaceBefore, "Занятое место изменилось не на размер файла");

        System.out.println("Проверяем, что свободное место уменьшилось ровно на размер файла");
        assertEquals(fileSize, freeSpaceBefore - freeSpaceAfter, "Свободное место изменилось не на размер файла");


        System.out.println("Удаляем созданный файл");
        Response deleteFileResponse = yandexDiskApiClient.deleteFile(filePath);
        assertEquals(204, deleteFileResponse.statusCode(), "Не удалось удалить файл");
    }

    @Test
    public void testFileCopy() {
        TestData testData = TestDataReader.getTestData();

        String sourceFilePath = testData.getApiCopyFile();
        String copiedFilePath = testData.getNewFileTxt();


        System.out.println("Создаем текстовый файл с рандомным текстом");
        String expectedText = TextGeneratorUtil.generateText(100);

        Response uploadSourceFileResponse = yandexDiskApiClient.uploadTextFile(sourceFilePath, expectedText);
        assertEquals(201, uploadSourceFileResponse.statusCode(), "Не удалось создать исходный файл");


        System.out.println("Проверяем, что исходный файл создан");
        Response getSourceFileResponse = yandexDiskApiClient.getResource(sourceFilePath);
        assertEquals(200, getSourceFileResponse.statusCode(), "Исходный файл не найден");
        assertEquals("file", getSourceFileResponse.jsonPath().getString("type"), "Исходный ресурс не является файлом");


        System.out.println("Копируем файл");
        Response copyFileResponse = yandexDiskApiClient.copyResource(sourceFilePath, copiedFilePath);
        assertEquals(201, copyFileResponse.statusCode(), "Не удалось скопировать файл");


        System.out.println("Проверяем, что скопированный файл создан");
        Response getCopiedFileResponse = yandexDiskApiClient.getResource(copiedFilePath);
        assertEquals(200, getCopiedFileResponse.statusCode(), "Скопированный файл не найден");
        assertEquals("file", getCopiedFileResponse.jsonPath().getString("type"), "Скопированный ресурс не является файлом");


        System.out.println("Проверяем, что размер исходного и скопированного файла совпадает");
        long sourceFileSize = getSourceFileResponse.jsonPath().getLong("size");
        long copiedFileSize = getCopiedFileResponse.jsonPath().getLong("size");
        assertEquals(sourceFileSize, copiedFileSize, "Размер исходного и скопированного файла отличается");


        System.out.println("Скачиваем исходный файл");
        byte[] sourceFileBytes = yandexDiskApiClient.downloadFileBytes(sourceFilePath);

        System.out.println("Скачиваем скопированный файл");
        byte[] copiedFileBytes = yandexDiskApiClient.downloadFileBytes(copiedFilePath);


        String sourceFileText = new String(sourceFileBytes);
        String copiedFileText = new String(copiedFileBytes);
        System.out.println("Проверяем, что текст внутри файлов совпадает");
        assertEquals(sourceFileText, copiedFileText, "Текст внутри исходного и скопированного файла отличается");


        System.out.println("Проверяем, что текст скопированного файла совпадает с ожидаемым");
        assertEquals(expectedText, copiedFileText, "Текст скопированного файла не совпадает с ожидаемым");
    }
}