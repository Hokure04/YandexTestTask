package org.example.Models.TestData;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TrashApiData {

    @JsonProperty("restoreFilePath")
    private String restoreFilePath;

    @JsonProperty("deleteForeverFilePath")
    private String deleteForeverFilePath;

    @JsonProperty("trashRootPath")
    private String trashRootPath;

    public String getRestoreFilePath() {
        return restoreFilePath;
    }

    public void setRestoreFilePath(String restoreFilePath) {
        this.restoreFilePath = restoreFilePath;
    }

    public String getDeleteForeverFilePath() {
        return deleteForeverFilePath;
    }

    public void setDeleteForeverFilePath(String deleteForeverFilePath) {
        this.deleteForeverFilePath = deleteForeverFilePath;
    }

    public String getTrashRootPath() {
        return trashRootPath;
    }

    public void setTrashRootPath(String trashRootPath) {
        this.trashRootPath = trashRootPath;
    }
}
