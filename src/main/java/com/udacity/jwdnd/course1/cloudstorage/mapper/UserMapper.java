package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Select("SELECT * FROM USERS WHERE userid = #{userid}")
    User getUserById(Integer userid);

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname)"
    + " VALUES(#{username}, #{salt}, #{password}, #{firstname}, #{lastname})")
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    int insert(User user);

    @Delete("DELETE * FROM USERS WHERE username = #{username}")
    void delete(String username);
}
