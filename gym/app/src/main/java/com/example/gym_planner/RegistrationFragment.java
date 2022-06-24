package com.example.gym_planner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegistrationFragment extends Fragment {
    private EditText userName, userPassword;
    private Button regButt;
    private FirebaseAuth firebaseAuth;
    private OnCompleteListener<AuthResult> onCompleteListener;



    public RegistrationFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onCompleteListener = (OnCompleteListener<AuthResult>) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

    }

    private void registrate(String userName, String userPass){
        firebaseAuth.createUserWithEmailAndPassword(userName,userPass).addOnCompleteListener(onCompleteListener);
    }

    private Boolean validate(){
        Boolean res = false;

        String name = userName.getText().toString();
        String pass = userPassword.getText().toString();


        if(name.isEmpty() || pass.isEmpty()){
            Toast.makeText(getActivity(),"Please enter all the details", Toast.LENGTH_SHORT).show();
        }else{
            res = true;
        }
        return res;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        userName = (EditText) view.findViewById(R.id.textViewUser);
        userPassword = (EditText)  view.findViewById(R.id.textViewPas);
        regButt = (Button)  view.findViewById(R.id.btnReg);

        regButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    registrate(userName.getText().toString(),userPassword.getText().toString());
                }else{
                    Toast.makeText(getActivity(),"Registration Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }
}
