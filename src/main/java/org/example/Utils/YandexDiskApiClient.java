package org.example.Utils;

import io.restassured.response.Response;
import org.example.Models.LinkResource;
import org.example.Models.TrashResource;

import static io.restassured.RestAssured.given;

public class YandexDiskApiClient extends BaseApi{

    public static final String TEXT_PLAIN = "text/plain";
    public static final String IMAGE_JPEG = "image/jpeg";

    private final String token;

    public YandexDiskApiClient(){
        super(SettingsReader.getSettings().getApiUrl());
        this.token = CredentialsReader.getUserData().getToken();
    }

    private String authHeader(){
        return "OAuth "+ token;
    }

    public Response getDiskInfo() {
        return request()
                .header("Authorization", authHeader())
                .get("/v1/disk");
    }

    public Response getResource(String path) {
        return request()
                .header("Authorization", authHeader())
                .queryParam("path", path)
                .get("/v1/disk/resources");
    }

    public Response createFolder(String path) {
        return request()
                .header("Authorization", authHeader())
                .queryParam("path", path)
                .put("/v1/disk/resources");
    }

    public Response getUploadResourceResponse(String path){
        return request()
                .header("Authorization", authHeader())
                .queryParam("path", path)
                .queryParam("overwrite", true)
                .get("/v1/disk/resources/upload");
    }

    public LinkResource getUploadResource(String path){
        return getUploadResourceResponse(path)
                .then()
                .statusCode(200)
                .extract()
                .as(LinkResource.class);
    }

    public Response uploadTextFile(String path, String content){
        LinkResource uploadResource = getUploadResource(path);
        return request()
                .contentType(TEXT_PLAIN)
                .body(content)
                .put(uploadResource.getHref());
    }

    public Response uploadImageFile(String path, byte[] imageBytes){
        LinkResource uploadResource = getUploadResource(path);
        return request()
                .contentType(IMAGE_JPEG)
                .body(imageBytes)
                .put(uploadResource.getHref());
    }

    public Response changeExtensions(String path, String extension){
        int index = path.lastIndexOf('.');

        if(index < 0){
            throw new IllegalArgumentException("Расширения файла не обнаружено "+path );
        }

        String newFileName = path.substring(0, index) + extension;
        return request()
                .header("Authorization", authHeader())
                .queryParam("from", path)
                .queryParam("path", newFileName)
                .queryParam("overwrite", true)
                .post("/v1/disk/resources/move");
    }

    public LinkResource getDownloadResource(String path){
        return request()
                .header("Authorization", authHeader())
                .queryParam("path", path)
                .get("/v1/disk/resources/download")
                .then()
                .statusCode(200)
                .extract()
                .as(LinkResource.class);
    }

    public Response moveResource(String from, String to) {
        return request()
                .header("Authorization", authHeader())
                .queryParam("from", from)
                .queryParam("path", to)
                .queryParam("overwrite", true)
                .post("/v1/disk/resources/move");
    }

    public Response copyResource(String from, String to) {
        return request()
                .header("Authorization", authHeader())
                .queryParam("from", from)
                .queryParam("path", to)
                .queryParam("overwrite", true)
                .post("/v1/disk/resources/copy");
    }


    public byte[] downloadFileBytes(String path) {
        LinkResource downloadResource = getDownloadResource(path);

        Response response = given()
                .urlEncodingEnabled(false)
                .redirects()
                .follow(true)
                .get(downloadResource.getHref());

        /*System.out.println("Download path: " + path);
        System.out.println("Download href: " + downloadResource.getHref());
        System.out.println("Download status: " + response.statusCode());*/

        return response
                .then()
                .statusCode(200)
                .extract()
                .asByteArray();
    }

    public TrashResource getTrashFiles(String path) {
        return request()
                .header("Authorization", authHeader())
                .queryParam("path", path)
                .get("/v1/disk/trash/resources")
                .then()
                .statusCode(200)
                .extract()
                .as(TrashResource.class);
    }

    public Response restoreFile(String trashPath) {
        return request()
                .header("Authorization", authHeader())
                .queryParam("path", trashPath)
                .put("/v1/disk/trash/resources/restore");
    }

    public Response deleteFile(String path) {
        return request()
                .header("Authorization", authHeader())
                .queryParam("path", path)
                .queryParam("permanently", true)
                .delete("/v1/disk/resources");
    }

    public Response moveFileToTrash(String path) {
        return request()
                .header("Authorization", authHeader())
                .queryParam("path", path)
                .queryParam("permanently", false)
                .delete("/v1/disk/resources");
    }


}
