package com.alha_app.mydictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.oss.licenses.OssLicensesActivity;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

public class DictionaryActivity extends AppCompatActivity {
    private MyDictionary myDictionary;

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
        wordListText.setOnClickListener(v -> {
            wordListText.setBackgroundColor(Color.parseColor("#dddddd"));
            indexText.setBackgroundColor(Color.parseColor("#00000000"));
            tagText.setBackgroundColor(Color.parseColor("#00000000"));
        });
        indexText.setOnClickListener(v -> {
            wordListText.setBackgroundColor(Color.parseColor("#00000000"));
            indexText.setBackgroundColor(Color.parseColor("#dddddd"));
            tagText.setBackgroundColor(Color.parseColor("#00000000"));
        });
        tagText.setOnClickListener(v -> {
            wordListText.setBackgroundColor(Color.parseColor("#00000000"));
            indexText.setBackgroundColor(Color.parseColor("#00000000"));
            tagText.setBackgroundColor(Color.parseColor("#dddddd"));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_dictionary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            finish();
        } else if(item.getItemId() == R.id.action_information){
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
}