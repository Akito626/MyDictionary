package com.alha_app.mydictionary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WordListFragment extends Fragment {
    private final Handler handler = new Handler();
    private final Collator collator = Collator.getInstance(Locale.JAPANESE);
    private DictionaryActivity activity;
    private MyDictionary myDictionary;
    private Comparator<WordEntity> japaneseComparator;

    // 閲覧専用リスト（操作はapplicationクラスで行う）
    private List<WordEntity> wordList = new ArrayList<>();
    private List<Map<String, Object>> listData = new ArrayList<>();

    // 単語リストのアダプター
    private SimpleAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_word_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (DictionaryActivity) getActivity();
        myDictionary = (MyDictionary) activity.getApplication();

        wordList = myDictionary.getWordList();

        // 検索バーの設定
        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                prepareSearchList();
                return false;
            }
        });

        // 五十音順にソートするComparator
        japaneseComparator = (w1, w2) -> collator.compare(w1.getWord(), w2.getWord());

        prepareList();
    }

    @Override
    public void onResume(){
        super.onResume();

        wordList = myDictionary.getWordList();
        prepareSearchList();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info) {
        super.onCreateContextMenu(menu, view, info);
        activity.getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if (item.getItemId() == R.id.context_delete) {
            deleteWord(info.position);
        }

        return true;
    }

    private void prepareList() {
        listData.clear();
        for (WordEntity entity : wordList) {
            Map<String, Object> item = new HashMap<>();
            item.put("list_title_text", entity.getWord());
            item.put("list_detail_text", entity.getDetail());
            item.put("id", entity.getId());
            item.put("kana", entity.getKana());
            item.put("tag", entity.getTag());
            listData.add(item);
        }

        ListView listView = getView().findViewById(R.id.word_list);
        adapter = new SimpleAdapter(
                activity,
                listData,
                R.layout.dictionary_list_item,
                new String[]{"list_title_text", "list_detail_text"},
                new int[]{R.id.list_title_text, R.id.list_detail_text}
        );
        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            myDictionary.setWordId(Integer.parseInt(listData.get(position).get("id").toString()));
            myDictionary.setWord(listData.get(position).get("list_title_text").toString());
            myDictionary.setWordKana(listData.get(position).get("kana").toString());
            myDictionary.setWordDetail(listData.get(position).get("list_detail_text").toString());
            myDictionary.setTag(listData.get(position).get("tag").toString());

            startActivity(new Intent(myDictionary, WordActivity.class));
        });

        registerForContextMenu(listView);
    }

    private void prepareSearchList() {
        Collections.sort(wordList, japaneseComparator);
        // 検索バーが空だったら通常のリストを準備
        SearchView searchView = getView().findViewById(R.id.search_view);
        String newText = searchView.getQuery().toString();
        if (newText.equals("")) {
            prepareList();
            return;
        }
        listData.clear();
        for (WordEntity entity : wordList) {
            if (entity.getWord().contains(newText) || entity.getKana().contains(newText)) {
                Map<String, Object> item = new HashMap<>();
                item.put("list_title_text", entity.getWord());
                item.put("list_detail_text", entity.getDetail());
                item.put("id", entity.getId());
                item.put("kana", entity.getKana());
                item.put("tag", entity.getTag());
                listData.add(item);
            }
        }
        adapter.notifyDataSetChanged();
    }

    public void addWord(@NonNull WordEntity entity){
        Collections.sort(wordList, japaneseComparator);

        Map<String, Object> item = new HashMap<>();
        item.put("list_title_text", entity.getWord());
        item.put("list_detail_text", entity.getDetail());
        item.put("id", entity.getId());
        item.put("kana", entity.getKana());
        item.put("tag", entity.getTag());
        listData.add(item);

        handler.post(() -> adapter.notifyDataSetChanged());
    }

    public void deleteWord(int position){
        int id = wordList.get(position).getId();
        myDictionary.removeWord(position);
        listData.remove(position);
        adapter.notifyDataSetChanged();
        activity.deleteWord(id);
    }
}
