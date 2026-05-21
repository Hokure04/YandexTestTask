package org.example.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TestData {

    @JsonProperty("apiTestFolder")
    private String apiTestFolder;

    @JsonProperty("apiTestFile")
    private String apiTestFileName;

    @JsonProperty("apiRenamedFile")
    private String apiRenamedFile;

    @JsonProperty("newFileTxt")
    private String newFileTxt;

    @JsonProperty("newFileJpg")
    private String newFileJpg;

    @JsonProperty("defaultFilename")
    private String defaultFilename;

    @JsonProperty("extension")
    private String extension;

    @JsonProperty("defaultPath")
    private String defaultPath;

    @JsonProperty("apiCopyFile")
    private String apiCopyFile;

    @JsonProperty("apiImageFile")
    private String apiImageFile;

    @JsonProperty("localImageFile")
    private String localImageFile;

    public String getApiRenamedFile() {
        return apiRenamedFile;
    }

    public void setApiRenamedFile(String apiRenamedFile) {
        this.apiRenamedFile = apiRenamedFile;
    }

    public String getApiImageFile() {
        return apiImageFile;
    }

    public void setApiImageFile(String apiImageFile) {
        this.apiImageFile = apiImageFile;
    }

    public String getLocalImageFile() {
        return localImageFile;
    }

    public void setLocalImageFile(String localImageFile) {
        this.localImageFile = localImageFile;
    }

    public String getApiCopyFile() {
        return apiCopyFile;
    }

    public void setApiCopyFile(String apiCopyFile) {
        this.apiCopyFile = apiCopyFile;
    }

    public String getDefaultPath() {
        return defaultPath;
    }

    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getDefaultFilename() {
        return defaultFilename;
    }

    public void setDefaultFilename(String defaultFilename) {
        this.defaultFilename = defaultFilename;
    }

    public String getNewFileJpg() {
        return newFileJpg;
    }

    public void setNewFileJpg(String newFileJpg) {
        this.newFileJpg = newFileJpg;
    }

    public String getApiTestFolder() {
        return apiTestFolder;
    }

    public void setApiTestFolder(String apiTestFolder) {
        this.apiTestFolder = apiTestFolder;
    }

    public String getApiTestFileName() {
        return apiTestFileName;
    }

    public void setApiTestFileName(String apiTestFileName) {
        this.apiTestFileName = apiTestFileName;
    }

    public String getApiTestRenamedFileName() {
        return apiRenamedFile;
    }

    public void setApiTestRenamedFileName(String apiTestRenamedFileName) {
        this.apiRenamedFile = apiTestRenamedFileName;
    }

    public String getNewFileTxt() {
        return newFileTxt;
    }

    public void setNewFileTxt(String newFileTxt) {
        this.newFileTxt = newFileTxt;
    }
}
