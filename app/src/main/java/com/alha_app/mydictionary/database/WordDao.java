package com.alha_app.mydictionary.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDao {
    @Query("SELECT * FROM WORD_DATA where `group` = :num")
    List<WordEntity> getAll(int num);

    @Query("DELETE FROM WORD_DATA")
    void deleteAll();

    @Query("DELETE FROM WORD_DATA where id = :id")
    void delete(int id);

    @Insert
    void insert(WordEntity wordEntity);
}
