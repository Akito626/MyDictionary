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

import java.util.ArrayList;
import java.util.Locale;

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

        ArrayList<Locale> locales = new ArrayList<>();
        locales.add(new Locale("ja"));
        locales.add(new Locale("en"));
        String[] spinnerItems = new String[locales.size()];
        for(int i = 0; i < spinnerItems.length; i++){
            spinnerItems[i] = locales.get(i).getDisplayName();
        }

        Spinner spinner = customAlertView.findViewById(R.id.language_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerItems);
        spinner.setAdapter(adapter);

        // ダイアログの作成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(customAlertView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        wordActivity.saveTTSSettings(locales.get(spinner.getSelectedItemPosition()));
                    }
                });
        return builder.create();
    }
}
