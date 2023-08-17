package com.alha_app.mydictionary.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DictionaryDao {
    @Query("SELECT * FROM DICTIONARY_DATA")
    List<DictionaryEntity> getAll();

    @Query("UPDATE DICTIONARY_DATA SET title = :title, detail = :detail where id = :id")
    void update(int id, String title, String detail);

    @Query("DELETE FROM DICTIONARY_DATA")
    void deleteAll();

    @Query("DELETE FROM DICTIONARY_DATA where id = :id")
    void delete(int id);

    @Insert
    long insert(DictionaryEntity dictionaryEntity);
}
