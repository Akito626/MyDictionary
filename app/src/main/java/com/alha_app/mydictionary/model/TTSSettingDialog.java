package com.alha_app.mydictionary.model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.alha_app.mydictionary.R;
import com.alha_app.mydictionary.WordActivity;

public class TTSSettingDialog extends DialogFragment {
    private WordActivity wordActivity;

    public TTSSettingDialog(WordActivity activity){
        wordActivity = activity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState){
        // カスタムレイアウトの用意
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View customAlertView = inflater.inflate(R.layout.tts_setting_dialog, null);

        Spinner spinner = customAlertView.findViewById(R.id.language_spinner);
        String[] languages = {"JAPANESE", "ENGLISH"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, languages);
        spinner.setAdapter(adapter);

        // ダイアログの作成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(customAlertView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        wordActivity.saveTTSSettings();
                    }
                });
        return builder.create();
    }
}
