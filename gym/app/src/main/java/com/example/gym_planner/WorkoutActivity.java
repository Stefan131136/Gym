package com.example.gym_planner;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class WorkoutActivity extends AppCompatActivity {



    private long startCount;
    private boolean timerRunning = true;

    private TextView txt;

    private TextView tsst;

    private CountDownTimer cdt;

    private int prepare;
    private int work;
    private int rest;
    private int cycles;
    private int set;
    private int restSet;

    int br = 0;
    int brSet = 1;

    private Button beep, strt, rst, vibra, pause, fin;
    private Vibrator vb;
    private long timeLeft;

    MediaPlayer mediaBeep = null, mediaStart = null, mediaRest = null, mediaFinish = null;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        mediaBeep = new MediaPlayer().create(this,R.raw.beep);
        mediaStart = new MediaPlayer().create(this,R.raw.start);
        mediaRest = new MediaPlayer().create(this,R.raw.rest);
        mediaFinish = new MediaPlayer().create(this,R.raw.finish);



        listView = (ListView) findViewById(R.id.listV);
        txt = findViewById(R.id.txtTimer);

        tsst = findViewById(R.id.txtNatpis);
        vb = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        pause = (Button) findViewById(R.id.btnpause);

        beep = (Button) findViewById(R.id.btnInv);
        beep.setVisibility(View.INVISIBLE);

        strt = (Button) findViewById(R.id.btnSoundStrt);
        strt.setVisibility(View.INVISIBLE);

        rst = (Button) findViewById(R.id.btnSoundRest);
        rst.setVisibility(View.INVISIBLE);

        vibra = (Button) findViewById(R.id.btnVib);
        vibra.setVisibility(View.INVISIBLE);

        fin = (Button) findViewById(R.id.btnFin);
        fin.setVisibility(View.INVISIBLE);



        Intent i = getIntent();

        prepare = i.getIntExtra("prp",0);
        work = i.getIntExtra("w", 0);
        rest = i.getIntExtra("r", 0);
        cycles = i.getIntExtra("c", 0);
        set = i.getIntExtra("s", 0);
        restSet = i.getIntExtra("rs",0);


        startCount = prepare +  (set * cycles * work) + (set*(cycles-1) * rest) + ((set - 1) * restSet);

        prepare*= 1000;
        work *= 1000;
        rest*= 1000;
        restSet*= 1000;

        beep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaBeep.start();
            }
        });

        strt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaStart.start();
            }
        });

        rst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRest.start();

            }
        });

        vibra.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                vb.vibrate(200);
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timerRunning){
                    pauseT();
                }else{
                    startAgain(timeLeft);
                }
            }
        });


        ArrayList<String> arrayList = new ArrayList<>();


        if(prepare == 0) {
            for (int k = 0; k < 2 * set * cycles - 1 ; k++) {
                if(k!=0 && restSet != 0 && (k+1) % (2 * cycles) == 0){
                    arrayList.add((k+1) + ". Rest between sets: " + restSet/1000);
                }else if(k % 2 == 0){
                    arrayList.add((k+1) + ". Work: " + work/1000);
                }else{
                    arrayList.add((k+1) + ". Rest: " + rest/1000);
                }
            }
        }else{
            arrayList.add("1. Prepare");
            for (int k = 0; k < 2 * set * cycles - 1 ; k++) {
                if(k!=0 && restSet != 0 && (k+1) % (2 * cycles) == 0){
                    arrayList.add((k+2) + ". Rest between sets: " + restSet/1000);
                }else if(k % 2 == 0){
                    arrayList.add((k+2) + ". Work: " + work/1000);
                }else{
                    arrayList.add((k+2) + ". Rest: " + rest/1000);
                }
            }

        }
        arrayList.add("Finish");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.center_list, arrayList);
        listView.setAdapter(arrayAdapter);


        tsst.setTextColor(Color.WHITE);
        txt.setTextColor(Color.WHITE);



        if(prepare != 0){
            tsst.setText("PREPARE");
            setActivityBackgroundColor(getResources().getColor(R.color.green));
            br++;

            cdt = new CountDownTimer(prepare, 1000){

                public void onTick(long millisUntilFinished) {
                    timeLeft = millisUntilFinished;
                    if(millisUntilFinished < 3000){
                        vibra.performClick();
                        vibra.setPressed(true);
                        vibra.invalidate();
                        vibra.setPressed(false);
                        vibra.invalidate();

                        beep.performClick();
                        beep.setPressed(true);
                        beep.invalidate();
                        beep.setPressed(false);
                        beep.invalidate();


                    }
                    txt.setText("" + (millisUntilFinished+1000)/1000);
                }

                public void onFinish() {
                    br++;
                    if(br == (2*set*cycles-1)){
                        preformClickFinish();
                        setActivityBackgroundColor(getResources().getColor(R.color.green));
                        txt.setText("Finish");
                        cancel();
                    }else{
                        if(br % ((2 * cycles)*brSet+1) == 0 && restSet != 0){
                            tsst.setText("REST BETWEEN SETS");
                            tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                            setActivityBackgroundColor(getResources().getColor(R.color.aqua));
                            brSet++;
                            startRestSet2();
                        }
                        else if(br % 2 == 0){

                            tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                            tsst.setText("WORK");
                            setActivityBackgroundColor(getResources().getColor(R.color.red));
                            startWork2();
                        }
                        else{
                            tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                            tsst.setText("REST");
                            setActivityBackgroundColor(getResources().getColor(R.color.blue));
                            startRest2();
                        }
                    }
                }
            }.start();

        }else{
            tsst.setText("WORK");
            setActivityBackgroundColor(getResources().getColor(R.color.red));

            cdt = new CountDownTimer(work, 1000){

                public void onTick(long millisUntilFinished) {
                    timeLeft = millisUntilFinished;
                    if(millisUntilFinished < 3000){
                        vibra.performClick();
                        vibra.setPressed(true);
                        vibra.invalidate();
                        vibra.setPressed(false);
                        vibra.invalidate();

                        beep.performClick();
                        beep.setPressed(true);
                        beep.invalidate();
                        beep.setPressed(false);
                        beep.invalidate();
                    }
                    txt.setText("" + (millisUntilFinished+1000) / 1000);
                }

                public void onFinish() {
                    br++;

                    if(br == (2*set*cycles)){
                        preformClickFinish();
                        setActivityBackgroundColor(getResources().getColor(R.color.green));
                        txt.setText("Finish");
                        cancel();
                    }else{
                        if(br % ((2 * cycles)*brSet-1) == 0 && restSet != 0){
                            tsst.setText("REST BETWEEN SETS");
                            tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                            setActivityBackgroundColor(getResources().getColor(R.color.aqua));
                            brSet++;
                            startRestSet();
                        }
                        else if(br % 2 == 0){

                            tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                            tsst.setText("WORK");
                            setActivityBackgroundColor(getResources().getColor(R.color.red));
                            startWork();
                        }
                        else{
                            tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                            tsst.setText("REST");
                            setActivityBackgroundColor(getResources().getColor(R.color.blue));
                            startRest();
                        }
                    }
                }
            }.start();

        }



    }
    private void startWork(){
        preformClickStart();
        cdt = new CountDownTimer(work, 1000){

            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                if(millisUntilFinished < 3000){
                    vibra.performClick();
                    vibra.setPressed(true);
                    vibra.invalidate();
                    vibra.setPressed(false);
                    vibra.invalidate();

                    beep.performClick();
                    beep.setPressed(true);
                    beep.invalidate();
                    beep.setPressed(false);
                    beep.invalidate();
                }
                txt.setText("" + (millisUntilFinished+1000) / 1000);
            }

            public void onFinish() {
                br++;

                if(br == (2*set*cycles-1)){
                    preformClickFinish();
                    setActivityBackgroundColor(getResources().getColor(R.color.green));
                    txt.setText("Finish");
                    cancel();
                }else{
                    if(br % ((2 * cycles)*brSet-1) == 0&& restSet != 0){
                        tsst.setText("REST BETWEEN SETS");
                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        setActivityBackgroundColor(getResources().getColor(R.color.aqua));
                        brSet++;
                        startRestSet();
                    }
                    else if(br % 2 == 0){

                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                        tsst.setText("WORK");
                        setActivityBackgroundColor(getResources().getColor(R.color.red));
                        startWork();
                    }
                    else{
                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                        tsst.setText("REST");
                        setActivityBackgroundColor(getResources().getColor(R.color.blue));
                        startRest();
                    }

                }
            }
        }.start();
    }


    private void startRestSet(){
        preformClickRest();
        cdt = new CountDownTimer(restSet, 1000){

            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                if(millisUntilFinished < 3000){
                    vibra.performClick();
                    vibra.setPressed(true);
                    vibra.invalidate();
                    vibra.setPressed(false);
                    vibra.invalidate();

                    beep.performClick();
                    beep.setPressed(true);
                    beep.invalidate();
                    beep.setPressed(false);
                    beep.invalidate();
                }
                txt.setText("" + (millisUntilFinished+1000) / 1000);
            }

            public void onFinish() {
                br++;
                if(br == (2*set*cycles-1)){
                    preformClickFinish();
                    setActivityBackgroundColor(getResources().getColor(R.color.green));
                    txt.setText("Finish");
                    cancel();
                }else{
                    if(br % ((2 * cycles)*brSet-1) == 0 && restSet != 0){
                        tsst.setText("REST BETWEEN SETS");
                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        setActivityBackgroundColor(getResources().getColor(R.color.aqua));
                        brSet++;
                        startRestSet();
                    }
                    else if(br % 2 == 0){

                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                        tsst.setText("WORK");
                        setActivityBackgroundColor(getResources().getColor(R.color.red));
                        startWork();
                    }
                    else{
                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                        tsst.setText("REST");
                        setActivityBackgroundColor(getResources().getColor(R.color.blue));
                        startRest();
                    }

                }
            }
        }.start();

    }

    private void startRest(){
        preformClickRest();
        cdt = new CountDownTimer(rest, 1000){

            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                if(millisUntilFinished < 3000){
                    vibra.performClick();
                    vibra.setPressed(true);
                    vibra.invalidate();
                    vibra.setPressed(false);
                    vibra.invalidate();

                    beep.performClick();
                    beep.setPressed(true);
                    beep.invalidate();
                    beep.setPressed(false);
                    beep.invalidate();
                }
                txt.setText("" + (millisUntilFinished+1000) / 1000);
            }

            public void onFinish() {
                br++;
                if(br == (2*set*cycles-1)){
                    preformClickFinish();
                    setActivityBackgroundColor(getResources().getColor(R.color.green));
                    txt.setText("Finish");
                    cancel();
                }else{
                    if(br % ((2 * cycles)*brSet-1) == 0&& restSet != 0){
                        tsst.setText("REST BETWEEN SETS");
                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        setActivityBackgroundColor(getResources().getColor(R.color.aqua));
                        brSet++;
                        startRestSet();
                    }
                    else if(br % 2 == 0){

                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                        tsst.setText("WORK");
                        setActivityBackgroundColor(getResources().getColor(R.color.red));
                        startWork();
                    }
                    else{
                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                        tsst.setText("REST");
                        setActivityBackgroundColor(getResources().getColor(R.color.blue));
                        startRest();
                    }

                }
            }
        }.start();

    }

    private void startRestSet2(){
        preformClickRest();
        cdt = new CountDownTimer(restSet, 1000){

            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                if(millisUntilFinished < 3000){
                    vibra.performClick();
                    vibra.setPressed(true);
                    vibra.invalidate();
                    vibra.setPressed(false);
                    vibra.invalidate();
                    beep.performClick();
                    beep.setPressed(true);
                    beep.invalidate();
                    beep.setPressed(false);
                    beep.invalidate();
                }
                txt.setText("" + (millisUntilFinished+1000) / 1000);
            }

            public void onFinish() {
                br++;

                if(br == (2*set*cycles+1)){
                    preformClickFinish();
                    setActivityBackgroundColor(getResources().getColor(R.color.green));
                    txt.setText("Finish");
                    cancel();
                }else{
                    if(br % ((2 * cycles)*brSet+1) == 0 && restSet != 0){
                        tsst.setText("REST BETWEEN SETS");
                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        setActivityBackgroundColor(getResources().getColor(R.color.aqua));
                        brSet++;
                        startRestSet2();
                    }
                    else if(br % 2 == 0){

                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                        tsst.setText("WORK");
                        setActivityBackgroundColor(getResources().getColor(R.color.red));
                        startWork2();
                    }
                    else{
                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                        tsst.setText("REST");
                        setActivityBackgroundColor(getResources().getColor(R.color.blue));
                        startRest2();
                    }

                }
            }
        }.start();

    }

    private void startRest2(){
        preformClickRest();
        cdt = new CountDownTimer(rest, 1000){

            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                if(millisUntilFinished < 3000){
                    vibra.performClick();
                    vibra.setPressed(true);
                    vibra.invalidate();
                    vibra.setPressed(false);
                    vibra.invalidate();

                    beep.performClick();
                    beep.setPressed(true);
                    beep.invalidate();
                    beep.setPressed(false);
                    beep.invalidate();
                }
                txt.setText("" + (millisUntilFinished+1000) / 1000);
            }

            public void onFinish() {
                br++;

                if(br == (2*set*cycles+1)){
                    preformClickFinish();
                    setActivityBackgroundColor(getResources().getColor(R.color.green));
                    txt.setText("Finish");
                    cancel();
                }else{
                    if(br % ((2 * cycles)*brSet+1) == 0&& restSet != 0){
                        tsst.setText("REST BETWEEN SETS");
                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        setActivityBackgroundColor(getResources().getColor(R.color.aqua));
                        brSet++;
                        startRestSet2();
                    }
                    else if(br % 2 == 0){

                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                        tsst.setText("WORK");
                        setActivityBackgroundColor(getResources().getColor(R.color.red));
                        startWork2();
                    }
                    else{
                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                        tsst.setText("REST");
                        setActivityBackgroundColor(getResources().getColor(R.color.blue));
                        startRest2();
                    }

                }
            }
        }.start();

    }

    private void startWork2(){
        preformClickStart();
        cdt = new CountDownTimer(work, 1000){

            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                if(millisUntilFinished < 3000){
                    vibra.performClick();
                    vibra.setPressed(true);
                    vibra.invalidate();
                    vibra.setPressed(false);
                    vibra.invalidate();
                    beep.performClick();
                    beep.setPressed(true);
                    beep.invalidate();
                    beep.setPressed(false);
                    beep.invalidate();
                }
                txt.setText("" + (millisUntilFinished+1000) / 1000);
            }

            public void onFinish() {
                br++;

                if(br == (2*set*cycles+1)){
                    preformClickFinish();
                    setActivityBackgroundColor(getResources().getColor(R.color.green));
                    txt.setText("Finish");
                    cancel();
                }else{
                    if(br % ((2 * cycles)*brSet+1) == 0&& restSet != 0){
                        tsst.setText("REST BETWEEN SETS");
                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        setActivityBackgroundColor(getResources().getColor(R.color.aqua));
                        brSet++;
                        startRestSet2();
                    }
                    else if(br % 2 == 0){

                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                        tsst.setText("WORK");
                        setActivityBackgroundColor(getResources().getColor(R.color.red));
                        startWork2();
                    }
                    else{
                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                        tsst.setText("REST");
                        setActivityBackgroundColor(getResources().getColor(R.color.blue));
                        startRest2();
                    }

                }
            }
        }.start();
    }

   private void pauseT(){
       cdt.cancel();
       timerRunning = false;
   }


    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }



    private void startAgain(long time){
        timerRunning = true;

        cdt = new CountDownTimer(time, 1000){

            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                if(millisUntilFinished < 3000){
                    vibra.performClick();
                    vibra.setPressed(true);
                    vibra.invalidate();
                    vibra.setPressed(false);
                    vibra.invalidate();

                    beep.performClick();
                    beep.setPressed(true);
                    beep.invalidate();
                    beep.setPressed(false);
                    beep.invalidate();
                }
                txt.setText("" + (millisUntilFinished+1000) / 1000);
            }

            public void onFinish() {
                br++;

                if(br == (2*set*cycles+1)){
                    setActivityBackgroundColor(getResources().getColor(R.color.green));
                    txt.setText("Finish");
                    cancel();
                }else{
                    if(br % ((2 * cycles)*brSet+1) == 0&& restSet != 0){
                        tsst.setText("REST BETWEEN SETS");
                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        setActivityBackgroundColor(getResources().getColor(R.color.aqua));
                        brSet++;
                        startRestSet2();
                    }
                    else if(br % 2 == 0){

                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                        tsst.setText("WORK");
                        setActivityBackgroundColor(getResources().getColor(R.color.red));
                        startWork2();
                    }
                    else{
                        tsst.setTextSize(TypedValue.COMPLEX_UNIT_SP,65);
                        tsst.setText("REST");
                        setActivityBackgroundColor(getResources().getColor(R.color.blue));
                        startRest2();
                    }

                }
            }
        }.start();
    }

    private void preformClickStart(){
        strt.performClick();
        strt.setPressed(true);
        strt.invalidate();
        strt.setPressed(false);
        strt.invalidate();

    }

    private void preformClickRest(){
        rst.performClick();
        rst.setPressed(true);
        rst.invalidate();
        rst.setPressed(false);
        rst.invalidate();

    }

    private void preformClickFinish(){
        fin.performClick();
        fin.setPressed(true);
        fin.invalidate();
        fin.setPressed(false);
        fin.invalidate();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cdt = null;
        mediaBeep.release();
        mediaStart.release();
        mediaRest.release();
        vb.cancel();
    }
}


