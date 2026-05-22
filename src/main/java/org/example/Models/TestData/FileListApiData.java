package org.example.Models.TestData;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileListApiData {

    @JsonProperty("sortedFilePath")
    private String sortedFilePath;

    @JsonProperty("lastUploadedFilePath")
    private String lastUploadedFilePath;

    public String getSortedFilePath() {
        return sortedFilePath;
    }

    public void setSortedFilePath(String sortedFilePath) {
        this.sortedFilePath = sortedFilePath;
    }

    public String getLastUploadedFilePath() {
        return lastUploadedFilePath;
    }

    public void setLastUploadedFilePath(String lastUploadedFilePath) {
        this.lastUploadedFilePath = lastUploadedFilePath;
    }
}
