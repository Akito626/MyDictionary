package com.alha_app.mydictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alha_app.mydictionary.database.AppDatabase;
import com.alha_app.mydictionary.database.DictionaryDao;
import com.alha_app.mydictionary.database.DictionaryEntity;
import com.alha_app.mydictionary.database.WordDao;
import com.alha_app.mydictionary.database.WordEntity;
import com.alha_app.mydictionary.model.TabPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
    private final Collator collator = Collator.getInstance(Locale.JAPANESE);
    private static final String dakuon = "がぎぐげござじずぜぞだぢづでどばびぶべぼぱぴぷぺぽ";
    private static final String seion = "かきくけこさしすせそたちつてとはひふへほはひふへほ";
    private Comparator<WordEntity> japaneseComparator;
    private List<Fragment> fragmentList;
    private MyDictionary myDictionary;
    private ViewPager2 pager;
    private TabPagerAdapter tabAdapter;
    private List<String> tags = new ArrayList<>();

    // tagの位置を保存
    private int choicePosition;

    // 辞書情報を編集中かどうか
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        myDictionary = (MyDictionary) this.getApplication();
        tags.add("");

        Toolbar toolbar = findViewById(R.id.toolbar_dictionary);
        toolbar.setTitle(myDictionary.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pager = findViewById(R.id.pager);
        tabAdapter = new TabPagerAdapter(this);
        pager.setAdapter(tabAdapter);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, pager, ((tab, position) -> {
            if(position == 0){
                tab.setText("単語一覧");
            } else if(position == 1){
                tab.setText("日本語索引");
            } else if(position == 2){
                tab.setText("英語索引");
            } else if(position == 3){
                tab.setText("タグ");
            }
        })).attach();

        // 五十音順にソートするComparator
        japaneseComparator = (w1, w2) -> collator.compare(w1.getWord(), w2.getWord());

        loadDB();
    }
    @Override
    public void onResume() {
        super.onResume();

        loadDB();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        } else if (menuItem.getItemId() == R.id.action_add_word) {      // 追加ボタンの処理
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.add_word_dialog);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(false);

            choicePosition = 0;

            TextView tagText = dialog.findViewById(R.id.tag_text);

            Button tagButton = dialog.findViewById(R.id.tag_button);
            tagButton.setOnClickListener(v -> {
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
                                        Collections.sort(tags, collator);
                                        tagText.setText(editText.getText().toString());

                                        myDictionary.setTags(tags);
                                    })
                                    .setCancelable(false)
                                    .show();
                        })
                        .setPositiveButton("OK", null)
                        .setCancelable(false)
                        .show();
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
                TextView detailText = dialog.findViewById(R.id.detail_text);
                WordEntity entity = new WordEntity(myDictionary.getId(), wordText.getText().toString(),
                        kanaText.getText().toString(), detailText.getText().toString(), tagText.getText().toString().trim());

                saveDB(entity);
                dialog.dismiss();
            });

            dialog.show();
        } else if (menuItem.getItemId() == R.id.action_information) {
            isEdit = false;
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dictionary_information_dialog);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(false);

            EditText titleText = dialog.findViewById(R.id.information_title_text);
            titleText.setText(myDictionary.getTitle());

            EditText detailText = dialog.findViewById(R.id.information_detail_text);
            detailText.setText(myDictionary.getDetail());

            Button button = dialog.findViewById(R.id.close_button);
            button.setOnClickListener(v -> {
                if(!isEdit) dialog.dismiss();
            });

            ImageButton editButton = dialog.findViewById(R.id.edit_button);
            editButton.setOnClickListener(v -> {
                if(isEdit) {
                    isEdit = false;
                    titleText.setBackgroundColor(Color.parseColor("#00000000"));
                    detailText.setBackgroundColor(Color.parseColor("#00000000"));
                    titleText.setEnabled(false);
                    detailText.setEnabled(false);

                    if(titleText.getText().toString().equals(myDictionary.getTitle()) && detailText.getText().toString().equals(myDictionary.getDetail())){
                        return;
                    }

                    getSupportActionBar().setTitle(titleText.getText().toString());

                    myDictionary.setTitle(titleText.getText().toString());
                    myDictionary.setDetail(detailText.getText().toString());

                    DictionaryEntity entity = new DictionaryEntity(titleText.getText().toString(), detailText.getText().toString());
                    updateDictionary(entity);
                } else {
                    isEdit = true;
                    titleText.setBackgroundColor(Color.parseColor("#dddddd"));
                    detailText.setBackgroundColor(Color.parseColor("#dddddd"));
                    titleText.setEnabled(true);
                    detailText.setEnabled(true);
                }
            });

            dialog.show();
        }
        return true;
    }
    public void setFragmentList(List<Fragment> fragmentList){
        this.fragmentList = fragmentList;
    }

    public void saveDB(WordEntity entity) {
        executor.execute(() -> {
            AppDatabase db = Room.databaseBuilder(getApplication(),
                    AppDatabase.class, "WORD_DATA").build();
            WordDao dao = db.wordDao();
            int id = (int)dao.insert(entity);
            entity.setId(id);
            myDictionary.addWord(entity);

            if(pager.getCurrentItem() == 0){
                Fragment fragment = fragmentList.get(0);
                WordListFragment wordFragment = (WordListFragment) fragment;
                wordFragment.addWord(entity);
            }

            handler.post(() -> Toast.makeText(myDictionary, "登録しました", Toast.LENGTH_SHORT).show());
        });
    }
    public void loadDB() {
        executor.execute(() -> {
            AppDatabase db = Room.databaseBuilder(getApplication(),
                    AppDatabase.class, "WORD_DATA").build();
            WordDao dao = db.wordDao();
            List<WordEntity> wordList = dao.getAll(myDictionary.getId());
            Collections.sort(wordList, japaneseComparator);
            myDictionary.setWordList(wordList);

            for(WordEntity entity: wordList){
                if(!tags.contains(entity.getTag())){
                    tags.add(entity.getTag());
                }
            }

            Collections.sort(tags, collator);
            myDictionary.setTags(tags);
        });
    }

    public void deleteWord(int id){
        executor.execute(() -> {
            AppDatabase db = Room.databaseBuilder(getApplication(),
                    AppDatabase.class, "WORD_DATA").build();
            WordDao dao = db.wordDao();
            dao.delete(id);
        });
    }

    private void updateDictionary(DictionaryEntity entity){
        executor.execute(() -> {
            AppDatabase db = Room.databaseBuilder(getApplication(),
                    AppDatabase.class, "DICTIONARY_DATA").build();
            DictionaryDao dao = db.dictionaryDao();
            dao.update(myDictionary.getId(), entity.getTitle(), entity.getDetail());

            handler.post(() -> Toast.makeText(myDictionary, "保存しました", Toast.LENGTH_SHORT).show());
        });
    }
}