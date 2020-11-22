package com.example.matchup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PhotoImport extends Activity {

    int optionId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_photo_import);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (1 * dm.widthPixels);
        int height = (int) (.18 * dm.heightPixels);

        getWindow().setLayout(width, height);
        getWindow().setGravity(Gravity.BOTTOM);

        //INTERACTIVE FUNCTIONS
        Button btnChoose = (Button) findViewById(R.id.btnSavedPhotos);
        btnChoose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Set option choice 0
                optionId = 0;
                Intent resultIntent = new Intent();
                resultIntent.putExtra("optionId", optionId);

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        /*Button btnTake = (Button) findViewById(R.id.btnTakePhoto);
        btnTake.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Set option choice 1
                optionId = 1;
                Intent resultIntent = new Intent();
                resultIntent.putExtra("optionId", optionId);

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });*/
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Set option choice 2
                optionId = 2;
                Intent resultIntent = new Intent();
                resultIntent.putExtra("optionId", optionId);

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
