package com.alha_app.mydictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

public class SearchResultsActivity extends AppCompatActivity {
    private MyDictionary myDictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        myDictionary = (MyDictionary) this.getApplication();

        Toolbar toolbar = findViewById(R.id.toolbar_search);
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

    private void prepareList(){
        List<Map<String, String>> listData = myDictionary.getSearchList();
        ListView searchList = findViewById(R.id.search_list);
        searchList.setAdapter(new SimpleAdapter(
                this,
                listData,
                R.layout.dictionary_list_item,
                new String[]{"list_title_text", "list_detail_text"},
                new int[]{R.id.list_title_text, R.id.list_detail_text}
        ));
    }
}