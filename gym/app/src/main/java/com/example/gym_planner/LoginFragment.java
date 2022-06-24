package com.example.gym_planner;

import android.content.Context;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {
    private EditText usEm, usPassword;
    private TextView sign;
    private Button btnLog;
    private FirebaseAuth firebaseAuth;
    private OnStartRegistrationFragment onStartRegistrationFragment;
    private OnCompleteListener<AuthResult> onCompleteListener;



    public LoginFragment() {
        // Required empty public constructor
    }

    public interface OnStartRegistrationFragment{
        void startRegFr();
        boolean twoPaneView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onStartRegistrationFragment = (OnStartRegistrationFragment) context;
        onCompleteListener = (OnCompleteListener<AuthResult>) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

    }

    private void validate(String userName, String userPass){
        firebaseAuth.signInWithEmailAndPassword(userName,userPass).addOnCompleteListener(onCompleteListener);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        usEm = (EditText) view.findViewById(R.id.txtem);
        usPassword = (EditText) view.findViewById(R.id.txtPas);
        sign = (TextView) view.findViewById(R.id.txtSign);
        btnLog = (Button) view.findViewById(R.id.btnLogin);
        if(onStartRegistrationFragment.twoPaneView()){
            sign.setVisibility(View.INVISIBLE);
            sign.setClickable(false);
        }
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartRegistrationFragment.startRegFr();
            }
        });

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(usEm.getText().toString().trim(), usPassword.getText().toString());
            }
        });


        return view;
    }


}