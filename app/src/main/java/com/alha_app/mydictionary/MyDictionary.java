package com.alha_app.mydictionary;

import android.app.Application;

public class MyDictionary extends Application {
    private int id;
    private String title;
    private String detail;
    private String wordId;
    private String word;
    private String wordKana;
    private String wordDetail;
    private String indexString;

    // Getter
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getWordId() {
        return wordId;
    }

    public String getWord() {
        return word;
    }

    public String getWordKana() {
        return wordKana;
    }

    public String getWordDetail() {
        return wordDetail;
    }

    public String getIndexString() {
        return indexString;
    }

    // Setter
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setWordKana(String wordKana) {
        this.wordKana = wordKana;
    }

    public void setWordDetail(String wordDetail) {
        this.wordDetail = wordDetail;
    }

    public void setIndexString(String indexString) {
        this.indexString = indexString;
    }
}
