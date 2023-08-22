package com.alha_app.mydictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.alha_app.mydictionary.database.AppDatabase;
import com.alha_app.mydictionary.database.DictionaryDao;
import com.alha_app.mydictionary.database.WordDao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchResultsActivity extends AppCompatActivity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler();
    private MyDictionary myDictionary;
    private List<Map<String, Object>> listData;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        myDictionary = (MyDictionary) this.getApplication();

        Toolbar toolbar = findViewById(R.id.toolbar_search);
        toolbar.setTitle("「" + myDictionary.getSearchString() + "」の単語");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prepareList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info) {
        super.onCreateContextMenu(menu, view, info);
        getMenuInflater().inflate(R.menu.menu_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        if(item.getItemId() == R.id.context_delete){
            deleteWord(Integer.parseInt(listData.get(info.position).get("id").toString()), info.position);
        }

        return true;
    }

    private void prepareList(){
        listData = myDictionary.getSearchList();
        ListView searchList = findViewById(R.id.search_list);
        adapter = new SimpleAdapter(
                this,
                listData,
                R.layout.dictionary_list_item,
                new String[]{"list_title_text", "list_detail_text"},
                new int[]{R.id.list_title_text, R.id.list_detail_text}
        );
        searchList.setAdapter(adapter);

        searchList.setOnItemClickListener((parent, view, position, id) -> {
            myDictionary.setWordId(Integer.parseInt(listData.get(position).get("id").toString()));
            myDictionary.setWord(listData.get(position).get("list_title_text").toString());
            myDictionary.setWordKana(listData.get(position).get("kana").toString());
            myDictionary.setWordDetail(listData.get(position).get("list_detail_text").toString());
            myDictionary.setTag1(listData.get(position).get("tag1").toString());
            myDictionary.setTag2(listData.get(position).get("tag2").toString());
            myDictionary.setTag3(listData.get(position).get("tag3").toString());

            startActivity(new Intent(getApplication(), WordActivity.class));
        });

        registerForContextMenu(searchList);
    }

    private void deleteWord(int id, int position){
        executor.execute(() -> {
            AppDatabase db = Room.databaseBuilder(getApplication(),
                    AppDatabase.class, "WORD_DATA").build();
            WordDao dao = db.wordDao();
            dao.delete(id);

            listData.remove(position);
            updateDictionaryTime();

            handler.post(() -> adapter.notifyDataSetChanged());
        });
    }

    private void updateDictionaryTime(){
        executor.execute(() -> {
            AppDatabase db = Room.databaseBuilder(getApplication(),
                    AppDatabase.class, "DICTIONARY_DATA").build();
            DictionaryDao dao = db.dictionaryDao();
            dao.update(myDictionary.getId(), myDictionary.getTitle(), myDictionary.getDetail(), System.currentTimeMillis());
        });
    }
}