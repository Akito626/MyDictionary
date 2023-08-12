package com.alha_app.mydictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alha_app.mydictionary.database.AppDatabase;
import com.alha_app.mydictionary.database.DictionaryDao;
import com.alha_app.mydictionary.database.DictionaryEntity;
import com.alha_app.mydictionary.database.WordDao;
import com.alha_app.mydictionary.database.WordEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WordActivity extends AppCompatActivity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler();
    private MyDictionary myDictionary;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        myDictionary = (MyDictionary) this.getApplication();

        Toolbar toolbar = findViewById(R.id.toolbar_word);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        isEdit = false;

        EditText wordText = findViewById(R.id.word_text);
        EditText kanaText = findViewById(R.id.kana_text);
        EditText detailText = findViewById(R.id.detail_text);
        wordText.setText(myDictionary.getWord());
        kanaText.setText(myDictionary.getWordKana());
        detailText.setText(myDictionary.getWordDetail());
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        getMenuInflater().inflate(R.menu.menu_word, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            if(!isEdit) {
                finish();
            }
        } else if(item.getItemId() == R.id.action_edit){
            EditText wordText = findViewById(R.id.word_text);
            EditText kanaText = findViewById(R.id.kana_text);
            EditText detailText = findViewById(R.id.detail_text);

            if(isEdit) {
                isEdit = false;
                item.setIcon(R.drawable.ic_edit);

                wordText.setEnabled(false);
                kanaText.setEnabled(false);
                detailText.setEnabled(false);

                WordEntity entity = new WordEntity(myDictionary.getWordId(), myDictionary.getId(),
                        wordText.getText().toString(), kanaText.getText().toString(), detailText.getText().toString());
                saveDB(entity);
            } else {
                isEdit = true;
                item.setIcon(R.drawable.ic_check);

                wordText.setEnabled(true);
                kanaText.setEnabled(true);
                detailText.setEnabled(true);
            }
        }
        return true;
    }

    private void saveDB(WordEntity entity) {
        executor.execute(() -> {
            AppDatabase db = Room.databaseBuilder(getApplication(),
                    AppDatabase.class, "WORD_DATA").build();
            WordDao dao = db.wordDao();
            dao.delete(myDictionary.getWordId());
            dao.insert(entity);

            handler.post(() -> Toast.makeText(myDictionary, "保存しました", Toast.LENGTH_SHORT).show());
        });
    }
}