import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.example.Models.TestData;
import org.example.Models.TrashResource;
import org.example.Utils.TestDataReader;
import org.example.Utils.TextGeneratorUtil;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Yandex Disk API")
@Feature("Корзина")
public class DiskTrashApiTest extends BaseTest{

    @Test
    @Story("Восстановление файла из корзины")
    @Description("Перемещение файла в корзину и восстановление его из корзины")
    public void moveFileToTrashAndRestoreTest() {
        TestData testData = TestDataReader.getTestData();
        String filePath = testData.getNewFileTxt();

        step("Создаем текстовый файл", () -> {
            String expectedText = TextGeneratorUtil.generateText(100);
            Response uploadFileResponse = yandexDiskApiClient.uploadTextFile(filePath, expectedText);
            assertEquals(201, uploadFileResponse.statusCode(), "Не удалось создать файл");
        });

        step("Перемещаем файл в корзину", () -> {
            Response moveToTrashResponse = yandexDiskApiClient.moveFileToTrash(filePath);
            assertEquals(204, moveToTrashResponse.statusCode(), "Не удалось переместить файл в корзину");
        });

        step("Проверяем, что файл отсутствует на Диске", () -> {
            Response getDeletedFileResponse = yandexDiskApiClient.getResource(filePath);
            assertEquals(404, getDeletedFileResponse.statusCode(), "Файл всё ещё находится на Диске");
        });

        TrashResource trash = step("Получаем содержимое корзины", () ->
                yandexDiskApiClient.getTrashFiles(testData.getDefaultPath())
        );

        Optional<TrashResource> fileInTrash = step("Ищем файл new_file.txt в корзине", () ->
                trash.getEmbedded().getItems().stream()
                        .filter(resource -> resource.getName().equals("new_file.txt"))
                        .findFirst()
        );

        step("Проверяем, что файл найден в корзине", () ->
                assertTrue(fileInTrash.isPresent(), "Файл не найден в корзине")
        );

        step("Восстанавливаем файл из корзины", () -> {
            Response restoreFileResponse = yandexDiskApiClient.restoreFile(fileInTrash.get().getPath());
            assertEquals(201, restoreFileResponse.statusCode(), "Не удалось восстановить файл из корзины");
        });

        step("Проверяем, что файл снова появился на Диске", () -> {
            Response getRestoredFileResponse = yandexDiskApiClient.getResource(filePath);
            assertEquals(200, getRestoredFileResponse.statusCode(), "Файл не восстановился на Диск");
        });
    }

    @Test
    @Story("Полное удаление файла из корзины")
    @Description("Перемещение файла в корзину и полное удаление его из корзины")
    public void testMoveFileToTrashAndFullDelete() {
        TestData testData = TestDataReader.getTestData();
        String filePath = testData.getApiCopyFile();

        step("Создаем текстовый файл", () -> {
            String expectedText = TextGeneratorUtil.generateText(100);
            Response uploadFileResponse = yandexDiskApiClient.uploadTextFile(filePath, expectedText);
            assertEquals(201, uploadFileResponse.statusCode(), "Не удалось создать файл");
        });

        step("Перемещаем файл в корзину", () -> {
            Response moveToTrashResponse = yandexDiskApiClient.moveFileToTrash(filePath);
            assertEquals(204, moveToTrashResponse.statusCode(), "Не удалось переместить файл в корзину");
        });

        step("Проверяем, что файл отсутствует на Диске", () -> {
            Response getDeletedFileResponse = yandexDiskApiClient.getResource(filePath);
            assertEquals(404, getDeletedFileResponse.statusCode(), "Файл всё ещё находится на Диске");
        });

        TrashResource trash = step("Получаем содержимое корзины", () ->
                yandexDiskApiClient.getTrashFiles(testData.getDefaultPath())
        );

        Optional<TrashResource> fileInTrash = step("Ищем файл new_file.txt в корзине", () ->
                trash.getEmbedded().getItems().stream()
                        .filter(resource -> resource.getName().equals("copy_test_file.txt"))
                        .findFirst()
        );

        step("Проверяем, что файл найден в корзине", () ->
                assertTrue(fileInTrash.isPresent(), "Файл не найден в корзине")
        );

        String fileInTrashPath = fileInTrash.get().getPath();

        step("Удаляем файл из корзины навсегда", () -> {
            Response deleteForeverResponse = yandexDiskApiClient.deleteFileFromTrash(fileInTrashPath);
            assertEquals(204, deleteForeverResponse.statusCode(), "Не удалось удалить файл из корзины навсегда");
        });

        step("Проверяем, что файл больше не находится в корзине", () -> {
            TrashResource trashAfterDelete = yandexDiskApiClient.getTrashFiles(testData.getDefaultPath());

            boolean fileStillExistsInTrash = trashAfterDelete.getEmbedded().getItems().stream()
                    .anyMatch(resource -> resource.getPath().equals(fileInTrashPath));

            assertFalse(fileStillExistsInTrash, "Файл всё ещё находится в корзине");
        });
    }

}
