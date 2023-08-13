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
import android.widget.LinearLayout;
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

public class DictionaryActivity extends AppCompatActivity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler();
    private Comparator<WordEntity> japaneseComparator;
    private MyDictionary myDictionary;
    private List<WordEntity> wordList = new ArrayList<>();
    private List<Map<String, Object>> listData = new ArrayList<>();
    private List<Map<String, String>> indexListData = new ArrayList<>();
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
        ListView wordListView = findViewById(R.id.word_list);
        ScrollView indexScrollView = findViewById(R.id.index_scroll_view);
        LinearLayout indexLayout = findViewById(R.id.index_layout);
        ListView indexList = findViewById(R.id.index_list);

        // tabを作成
        wordListText.setOnClickListener(v -> {
            wordListText.setBackgroundColor(Color.parseColor("#dddddd"));
            indexText.setBackgroundColor(Color.parseColor("#00000000"));
            tagText.setBackgroundColor(Color.parseColor("#00000000"));

            wordListView.setVisibility(View.VISIBLE);
            indexScrollView.setVisibility(View.INVISIBLE);
            indexLayout.setVisibility(View.INVISIBLE);
        });
        indexText.setOnClickListener(v -> {
            wordListText.setBackgroundColor(Color.parseColor("#00000000"));
            indexText.setBackgroundColor(Color.parseColor("#dddddd"));
            tagText.setBackgroundColor(Color.parseColor("#00000000"));

            wordListView.setVisibility(View.INVISIBLE);
            indexScrollView.setVisibility(View.VISIBLE);
        });

        tagText.setOnClickListener(v -> {
            wordListText.setBackgroundColor(Color.parseColor("#00000000"));
            indexText.setBackgroundColor(Color.parseColor("#00000000"));
            tagText.setBackgroundColor(Color.parseColor("#dddddd"));

            wordListView.setVisibility(View.INVISIBLE);
            indexScrollView.setVisibility(View.INVISIBLE);
            indexLayout.setVisibility(View.INVISIBLE);
        });

        // 索引のボタン全てにlistenerをセット
        View.OnClickListener listener = v -> {
            Button button = (Button) v;

            indexListData.clear();
            for(int i = 0; i < wordList.size(); i++){
                if(wordList.get(i).getKana().startsWith(button.getText().toString())) {
                    Map<String, String> item = new HashMap<>();
                    item.put("list_title_text", wordList.get(i).getWord());
                    item.put("list_detail_text", wordList.get(i).getDetail());
                    item.put("id", wordList.get(i).getId());
                    item.put("kana", wordList.get(i).getKana());
                    indexListData.add(item);
                }
            }

            indexList.setAdapter(new SimpleAdapter(
                    this,
                    indexListData,
                    R.layout.dictionary_list_item,
                    new String[]{"list_title_text", "list_detail_text"},
                    new int[]{R.id.list_title_text, R.id.list_detail_text}
            ));

            indexList.setOnItemClickListener((parent, view, position, id) -> {
                myDictionary.setWordId(indexListData.get(position).get("id"));
                myDictionary.setWord(indexListData.get(position).get("list_title_text"));
                myDictionary.setWordKana(indexListData.get(position).get("kana"));
                myDictionary.setWordDetail(indexListData.get(position).get("list_detail_text"));

                startActivity(new Intent(getApplication(), WordActivity.class));
            });

            indexScrollView.setVisibility(View.INVISIBLE);
            indexLayout.setVisibility(View.VISIBLE);
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

        findViewById(R.id.index_button).setOnClickListener(v -> {
            indexLayout.setVisibility(View.INVISIBLE);
            indexScrollView.setVisibility(View.VISIBLE);
        });

        // 五十音順にソートするComparator
        japaneseComparator = (w1, w2) -> {
            Collator collator = Collator.getInstance(Locale.JAPANESE);
            return collator.compare(w1.getKana(), w2.getKana());
        };

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

    @Override
    public void onResume() {
        super.onResume();

        loadDB();
    }

    private void prepareList() {
        listData.clear();
        Collections.sort(wordList, japaneseComparator);
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
            myDictionary.setWordId(wordList.get(position).getId());
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
                Collections.sort(wordList, japaneseComparator);
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