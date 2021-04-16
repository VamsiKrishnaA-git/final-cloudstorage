package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialsMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    Credential getCredential(Integer credentialid);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    List<Credential> getAllCredentials(Integer userid);

    @Select("SELECT * FROM CREDENTIALS")
    List<Credential> getAllCredentialsWithoutId();

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    int deteleCredential(Integer credentialid);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES (#{url}, #{username}, #{key}, #{password}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    int insertCredential(Credential credential);

    @Update("UPDATE CREDENTIALS SET url = #{newUrl}, username = #{newUsername}, password = #{newPassword} WHERE credentialid = #{credentialId}")
    int updateCredential(String newUrl, String newUsername, String newPassword, Integer credentialId);
}
