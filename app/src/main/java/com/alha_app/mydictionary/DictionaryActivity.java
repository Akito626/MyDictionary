package com.alha_app.mydictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.alha_app.mydictionary.database.AppDatabase;
import com.alha_app.mydictionary.database.WordDao;
import com.alha_app.mydictionary.database.WordEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DictionaryActivity extends AppCompatActivity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler();
    private MyDictionary myDictionary;
    private List<WordEntity> wordList = new ArrayList<>();
    private List<Map<String, Object>> listData = new ArrayList<>();
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        myDictionary = (MyDictionary) this.getApplication();

        Toolbar toolbar = findViewById(R.id.toolbar_dictionary);
        toolbar.setTitle(myDictionary.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView wordListText = findViewById(R.id.word_list_text);
        TextView indexText = findViewById(R.id.index_text);
        TextView tagText = findViewById(R.id.tag_text);
        ListView wordList = findViewById(R.id.word_list);
        ScrollView scrollView = findViewById(R.id.index_scroll_view);

        wordListText.setOnClickListener(v -> {
            wordListText.setBackgroundColor(Color.parseColor("#dddddd"));
            indexText.setBackgroundColor(Color.parseColor("#00000000"));
            tagText.setBackgroundColor(Color.parseColor("#00000000"));

            wordList.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.INVISIBLE);
        });
        indexText.setOnClickListener(v -> {
            wordListText.setBackgroundColor(Color.parseColor("#00000000"));
            indexText.setBackgroundColor(Color.parseColor("#dddddd"));
            tagText.setBackgroundColor(Color.parseColor("#00000000"));

            wordList.setVisibility(View.INVISIBLE);
            scrollView.setVisibility(View.VISIBLE);
        });

        tagText.setOnClickListener(v -> {
            wordListText.setBackgroundColor(Color.parseColor("#00000000"));
            indexText.setBackgroundColor(Color.parseColor("#00000000"));
            tagText.setBackgroundColor(Color.parseColor("#dddddd"));

            wordList.setVisibility(View.INVISIBLE);
            scrollView.setVisibility(View.INVISIBLE);
        });

        // 索引のボタン全てにlistenerをセット
        View.OnClickListener listener = v -> {
            Button button = (Button) v;
            myDictionary.setIndexString(button.getText().toString());
        };

        findViewById(R.id.button1).setOnClickListener(listener);
        findViewById(R.id.button2).setOnClickListener(listener);
        findViewById(R.id.button3).setOnClickListener(listener);
        findViewById(R.id.button4).setOnClickListener(listener);
        findViewById(R.id.button5).setOnClickListener(listener);
        findViewById(R.id.button6).setOnClickListener(listener);
        findViewById(R.id.button7).setOnClickListener(listener);
        findViewById(R.id.button8).setOnClickListener(listener);
        findViewById(R.id.button9).setOnClickListener(listener);
        findViewById(R.id.button10).setOnClickListener(listener);
        findViewById(R.id.button11).setOnClickListener(listener);
        findViewById(R.id.button12).setOnClickListener(listener);
        findViewById(R.id.button13).setOnClickListener(listener);
        findViewById(R.id.button14).setOnClickListener(listener);
        findViewById(R.id.button15).setOnClickListener(listener);
        findViewById(R.id.button16).setOnClickListener(listener);
        findViewById(R.id.button17).setOnClickListener(listener);
        findViewById(R.id.button18).setOnClickListener(listener);
        findViewById(R.id.button19).setOnClickListener(listener);
        findViewById(R.id.button20).setOnClickListener(listener);
        findViewById(R.id.button21).setOnClickListener(listener);
        findViewById(R.id.button22).setOnClickListener(listener);
        findViewById(R.id.button23).setOnClickListener(listener);
        findViewById(R.id.button24).setOnClickListener(listener);
        findViewById(R.id.button25).setOnClickListener(listener);
        findViewById(R.id.button26).setOnClickListener(listener);
        findViewById(R.id.button27).setOnClickListener(listener);
        findViewById(R.id.button28).setOnClickListener(listener);
        findViewById(R.id.button29).setOnClickListener(listener);
        findViewById(R.id.button30).setOnClickListener(listener);
        findViewById(R.id.button31).setOnClickListener(listener);
        findViewById(R.id.button32).setOnClickListener(listener);
        findViewById(R.id.button33).setOnClickListener(listener);
        findViewById(R.id.button34).setOnClickListener(listener);
        findViewById(R.id.button35).setOnClickListener(listener);
        findViewById(R.id.button36).setOnClickListener(listener);
        findViewById(R.id.button37).setOnClickListener(listener);
        findViewById(R.id.button38).setOnClickListener(listener);
        findViewById(R.id.button39).setOnClickListener(listener);
        findViewById(R.id.button40).setOnClickListener(listener);
        findViewById(R.id.button41).setOnClickListener(listener);
        findViewById(R.id.button42).setOnClickListener(listener);
        findViewById(R.id.button43).setOnClickListener(listener);
        findViewById(R.id.button44).setOnClickListener(listener);
        findViewById(R.id.button45).setOnClickListener(listener);
        findViewById(R.id.button46).setOnClickListener(listener);

        loadDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.action_add_word) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.add_word_dialog);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(false);

            Button tagButton = dialog.findViewById(R.id.tag_button);
            tagButton.setOnClickListener(v -> {
            });

            Button cancelButton = dialog.findViewById(R.id.cancel_button);
            cancelButton.setOnClickListener(v -> dialog.dismiss());

            Button addButton = dialog.findViewById(R.id.add_button);
            addButton.setOnClickListener(v -> {
                TextView wordText = dialog.findViewById(R.id.word_text);
                if (wordText.getText().toString().equals("")) {
                    Toast.makeText(myDictionary, "単語を入力してください", Toast.LENGTH_SHORT).show();
                    return;
                }
                TextView kanaText = dialog.findViewById(R.id.kana_text);
                if (wordText.getText().toString().equals("")) {
                    Toast.makeText(myDictionary, "読みを入力してください", Toast.LENGTH_SHORT).show();
                    return;
                }
                TextView detailText = dialog.findViewById(R.id.detail_text);
                TextView tagText = dialog.findViewById(R.id.tag_text);
                String id = String.valueOf((char) ('A' + myDictionary.getId()));
                id += wordList.size();
                WordEntity entity = new WordEntity(id, myDictionary.getId(), wordText.getText().toString(),
                        kanaText.getText().toString(), detailText.getText().toString());
                if (tagText.getText().toString().equals("")) {
                    entity.setTag(tagText.getText().toString());
                }

                wordList.add(entity);

                Map<String, Object> listItem = new HashMap<>();
                listItem.put("list_title_text", entity.getWord());
                listItem.put("list_detail_text", entity.getDetail());
                listData.add(listItem);
                adapter.notifyDataSetChanged();

                saveDB(entity);
                dialog.dismiss();
            });

            dialog.show();
        } else if (item.getItemId() == R.id.action_information) {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dictionary_information_dialog);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(false);

            TextView titleText = dialog.findViewById(R.id.information_title_text);
            titleText.setText(myDictionary.getTitle());

            TextView detailText = dialog.findViewById(R.id.information_detail_text);
            detailText.setText(myDictionary.getDetail());

            Button button = dialog.findViewById(R.id.close_button);
            button.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        }
        return true;
    }

    private void prepareList() {
        listData.clear();
        for (int i = 0; i < wordList.size(); i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("list_title_text", wordList.get(i).getWord());
            item.put("list_detail_text", wordList.get(i).getDetail());
            listData.add(item);
        }

        ListView listView = findViewById(R.id.word_list);
        adapter = new SimpleAdapter(
                this,
                listData,
                R.layout.dictionary_list_item,
                new String[]{"list_title_text", "list_detail_text"},
                new int[]{R.id.list_title_text, R.id.list_detail_text}
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            myDictionary.setWord(wordList.get(position).getWord());
            myDictionary.setWordKana(wordList.get(position).getKana());
            myDictionary.setWordDetail(wordList.get(position).getDetail());

            startActivity(new Intent(getApplication(), WordActivity.class));
        });
    }

    private void saveDB(WordEntity entity) {
        executor.execute(() -> {
            AppDatabase db = Room.databaseBuilder(getApplication(),
                    AppDatabase.class, "WORD_DATA").build();
            WordDao dao = db.wordDao();
            dao.insert(entity);

            handler.post(() -> {
                Toast.makeText(myDictionary, "登録しました", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void loadDB() {
        executor.execute(() -> {
            AppDatabase db = Room.databaseBuilder(getApplication(),
                    AppDatabase.class, "WORD_DATA").build();
            WordDao dao = db.wordDao();
            wordList = dao.getAll(myDictionary.getId());
        });

        handler.post(() -> prepareList());
    }
}