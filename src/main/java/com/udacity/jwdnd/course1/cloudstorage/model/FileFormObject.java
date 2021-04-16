package com.udacity.jwdnd.course1.cloudstorage.model;

import org.springframework.web.multipart.MultipartFile;

public class FileFormObject {
    private Integer fileId;
    private MultipartFile fileUpload;

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public MultipartFile getFileUpload() {
        return fileUpload;
    }

    public void setFileUpload(MultipartFile fileUpload) {
        this.fileUpload = fileUpload;
    }
}
