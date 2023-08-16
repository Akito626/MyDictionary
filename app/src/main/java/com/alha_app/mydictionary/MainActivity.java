package com.alha_app.mydictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Room;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.alha_app.mydictionary.database.AppDatabase;
import com.alha_app.mydictionary.database.DictionaryDao;
import com.alha_app.mydictionary.database.DictionaryEntity;
import com.alha_app.mydictionary.database.WordDao;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler();
    private List<Map<String, Object>> listData = new ArrayList<>();
    private SimpleAdapter adapter;
    private List<DictionaryEntity> dictionaryList = new ArrayList<>();
    private MyDictionary myDictionary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDictionary = (MyDictionary) this.getApplication();

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("ホーム");
        setSupportActionBar(toolbar);

        loadDB();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == R.id.action_add){
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.add_dictionary_dialog);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(false);

            EditText titleText = dialog.findViewById(R.id.title_text);
            EditText detailText = dialog.findViewById(R.id.detail_text);

            Button cancelButton = dialog.findViewById(R.id.cancel_button);
            cancelButton.setOnClickListener(v -> dialog.dismiss());

            Button addButton = dialog.findViewById(R.id.add_button);
            addButton.setOnClickListener(v -> {
                if(titleText.getText().toString().equals("")) return;
                DictionaryEntity entity = new DictionaryEntity(titleText.getText().toString(), detailText.getText().toString());

                saveDB(entity);
                dialog.dismiss();
            });
            dialog.show();
        } else if(item.getItemId() == R.id.action_license){
            OssLicensesMenuActivity.setActivityTitle("ライセンス");
            startActivity(new Intent(getApplication(), OssLicensesMenuActivity.class));
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
            deleteDictionary(dictionaryList.get(info.position).getId());
        }

        return true;
    }

    private void prepareList(){
        listData.clear();
        for(int i = 0; i < dictionaryList.size(); i++){
            Map<String, Object> item = new HashMap<>();
            item.put("list_title_text", dictionaryList.get(i).getTitle());
            item.put("list_detail_text", dictionaryList.get(i).getDetail());
            listData.add(item);
        }

        ListView listView = findViewById(R.id.list_dictionary);
        adapter = new SimpleAdapter(
                this,
                listData,
                R.layout.dictionary_list_item,
                new String[]{"list_title_text", "list_detail_text"},
                new int[] {R.id.list_title_text, R.id.list_detail_text}
        );
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            myDictionary.setId(dictionaryList.get(position).getId());
            myDictionary.setTitle(dictionaryList.get(position).getTitle());
            myDictionary.setDetail(dictionaryList.get(position).getDetail());
            startActivity(new Intent(getApplication(), DictionaryActivity.class));
        });

        registerForContextMenu(listView);
    }

    private void saveDB(DictionaryEntity entity){
        executor.execute(() -> {
            AppDatabase db = Room.databaseBuilder(getApplication(),
                    AppDatabase.class, "DICTIONARY_DATA").build();
            DictionaryDao dao = db.dictionaryDao();
            int id = (int)dao.insert(entity);

            entity.setId(id);
            myDictionary.setId(id);
            myDictionary.setTitle(entity.getTitle());
            myDictionary.setDetail(entity.getDetail());
            dictionaryList.add(entity);

            Map<String, Object> listItem = new HashMap<>();
            listItem.put("list_title_text", entity.getTitle());
            listItem.put("list_detail_text", entity.getDetail());
            listData.add(listItem);

            handler.post(() -> {
                adapter.notifyDataSetChanged();
                startActivity(new Intent(getApplication(), DictionaryActivity.class));
            });
        });
    }
    private void loadDB(){
        executor.execute(() -> {
            AppDatabase db = Room.databaseBuilder(getApplication(),
                    AppDatabase.class, "DICTIONARY_DATA").build();
            DictionaryDao dao = db.dictionaryDao();
            dictionaryList = dao.getAll();

            dictionaryList.sort(Comparator.comparing(DictionaryEntity::getId));
            for(DictionaryEntity entity: dictionaryList){
                System.out.println(entity.getId() + " " + entity.getTitle());
            }

            handler.post(() -> prepareList());
        });
    }

    private void deleteDictionary(int id){
        executor.execute(() -> {
            AppDatabase dictionaryDB = Room.databaseBuilder(getApplication(),
                    AppDatabase.class, "DICTIONARY_DATA").build();
            AppDatabase wordDB = Room.databaseBuilder(getApplication(),
                    AppDatabase.class, "WORD_DATA").build();
            DictionaryDao dictionaryDao = dictionaryDB.dictionaryDao();
            WordDao wordDao = wordDB.wordDao();

            wordDao.deleteAll(id);
            dictionaryDao.delete(id);

            loadDB();
        });
    }
}