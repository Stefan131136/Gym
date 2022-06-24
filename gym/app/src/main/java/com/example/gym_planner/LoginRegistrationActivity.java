package com.example.gym_planner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;


public class LoginRegistrationActivity extends AppCompatActivity implements OnCompleteListener<AuthResult>, LoginFragment.OnStartRegistrationFragment {



    private RegistrationFragment registrationFragment;
    private LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration);

        loginFragment = new LoginFragment();
        registrationFragment = new RegistrationFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (!isTwoPaneView()) {
            transaction.add(R.id.frLayout, loginFragment);
        } else {
            transaction.add(R.id.frLeft, loginFragment);
            transaction.add(R.id.frRight, registrationFragment);
        }
        transaction.commit();





    }

    private boolean isTwoPaneView(){
        return findViewById(R.id.frLayout) == null;
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
            Toast.makeText(LoginRegistrationActivity.this,"Login Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginRegistrationActivity.this, HomeActivity.class));
        }else{
            Toast.makeText(LoginRegistrationActivity.this,"Login Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void startRegFr() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.frLayout, registrationFragment);

        transaction.addToBackStack(null);
        transaction.commit();
        getFragmentManager().executePendingTransactions();


    }

    @Override
    public boolean twoPaneView() {
        return isTwoPaneView();
    }
}