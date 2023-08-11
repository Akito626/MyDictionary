package com.alha_app.mydictionary.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DictionaryDao {
    @Query("SELECT * FROM DICTIONARY_DATA")
    List<DictionaryEntity> getAll();

    @Query("DELETE FROM DICTIONARY_DATA")
    void deleteAll();

    @Query("DELETE FROM DICTIONARY_DATA where id = :num")
    void delete(int num);

    @Insert
    void insert(DictionaryEntity dictionaryEntity);
}
