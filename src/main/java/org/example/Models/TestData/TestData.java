package org.example.Models.TestData;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestData {

    @JsonProperty("fileApi")
    private FileApiData fileApi;

    @JsonProperty("imageApi")
    private ImageApiData imageApi;

    @JsonProperty("trashApi")
    private TrashApiData trashApi;

    @JsonProperty("fileListApi")
    private FileListApiData fileListApi;

    public FileApiData getFileApi() {
        return fileApi;
    }

    public void setFileApi(FileApiData fileApi) {
        this.fileApi = fileApi;
    }

    public ImageApiData getImageApi() {
        return imageApi;
    }

    public void setImageApi(ImageApiData imageApi) {
        this.imageApi = imageApi;
    }

    public TrashApiData getTrashApi() {
        return trashApi;
    }

    public void setTrashApi(TrashApiData trashApi) {
        this.trashApi = trashApi;
    }

    public FileListApiData getFileListApi() {
        return fileListApi;
    }

    public void setFileListApi(FileListApiData fileListApi) {
        this.fileListApi = fileListApi;
    }
}