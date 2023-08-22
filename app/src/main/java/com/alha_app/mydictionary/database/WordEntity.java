package com.alha_app.mydictionary.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "WORD_DATA")
public class WordEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int group;
    private String word;
    private String kana;
    private String detail;
    private String tag1 = "";
    private String tag2 = "";
    private String tag3 = "";

    public WordEntity(int group, String word, String kana, String detail){
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

    public String getTag1() {
        return tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public String getTag3() {
        return tag3;
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

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }
}
