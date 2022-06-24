package com.example.gym_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class StartWorkoutActivity extends AppCompatActivity {
    private EditText prep;
    private EditText work;
    private EditText rest;
    private EditText cyc;
    private EditText set;
    private EditText restSet;
    private TextView txt;

    private Button str;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_workout);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        prep = (EditText) findViewById(R.id.edPrepare);
        prep.setText("0",TextView.BufferType.EDITABLE);
        prep.setSelectAllOnFocus(true);
        work = (EditText) findViewById(R.id.edWork);
        work.setText("0",TextView.BufferType.EDITABLE);
        work.setSelectAllOnFocus(true);
        rest = (EditText) findViewById(R.id.edRest);
        rest.setText("0",TextView.BufferType.EDITABLE);
        rest.setSelectAllOnFocus(true);
        cyc = (EditText) findViewById(R.id.edCycles);
        cyc.setText("1",TextView.BufferType.EDITABLE);
        cyc.setSelectAllOnFocus(true);
        set = (EditText) findViewById(R.id.edSets);
        set.setText("1",TextView.BufferType.EDITABLE);
        set.setSelectAllOnFocus(true);
        restSet = (EditText) findViewById(R.id.edREstSets);
        restSet.setText("0",TextView.BufferType.EDITABLE);
        restSet.setSelectAllOnFocus(true);
        closeKeyboard();


        txt = findViewById(R.id.txtWork);


        str = (Button) findViewById(R.id.btnStar);


        str.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    int prp = Integer.parseInt(prep.getText().toString());
                    int wrk =  Integer.parseInt(work.getText().toString());
                    int rst =  Integer.parseInt(rest.getText().toString());
                    int cc =  Integer.parseInt(cyc.getText().toString());
                    int st =  Integer.parseInt(set.getText().toString());
                    int restS =  Integer.parseInt(restSet.getText().toString());

                    Intent intent = new Intent(StartWorkoutActivity.this, WorkoutActivity.class);

                    intent.putExtra("prp", prp);
                    intent.putExtra("w", wrk);
                    intent.putExtra("r", rst);
                    intent.putExtra("c",cc);
                    intent.putExtra("s", st);
                    intent.putExtra("rs",restS);
                    startActivity(intent);
            }
        });


    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}