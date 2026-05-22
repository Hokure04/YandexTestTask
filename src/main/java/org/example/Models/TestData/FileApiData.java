package org.example.Models.TestData;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileApiData {

    @JsonProperty("testFolder")
    private String testFolder;

    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("renamedFileName")
    private String renamedFileName;

    @JsonProperty("copySourceFile")
    private String copySourceFile;

    @JsonProperty("copyTargetFile")
    private String copyTargetFile;

    @JsonProperty("diskSpaceFileTemplate")
    private String diskSpaceFileTemplate;

    public String getTestFolder() {
        return testFolder;
    }

    public void setTestFolder(String testFolder) {
        this.testFolder = testFolder;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRenamedFileName() {
        return renamedFileName;
    }

    public void setRenamedFileName(String renamedFileName) {
        this.renamedFileName = renamedFileName;
    }

    public String getCopySourceFile() {
        return copySourceFile;
    }

    public void setCopySourceFile(String copySourceFile) {
        this.copySourceFile = copySourceFile;
    }

    public String getCopyTargetFile() {
        return copyTargetFile;
    }

    public void setCopyTargetFile(String copyTargetFile) {
        this.copyTargetFile = copyTargetFile;
    }

    public String getDiskSpaceFileTemplate() {
        return diskSpaceFileTemplate;
    }

    public void setDiskSpaceFileTemplate(String diskSpaceFileTemplate) {
        this.diskSpaceFileTemplate = diskSpaceFileTemplate;
    }
}
