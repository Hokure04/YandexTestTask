package org.example.Models.TestData;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ImageApiData {

    @JsonProperty("diskImagePath")
    private String diskImagePath;

    @JsonProperty("localImagePath")
    private String localImagePath;

    @JsonProperty("textFilePath")
    private String textFilePath;

    @JsonProperty("defaultFilename")
    private String defaultFilename;

    @JsonProperty("newExtension")
    private String newExtension;

    public String getDiskImagePath() {
        return diskImagePath;
    }

    public void setDiskImagePath(String diskImagePath) {
        this.diskImagePath = diskImagePath;
    }

    public String getLocalImagePath() {
        return localImagePath;
    }

    public void setLocalImagePath(String localImagePath) {
        this.localImagePath = localImagePath;
    }

    public String getTextFilePath() {
        return textFilePath;
    }

    public void setTextFilePath(String textFilePath) {
        this.textFilePath = textFilePath;
    }

    public String getDefaultFilename() {
        return defaultFilename;
    }

    public void setDefaultFilename(String defaultFilename) {
        this.defaultFilename = defaultFilename;
    }

    public String getNewExtension() {
        return newExtension;
    }

    public void setNewExtension(String newExtension) {
        this.newExtension = newExtension;
    }
}
