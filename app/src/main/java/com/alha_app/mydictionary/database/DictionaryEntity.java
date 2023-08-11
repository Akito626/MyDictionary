package com.alha_app.mydictionary.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DICTIONARY_DATA")
public class DictionaryEntity {
    @PrimaryKey
    private int id;
    private String title;
    private String detail;

    public DictionaryEntity(int id, String title, String detail){
        this.id = id;
        this.title = title;
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
