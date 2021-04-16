package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE notetitle = #{notetitle}")
    Note getNote(String notetitle);

    @Select("SELECT * FROM NOTES WHERE noteid = #{noteid}")
    Note getNoteById(Integer noteid);

    @Select("SELECT * FROM NOTES WHERE userid = #{userid}")
    List<Note> getAllNotes(Integer userid);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{notetitle}, #{notedescription}, #{userid})")
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    int insertNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteid}")
    void deteleNote(Integer noteid);

    @Update("UPDATE NOTES SET notetitle = #{newTitle}, notedescription = #{notedescription} WHERE noteid = #{noteid}")
    int updateNote(String newTitle, String notedescription, Integer noteid);

}
