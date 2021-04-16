package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialFormObject;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private UserMapper userMapper;
    private CredentialsMapper credentialsMapper;
    private EncryptDecryptService encryptDecryptService;
    private User currentUser;

    public CredentialService(UserMapper userMapper, CredentialsMapper credentialsMapper, EncryptDecryptService encryptDecryptService) {
        this.userMapper = userMapper;
        this.credentialsMapper = credentialsMapper;
        this.encryptDecryptService = encryptDecryptService;
    }

//    @PostConstruct
//    public void postConstruct(Authentication authentication){
//        currentUser = userMapper.getUser(authentication.getName());
//    }


    public int addCredential(CredentialFormObject credentialFormObject, Authentication authentication){
        Credential credential = new Credential();
        credential.setUrl(credentialFormObject.getCredentialUrl());
        credential.setUsername(credentialFormObject.getCredentialUsername());
        credential.setKey(encryptDecryptService.getEncodedKey());
        String encryptedPassword = encryptDecryptService.encrypt(credentialFormObject.getCredentialPassword());
        credential.setPassword(encryptedPassword);
        currentUser = userMapper.getUser(authentication.getName());
        credential.setUserid(currentUser.getUserid());
        credentialsMapper.insertCredential(credential);
        return 0;
    }

    public List<Credential> getAllCredentials(Authentication authentication){
        currentUser = userMapper.getUser(authentication.getName());
        return credentialsMapper.getAllCredentials(currentUser.getUserid());
    }

    public List<Credential> getAllCredentialsWithoutId(){
        return credentialsMapper.getAllCredentialsWithoutId();
    }

    public Credential getCredential(Integer credentialId){
        return credentialsMapper.getCredential(credentialId);
    }


    public int deleteCredential(Integer credentialId){
        return credentialsMapper.deteleCredential(credentialId);

    }

    public int updateCredential(String newUrl, String newUsername, String newPassword, Integer credentialId){
        String password = encryptDecryptService.encrypt(newPassword);
        int updatedCredential = credentialsMapper.updateCredential(newUrl,newUsername,password,credentialId);
        System.out.println("Updated " + updatedCredential + " credentials");
        return updatedCredential;
    }


}
