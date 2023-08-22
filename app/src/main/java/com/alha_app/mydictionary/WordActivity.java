package com.alha_app.mydictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alha_app.mydictionary.database.AppDatabase;
import com.alha_app.mydictionary.database.DictionaryDao;
import com.alha_app.mydictionary.database.DictionaryEntity;
import com.alha_app.mydictionary.database.WordDao;
import com.alha_app.mydictionary.database.WordEntity;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WordActivity extends AppCompatActivity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler();
    private MyDictionary myDictionary;
    private boolean isEdit;
    private List<String> tags;
    private int choicePosition;

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
        tags = myDictionary.getTags();

        // テキストをセット
        EditText wordText = findViewById(R.id.word_text);
        EditText kanaText = findViewById(R.id.kana_text);
        EditText detailText = findViewById(R.id.detail_text);
        TextView tagText = findViewById(R.id.tag_text);
        wordText.setText(myDictionary.getWord());
        kanaText.setText(myDictionary.getWordKana());
        detailText.setText(myDictionary.getWordDetail());
        tagText.setText(myDictionary.getTag());

        Button button = findViewById(R.id.tag_button);
        button.setOnClickListener(v -> {
            if(!isEdit) return;
            // タグを編集するダイアログ
            new AlertDialog.Builder(this)
                    .setTitle("タグ")
                    .setSingleChoiceItems(tags.stream().toArray(String[]::new), choicePosition, (dialog12, which) -> {
                        tagText.setText(tags.get(which));
                        choicePosition = which;
                    })
                    .setNeutralButton("新規タグ", (dialog1, which) -> {
                        EditText editText = new EditText(this);
                        editText.setBackgroundColor(Color.parseColor("#00000000"));
                        editText.setHint("タグ名");
                        editText.setGravity(Gravity.CENTER);
                        // 新しいタグを追加するダイアログ
                        new AlertDialog.Builder(this)
                                .setTitle("タグ名を入力")
                                .setView(editText)
                                .setNegativeButton("キャンセル", null)
                                .setPositiveButton("OK", (dialog2, which1) -> {
                                    if(tags.contains(editText.getText().toString())) {
                                        Toast.makeText(myDictionary, "既に存在するタグです", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    tags.add(editText.getText().toString());
                                    tagText.setText(editText.getText().toString());
                                })
                                .setCancelable(false)
                                .show();
                    })
                    .setPositiveButton("OK", null)
                    .setCancelable(false)
                    .show();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        getMenuInflater().inflate(R.menu.menu_word, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == android.R.id.home){
            if(!isEdit) {
                finish();
            }
        } else if(item.getItemId() == R.id.action_edit){
            EditText wordText = findViewById(R.id.word_text);
            EditText kanaText = findViewById(R.id.kana_text);
            EditText detailText = findViewById(R.id.detail_text);
            TextView tagText = findViewById(R.id.tag_text);
            Button tagButton = findViewById(R.id.tag_button);

            if(isEdit) {
                isEdit = false;
                item.setIcon(R.drawable.ic_edit);

                wordText.setEnabled(false);
                kanaText.setEnabled(false);
                detailText.setEnabled(false);

                wordText.setBackgroundColor(Color.parseColor("#00000000"));
                kanaText.setBackgroundColor(Color.parseColor("#00000000"));
                detailText.setBackgroundColor(Color.parseColor("#00000000"));
                tagButton.setTextColor(Color.parseColor("#dddddd"));

                if(wordText.getText().toString().equals(myDictionary.getWord()) && kanaText.getText().toString().equals(myDictionary.getWordKana())
                    && detailText.getText().toString().equals(myDictionary.getWordDetail()) && tagText.getText().toString().equals(myDictionary.getTag())){
                    return false;
                }

                myDictionary.setWord(wordText.getText().toString());
                myDictionary.setWordKana(kanaText.getText().toString());
                myDictionary.setWordDetail(detailText.getText().toString());
                myDictionary.setTag(tagText.getText().toString());

                WordEntity entity = new WordEntity(myDictionary.getId(), wordText.getText().toString(),
                        kanaText.getText().toString(), detailText.getText().toString(), tagText.getText().toString());
                updateDB(entity);
            } else {
                isEdit = true;
                item.setIcon(R.drawable.ic_check);

                wordText.setEnabled(true);
                kanaText.setEnabled(true);
                detailText.setEnabled(true);

                wordText.setBackgroundColor(Color.parseColor("#dddddd"));
                kanaText.setBackgroundColor(Color.parseColor("#dddddd"));
                detailText.setBackgroundColor(Color.parseColor("#dddddd"));
                tagButton.setTextColor(Color.parseColor("#0000ff"));
            }
        } else if(item.getItemId() == R.id.action_home){
            Intent intent = new Intent(getApplication(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if(item.getItemId() == R.id.action_dictionary_home) {
            Intent intent = new Intent(getApplication(), DictionaryActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    private void updateDB(WordEntity entity) {
        executor.execute(() -> {
            AppDatabase db = Room.databaseBuilder(getApplication(),
                    AppDatabase.class, "WORD_DATA").build();
            WordDao dao = db.wordDao();
            dao.update(myDictionary.getWordId(), entity.getWord(), entity.getKana(), entity.getDetail(), entity.getTag());
            updateDictionaryTime();

            handler.post(() -> Toast.makeText(myDictionary, "保存しました", Toast.LENGTH_SHORT).show());
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