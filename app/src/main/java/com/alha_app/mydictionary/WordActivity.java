package com.alha_app.mydictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alha_app.mydictionary.database.AppDatabase;
import com.alha_app.mydictionary.database.DictionaryDao;
import com.alha_app.mydictionary.database.DictionaryEntity;
import com.alha_app.mydictionary.database.WordDao;
import com.alha_app.mydictionary.database.WordEntity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WordActivity extends AppCompatActivity {
    private final int MAX_TAG = 3;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler();
    private final Collator collator = Collator.getInstance(Locale.JAPANESE);
    private MyDictionary myDictionary;
    private boolean isEdit;
    private List<String> tags;
    private int choicePosition;
    private int tagCount;

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
        TextView tagText1 = findViewById(R.id.tag_text1);
        TextView tagText2 = findViewById(R.id.tag_text2);
        TextView tagText3 = findViewById(R.id.tag_text3);
        ImageButton deleteButton1 = findViewById(R.id.delete_button1);
        ImageButton deleteButton2 = findViewById(R.id.delete_button2);
        ImageButton deleteButton3 = findViewById(R.id.delete_button3);
        wordText.setText(myDictionary.getWord());
        kanaText.setText(myDictionary.getWordKana());
        detailText.setText(myDictionary.getWordDetail());

        if(!myDictionary.getTag1().equals("")) {
            tagText1.setText(myDictionary.getTag1());
            tagCount++;
        }
        if(!myDictionary.getTag2().equals("")) {
            tagText2.setText(myDictionary.getTag2());
            tagCount++;
        }
        if(!myDictionary.getTag3().equals("")) {
            tagText3.setText(myDictionary.getTag3());
            tagCount++;
        }

        Button button = findViewById(R.id.tag_button);
        button.setOnClickListener(v -> {
            if(!isEdit) return;
            if(tagCount >= MAX_TAG) {
                Toast.makeText(myDictionary, "タグは3つまでしか追加できません", Toast.LENGTH_SHORT).show();
                return;
            }

            // タグを編集するダイアログ
            new AlertDialog.Builder(this)
                    .setTitle("タグ")
                    .setSingleChoiceItems(tags.stream().toArray(String[]::new), 0, (dialog12, which) -> {
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
                                    Collections.sort(tags, collator);

                                    switch (tagCount){
                                        case 0:
                                            tagText1.setText(editText.getText().toString());
                                            deleteButton1.setVisibility(View.VISIBLE);
                                            break;
                                        case 1:
                                            tagText2.setText(editText.getText().toString());
                                            deleteButton2.setVisibility(View.VISIBLE);
                                            break;
                                        case 2:
                                            tagText3.setText(editText.getText().toString());
                                            deleteButton3.setVisibility(View.VISIBLE);
                                            break;
                                    }
                                    tagCount++;

                                    myDictionary.setTags(tags);
                                })
                                .setCancelable(false)
                                .show();
                    })
                    .setNegativeButton("キャンセル", null)
                    .setPositiveButton("OK", (dialog, which) -> {
                        if(tags.get(choicePosition).equals(tagText1.getText().toString()) || tags.get(choicePosition).equals(tagText2.getText().toString())
                                || tags.get(choicePosition).equals(tagText3.getText().toString())){
                            Toast.makeText(myDictionary, "すでについているタグです", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        switch (tagCount){
                            case 0:
                                tagText1.setText(tags.get(choicePosition));
                                deleteButton1.setVisibility(View.VISIBLE);
                                break;
                            case 1:
                                tagText2.setText(tags.get(choicePosition));
                                deleteButton2.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                tagText3.setText(tags.get(choicePosition));
                                deleteButton3.setVisibility(View.VISIBLE);
                                break;
                        }
                        tagCount++;
                    })
                    .setCancelable(false)
                    .show();
        });

        deleteButton1.setOnClickListener(v -> {
            tagText1.setText(tagText2.getText().toString());
            tagText2.setText(tagText3.getText().toString());
            tagText3.setText("");

            switch (tagCount){
                case 1:
                    deleteButton1.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    deleteButton2.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    deleteButton3.setVisibility(View.INVISIBLE);
                    break;
            }
            tagCount--;
        });

        deleteButton2.setOnClickListener(v -> {
            tagText2.setText(tagText3.getText().toString());
            tagText3.setText("");

            switch (tagCount){
                case 2:
                    deleteButton2.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    deleteButton3.setVisibility(View.INVISIBLE);
                    break;
            }
            tagCount--;
        });

        deleteButton3.setOnClickListener(v -> {
            tagText3.setText("");
            deleteButton3.setVisibility(View.INVISIBLE);
            tagCount--;
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
            TextView tagText1 = findViewById(R.id.tag_text1);
            TextView tagText2 = findViewById(R.id.tag_text2);
            TextView tagText3 = findViewById(R.id.tag_text3);
            ImageButton deleteButton1 = findViewById(R.id.delete_button1);
            ImageButton deleteButton2 = findViewById(R.id.delete_button2);
            ImageButton deleteButton3 = findViewById(R.id.delete_button3);
            Button tagButton = findViewById(R.id.tag_button);

            if(isEdit) {
                isEdit = false;
                item.setIcon(R.drawable.ic_edit);

                wordText.setEnabled(false);
                kanaText.setEnabled(false);
                detailText.setEnabled(false);

                deleteButton1.setVisibility(View.INVISIBLE);
                deleteButton2.setVisibility(View.INVISIBLE);
                deleteButton3.setVisibility(View.INVISIBLE);

                wordText.setBackgroundColor(Color.parseColor("#00000000"));
                kanaText.setBackgroundColor(Color.parseColor("#00000000"));
                detailText.setBackgroundColor(Color.parseColor("#00000000"));
                tagButton.setTextColor(Color.parseColor("#dddddd"));

                if(wordText.getText().toString().equals(myDictionary.getWord()) && kanaText.getText().toString().equals(myDictionary.getWordKana())
                    && detailText.getText().toString().equals(myDictionary.getWordDetail()) && tagText1.getText().toString().equals(myDictionary.getTag1())
                    && tagText2.getText().toString().equals(myDictionary.getTag2()) && tagText3.getText().toString().equals(myDictionary.getTag3())){
                    return false;
                }

                myDictionary.setWord(wordText.getText().toString());
                myDictionary.setWordKana(kanaText.getText().toString());
                myDictionary.setWordDetail(detailText.getText().toString());
                myDictionary.setTag1(tagText1.getText().toString());
                myDictionary.setTag2(tagText2.getText().toString());
                myDictionary.setTag3(tagText3.getText().toString());

                WordEntity entity = new WordEntity(myDictionary.getId(), wordText.getText().toString(),
                        kanaText.getText().toString(), detailText.getText().toString());

                if(!tagText1.getText().toString().equals("")){
                    entity.setTag1(tagText1.getText().toString().trim());
                }
                if(!tagText2.getText().toString().equals("")){
                    entity.setTag2(tagText2.getText().toString().trim());
                }
                if(!tagText3.getText().toString().equals("")){
                    entity.setTag3(tagText3.getText().toString().trim());
                }
                updateDB(entity);
            } else {
                isEdit = true;
                item.setIcon(R.drawable.ic_check);

                wordText.setEnabled(true);
                kanaText.setEnabled(true);
                detailText.setEnabled(true);

                switch (tagCount){
                    case 1:
                        deleteButton1.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        deleteButton1.setVisibility(View.VISIBLE);
                        deleteButton2.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        deleteButton1.setVisibility(View.VISIBLE);
                        deleteButton2.setVisibility(View.VISIBLE);
                        deleteButton3.setVisibility(View.VISIBLE);
                }

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
            dao.update(myDictionary.getWordId(), entity.getWord(), entity.getKana(), entity.getDetail(), entity.getTag1(), entity.getTag2(), entity.getTag3());
            updateDictionaryTime();

            List<WordEntity> wordList = myDictionary.getWordList();
            for(int i = 0; i < wordList.size(); i++){
                if(wordList.get(i).getId() == myDictionary.getWordId()){
                    wordList.set(i, entity);
                    break;
                }
            }
            myDictionary.setWordList(wordList);

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