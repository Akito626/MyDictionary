package com.alha_app.mydictionary.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DictionaryEntity.class, WordEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DictionaryDao dictionaryDao();
    public abstract WordDao wordDao();
}
