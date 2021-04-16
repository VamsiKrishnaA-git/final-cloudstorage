package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.FileFormObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
public class FileService {
    private UserMapper userMapper;
    private FileMapper fileMapper;


    @Autowired
    public FileService(UserMapper userMapper, FileMapper fileMapper) {
        this.userMapper = userMapper;
        this.fileMapper = fileMapper;
    }

    public void addFile(FileFormObject fileFormObject, Authentication authentication) throws IOException {
        File file = new File();
        file.setFilename(fileFormObject.getFileUpload().getOriginalFilename());
        file.setContenttype(fileFormObject.getFileUpload().getContentType());
        file.setFilesize(String.valueOf(fileFormObject.getFileUpload().getSize()));
        file.setUserid(userMapper.getUser(authentication.getName()).getUserid());
        file.setFiledata(fileFormObject.getFileUpload().getBytes());
        fileMapper.insertFile(file);
    }

    public boolean fileAlreadyExists(String filename){
        return fileMapper.getFileByName(filename) != null;
    }

    public File getFile(String fileName){
        return fileMapper.getFileByName(fileName);
    }

    public File getFileById(Integer fileId){
        return fileMapper.getFile(fileId);
    }

    public List<File> getAllFiles(Authentication authentication){
        return fileMapper.getAllFiles(userMapper.getUser(authentication.getName()).getUserid());
    }

    public void deleteFile(Integer fileId){
        fileMapper.deleteFile(fileId);
    }

    public File downloadFile(Integer fileId){
        return fileMapper.getFile(fileId);
    }

}
