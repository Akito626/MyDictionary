package com.alha_app.mydictionary.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "WORD_DATA")
public class WordEntity {
    @PrimaryKey
    private int id;
    private int group;
    private String word;
    private String kana;
    private String detail;

    public WordEntity(int id, int group, String word, String kana, String detail){
        this.id = id;
        this.group = group;
        this.word = word;
        this.kana = kana;
        this.detail = detail;
    }

    // Getter
    public int getId() {
        return id;
    }

    public int getGroup() {
        return group;
    }

    public String getWord() {
        return word;
    }

    public String getKana() {
        return kana;
    }

    public String getDetail() {
        return detail;
    }

    // Setter
    public void setId(int id) {
        this.id = id;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setKana(String kana) {
        this.kana = kana;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
