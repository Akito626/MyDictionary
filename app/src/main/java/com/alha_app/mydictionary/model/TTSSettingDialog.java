package com.alha_app.mydictionary.model;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.alha_app.mydictionary.R;
import com.alha_app.mydictionary.WordActivity;

import java.util.ArrayList;
import java.util.Locale;

public class TTSSettingDialog extends DialogFragment {
    private WordActivity wordActivity;
    private ArrayList<Locale> locales;
    private int language;
    private float speed;
    private float pitch;

    public TTSSettingDialog(WordActivity activity, ArrayList<Locale> locales, int language, float speed, float pitch){
        wordActivity = activity;
        this.locales = locales;
        this.language = language;
        this.speed = speed;
        this.pitch = pitch;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState){
        // カスタムレイアウトの用意
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View customAlertView = inflater.inflate(R.layout.tts_setting_dialog, null);

        // Spinnerの準備
        String[] spinnerItems = new String[locales.size()];
        for(int i = 0; i < spinnerItems.length; i++){
            spinnerItems[i] = locales.get(i).getDisplayName();
        }
        Spinner spinner = customAlertView.findViewById(R.id.language_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerItems);
        spinner.setAdapter(adapter);
        spinner.setSelection(language);

        // SpeedBarの設定
        EditText speedText = customAlertView.findViewById(R.id.speed_text);
        SeekBar speedBar = customAlertView.findViewById(R.id.speed_bar);
        speedText.setText(String.valueOf(speed));
        speedBar.setProgress((int) (speed * 10));
        speedBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speedText.setText(String.valueOf((float)progress / 10));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // PitchBarの設定
        EditText pitchText = customAlertView.findViewById(R.id.pitch_text);
        SeekBar pitchBar = customAlertView.findViewById(R.id.pitch_bar);
        pitchText.setText(String.valueOf(pitch));
        pitchBar.setProgress((int) (pitch * 10));
        pitchBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pitchText.setText(String.valueOf((float)progress / 10));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // ダイアログの作成
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(customAlertView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        wordActivity.saveTTSSettings(
                                spinner.getSelectedItemPosition(),
                                (float) speedBar.getProgress() / 10,
                                (float) pitchBar.getProgress() / 10
                        );
                    }
                });
        return builder.create();
    }
}
