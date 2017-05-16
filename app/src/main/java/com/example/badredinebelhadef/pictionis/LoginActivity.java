package com.example.badredinebelhadef.pictionis;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {


    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        final AutoCompleteTextView mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        final EditText mPasswordView = (EditText) findViewById(R.id.password);



        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmailView.getText().toString();
                final String password = mPasswordView.getText().toString();

                if (TextUtils.isEmpty(email))    { Toast.makeText(getApplicationContext(), "Please entrer an email address.", Toast.LENGTH_LONG).show();  return; }

                if (TextUtils.isEmpty(password)) { Toast.makeText(getApplicationContext(), "Please entrer a password.", Toast.LENGTH_LONG).show(); return; }

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {

                                        Toast.makeText(LoginActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                } else {
                                    startActivity( new Intent(LoginActivity.this, MainActivity.class) );
                                    finish();
                                }
                            }
                        });

            }
        });

        Button registerButton = (Button) findViewById(R.id.register_button);

        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailView.getText().toString();
                final String password = mPasswordView.getText().toString();

                if (TextUtils.isEmpty(email))    { Toast.makeText(getApplicationContext(), "Please entrer an email address.", Toast.LENGTH_LONG).show();  return; }

                if (TextUtils.isEmpty(password)) { Toast.makeText(getApplicationContext(), "Please entrer a password.", Toast.LENGTH_LONG).show(); return; }

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Error : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Register successful, welcome :)", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            }
        });


    }



}
