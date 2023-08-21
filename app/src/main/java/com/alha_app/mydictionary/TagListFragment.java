package com.alha_app.mydictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.alha_app.mydictionary.database.WordEntity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.FileHandler;

public class TagListFragment extends Fragment {
    private DictionaryActivity activity;
    private MyDictionary myDictionary;
    private List<WordEntity> wordList = new ArrayList<>();
    private ListView tagList;
    private List<String> tags;
    private List<String> listData = new ArrayList<>();
    private ArrayAdapter<String> tagsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tag_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (DictionaryActivity) getActivity();
        myDictionary = (MyDictionary) activity.getApplication();

        wordList = myDictionary.getWordList();
        tags = myDictionary.getTags();

        tagList = view.findViewById(R.id.tag_list);
        tagList.setTextFilterEnabled(true);

        // 検索バーの設定
        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    prepareList();
                } else {
                    listData.clear();
                    for(String s: tags){
                        if(s.contains(newText)){
                            listData.add(s);
                        }
                    }
                    tagsAdapter.notifyDataSetChanged();
                }
                return false;
            }
        });

        prepareList();
    }

    @Override
    public void onResume(){
        super.onResume();

        wordList = myDictionary.getWordList();
        tags = myDictionary.getTags();
        prepareList();
    }

    private void prepareList() {
        listData.clear();

        for(String s: tags){
            listData.add(s);
        }
        // タグリストを準備
        ListView tagsList = getView().findViewById(R.id.tag_list);
        tagsAdapter = new ArrayAdapter<>(
                activity,
                android.R.layout.simple_list_item_1,
                listData
        );
        tagsList.setAdapter(tagsAdapter);

        tagsList.setOnItemClickListener((parent, view, position, id) -> {
            String tag = listData.get(position);

            List<Map<String, Object>> searchListData = new ArrayList<>();
            for(WordEntity entity: wordList){
                if(entity.getTag().equals(tag)) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("list_title_text", entity.getWord());
                    item.put("list_detail_text", entity.getDetail());
                    item.put("id", entity.getId());
                    item.put("kana", entity.getKana());
                    item.put("tag", entity.getTag());
                    searchListData.add(item);
                }
            }
            myDictionary.setSearchString(tag);
            myDictionary.setSearchList(searchListData);

            startActivity(new Intent(myDictionary, SearchResultsActivity.class));
        });
    }
}
