package com.alha_app.mydictionary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.alha_app.mydictionary.database.WordEntity;
import com.alha_app.mydictionary.model.SearchNum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexFragment extends Fragment {
    private DictionaryActivity activity;
    private MyDictionary myDictionary;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_index, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (DictionaryActivity) getActivity();
        myDictionary = (MyDictionary) activity.getApplication();

        prepareButton();
    }

    private void prepareButton(){
        // 索引のボタン全てにlistenerをセット
        View.OnClickListener listener = v -> {
            Button button = (Button) v;
            myDictionary.setSearchString(button.getText().toString());
            myDictionary.setSearchNum(SearchNum.Index);

            startActivity(new Intent(myDictionary, SearchResultsActivity.class));
        };

        getView().findViewById(R.id.button1).setOnClickListener(listener);
        getView().findViewById(R.id.button2).setOnClickListener(listener);
        getView().findViewById(R.id.button3).setOnClickListener(listener);
        getView().findViewById(R.id.button4).setOnClickListener(listener);
        getView().findViewById(R.id.button5).setOnClickListener(listener);
        getView().findViewById(R.id.button6).setOnClickListener(listener);
        getView().findViewById(R.id.button7).setOnClickListener(listener);
        getView().findViewById(R.id.button8).setOnClickListener(listener);
        getView().findViewById(R.id.button9).setOnClickListener(listener);
        getView().findViewById(R.id.button10).setOnClickListener(listener);
        getView().findViewById(R.id.button11).setOnClickListener(listener);
        getView().findViewById(R.id.button12).setOnClickListener(listener);
        getView().findViewById(R.id.button13).setOnClickListener(listener);
        getView().findViewById(R.id.button14).setOnClickListener(listener);
        getView().findViewById(R.id.button15).setOnClickListener(listener);
        getView().findViewById(R.id.button16).setOnClickListener(listener);
        getView().findViewById(R.id.button17).setOnClickListener(listener);
        getView().findViewById(R.id.button18).setOnClickListener(listener);
        getView().findViewById(R.id.button19).setOnClickListener(listener);
        getView().findViewById(R.id.button20).setOnClickListener(listener);
        getView().findViewById(R.id.button21).setOnClickListener(listener);
        getView().findViewById(R.id.button22).setOnClickListener(listener);
        getView().findViewById(R.id.button23).setOnClickListener(listener);
        getView().findViewById(R.id.button24).setOnClickListener(listener);
        getView().findViewById(R.id.button25).setOnClickListener(listener);
        getView().findViewById(R.id.button26).setOnClickListener(listener);
        getView().findViewById(R.id.button27).setOnClickListener(listener);
        getView().findViewById(R.id.button28).setOnClickListener(listener);
        getView().findViewById(R.id.button29).setOnClickListener(listener);
        getView().findViewById(R.id.button30).setOnClickListener(listener);
        getView().findViewById(R.id.button31).setOnClickListener(listener);
        getView().findViewById(R.id.button32).setOnClickListener(listener);
        getView().findViewById(R.id.button33).setOnClickListener(listener);
        getView().findViewById(R.id.button34).setOnClickListener(listener);
        getView().findViewById(R.id.button35).setOnClickListener(listener);
        getView().findViewById(R.id.button36).setOnClickListener(listener);
        getView().findViewById(R.id.button37).setOnClickListener(listener);
        getView().findViewById(R.id.button38).setOnClickListener(listener);
        getView().findViewById(R.id.button39).setOnClickListener(listener);
        getView().findViewById(R.id.button40).setOnClickListener(listener);
        getView().findViewById(R.id.button41).setOnClickListener(listener);
        getView().findViewById(R.id.button42).setOnClickListener(listener);
        getView().findViewById(R.id.button43).setOnClickListener(listener);
        getView().findViewById(R.id.button44).setOnClickListener(listener);
        getView().findViewById(R.id.button45).setOnClickListener(listener);
        getView().findViewById(R.id.button46).setOnClickListener(listener);
    }
}
