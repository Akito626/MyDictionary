package com.alha_app.mydictionary;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyDictionary extends Application {
    // 辞書データ
    private int id;
    private String title;
    private String detail;

    // 単語データ
    private String wordId;
    private String word;
    private String wordKana;
    private String wordDetail;

    // 検索データ
    private String searchString;
    private List<Map<String, String>> searchList = new ArrayList<>();

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

    public String getSearchString() {
        return searchString;
    }

    public List<Map<String, String>> getSearchList() {
        return searchList;
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

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public void setSearchList(List<Map<String, String>> searchList) {
        this.searchList = searchList;
    }
}
