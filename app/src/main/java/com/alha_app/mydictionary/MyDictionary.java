package com.alha_app.mydictionary;

import android.app.Application;

import com.alha_app.mydictionary.database.WordEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyDictionary extends Application {
    // 辞書データ
    private int id;
    private String title;
    private String detail;

    // 単語データ
    private int wordId;
    private String word;
    private String wordKana;
    private String wordDetail;
    private String tag1;
    private String tag2;
    private String tag3;
    private List<WordEntity> wordList = new ArrayList<>();
    private List<String> tags;

    // 検索データ
    private String searchString;
    private List<Map<String, Object>> searchList;

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

    public int getWordId() {
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

    public String getTag1() {
        return tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public String getTag3() {
        return tag3;
    }

    public List<WordEntity> getWordList() {
        return wordList;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getSearchString() {
        return searchString;
    }

    public List<Map<String, Object>> getSearchList() {
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

    public void setWordId(int wordId) {
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

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    public void setTag3(String tag3) {
        this.tag3 = tag3;
    }

    public void setWordList(List<WordEntity> wordList) {
        this.wordList = wordList;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public void setSearchList(List<Map<String, Object>> searchList) {
        this.searchList = searchList;
    }

    public void addWord(WordEntity entity){
        wordList.add(entity);
    }

    public void removeWord(int position){
        wordList.remove(position);
    }
}
