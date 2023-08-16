package com.alha_app.mydictionary.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDao {
    @Query("SELECT * FROM WORD_DATA where `group` = :id")
    List<WordEntity> getAll(int id);

    @Query("UPDATE WORD_DATA SET word = :word, kana = :kana, detail = :detail, tag = :tag where id = :id")
    void update(int id, String word, String kana, String detail, String tag);

    @Query("DELETE FROM WORD_DATA where `group` = :id")
    void deleteAll(int id);

    @Query("DELETE FROM WORD_DATA where id = :id")
    void delete(String id);

    @Insert
    long insert(WordEntity wordEntity);
}
