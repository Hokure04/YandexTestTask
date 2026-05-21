import io.restassured.response.Response;
import org.example.Models.TestData;
import org.example.Models.TrashResource;
import org.example.Utils.TestDataReader;
import org.example.Utils.TextGeneratorUtil;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DiskTrashApiTest extends BaseTest{

    @Test
    public void moveFileToTrashAndRestoreTest() {
        TestData testData = TestDataReader.getTestData();
        String filePath = testData.getNewFileTxt();

        System.out.println("Создаем текстовый файл");
        String expectedText = TextGeneratorUtil.generateText(100);
        Response uploadFileResponse = yandexDiskApiClient.uploadTextFile(filePath, expectedText);
        assertEquals(201, uploadFileResponse.statusCode(), "Не удалось создать файл");


        System.out.println("Перемещаем файл в корзину");
        Response moveToTrashResponse = yandexDiskApiClient.moveFileToTrash(filePath);
        assertEquals(204, moveToTrashResponse.statusCode(), "Не удалось переместить файл в корзину");


        System.out.println("Проверяем, что файл отсутствует на Диске");
        Response getDeletedFileResponse = yandexDiskApiClient.getResource(filePath);
        assertEquals(404, getDeletedFileResponse.statusCode(), "Файл всё ещё находится на Диске");


        System.out.println("Проверяем, что файл появился в корзине");
        TrashResource trash = yandexDiskApiClient.getTrashFiles(testData.getDefaultPath());
        Optional<TrashResource> fileInTrash = trash.getEmbedded().getItems().stream()
                .filter(resource -> resource.getName().equals("new_file.txt"))
                .findFirst();
        assertTrue(fileInTrash.isPresent(), "Файл не найден в корзине");


        System.out.println("Восстанавливаем файл из корзины");
        Response restoreFileResponse = yandexDiskApiClient.restoreFile(fileInTrash.get().getPath());
        assertEquals(201, restoreFileResponse.statusCode(), "Не удалось восстановить файл из корзины");


        System.out.println("Проверяем, что файл снова появился на Диске");
        Response getRestoredFileResponse = yandexDiskApiClient.getResource(filePath);
        assertEquals(200, getRestoredFileResponse.statusCode(), "Файл не восстановился на Диск");
    }

    @Test
    public void testMoveFileToTrashAndDeleteForever() {
        TestData testData = TestDataReader.getTestData();
        String filePath = testData.getApiCopyFile();

        System.out.println("Создаем текстовый файл");
        String expectedText = TextGeneratorUtil.generateText(100);
        Response uploadFileResponse = yandexDiskApiClient.uploadTextFile(filePath, expectedText);
        assertEquals(201, uploadFileResponse.statusCode(), "Не удалось создать файл");

        System.out.println("Перемещаем файл в корзину");
        Response moveToTrashResponse = yandexDiskApiClient.moveFileToTrash(filePath);
        assertEquals(204, moveToTrashResponse.statusCode(), "Не удалось переместить файл в корзину");


        System.out.println("Проверяем, что файл отсутствует на Диске");
        Response getDeletedFileResponse = yandexDiskApiClient.getResource(filePath);
        assertEquals(404, getDeletedFileResponse.statusCode(), "Файл всё ещё находится на Диске");


        System.out.println("Проверяем, что файл появился в корзине");
        TrashResource trash = yandexDiskApiClient.getTrashFiles(testData.getDefaultPath());
        Optional<TrashResource> fileInTrash = trash.getEmbedded().getItems().stream()
                .filter(resource -> resource.getName().equals("copy_test_file.txt"))
                .findFirst();
        assertTrue(fileInTrash.isPresent(), "Файл не найден в корзине");


        System.out.println("Удаляем файл из корзины навсегда");
        Response deleteForeverResponse = yandexDiskApiClient.deleteFileFromTrash(fileInTrash.get().getPath());
        assertEquals(204, deleteForeverResponse.statusCode(), "Не удалось удалить файл из корзины навсегда");


        System.out.println("Проверяем, что файл больше не находится в корзине");
        TrashResource trashAfterDelete = yandexDiskApiClient.getTrashFiles(testData.getDefaultPath());
        boolean fileStillExistsInTrash = trashAfterDelete.getEmbedded().getItems().stream()
                .anyMatch(resource -> resource.getName().equals("copy_test_file.txt"));
        assertFalse(fileStillExistsInTrash, "Файл всё ещё находится в корзине");
    }

}
