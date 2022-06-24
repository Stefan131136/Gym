package com.example.gym_planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalculateBMIActivity extends AppCompatActivity {

    private static final String TAG = "CalculateBMIActivity";

    private EditText weight;
    private EditText height;
    private TextView txtBmi;

    private TextView bmi;
    private TextView category;

    private Button calculate;

    ApplicationRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_bmi);

        repository = ApplicationRepository.getInstance(getApplicationContext());

        weight = (EditText) findViewById(R.id.weight_tf);
        height = (EditText) findViewById(R.id.height_tf);

        calculate = (Button) findViewById(R.id.calculate_btn);

        bmi = (TextView) findViewById(R.id.bmi_tv);
        bmi.setVisibility(View.INVISIBLE);

        txtBmi = (TextView) findViewById(R.id.txtBmi);
        txtBmi.setVisibility(View.INVISIBLE);



        closeKeyboard();


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.meal);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.home:
                        txtBmi.setVisibility(View.INVISIBLE);
                        bmi.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.map:
                        bmi.setVisibility(View.INVISIBLE);
                        txtBmi.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(getApplicationContext(),MapActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.meal:
                        return true;
                }
                return false;
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bmi.setVisibility(View.VISIBLE);
                txtBmi.setVisibility(View.VISIBLE);
                calculateRequest();
            }
        });
    }

    private void calculateRequest() {
        double w = Double.parseDouble(weight.getText().toString());
        double h = Double.parseDouble(height.getText().toString());
        repository.checkBMI(w, h, checkBMICalllback());
    }

    private Callback<Bmijson> checkBMICalllback() {
        return new Callback<Bmijson>() {
            @Override
            public void onResponse(Call<Bmijson> call, Response<Bmijson> response) {
                Bmijson bmiResponse = response.body();
                if (bmiResponse != null) {
                    bmi.setText(""+String.format("%.2f",bmiResponse.getBmi()));
                } else {
                    Toast.makeText(CalculateBMIActivity.this, "bmiResponse EMT", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<Bmijson> call, Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        };
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}