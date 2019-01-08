package com.droid.matt.matt;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    Button signupfirst;
    TextView goback;
    EditText email;
    EditText password;
    EditText name;
    EditText number;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    EditText editText;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        goback = (TextView) findViewById(R.id.go_back_to_login);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        signupfirst = (Button) findViewById(R.id.signupfirst_button);
        signupfirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        editText = (EditText) findViewById(R.id.captchentry);
        email = (EditText) findViewById(R.id.email_register);
        password = (EditText) findViewById(R.id.password_register);
        name = (EditText) findViewById(R.id.username_register);
        number = (EditText) findViewById(R.id.number_register);
    }

    private void signUp() {
        String captcha = editText.getText().toString().trim();
        String email_value = email.getText().toString().trim();
        String password_value = password.getText().toString().trim();
        String name_value = name.getText().toString().trim();
        String number_value = number.getText().toString().trim();

        if(TextUtils.isEmpty(email_value)){
            Toast.makeText(this,"Please Enter EmailId to Proceed Further",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password_value)){
            Toast.makeText(this,"Please Enter Password to Proceed Further",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(name_value)){
            Toast.makeText(this,"Please Enter your Name to Proceed Further",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(number_value)){
            Toast.makeText(this,"Please Enter your Phone Number to Proceed Further",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(captcha)){
            Toast.makeText(this,"Please Enter the correct captch to Proceed Further",Toast.LENGTH_SHORT).show();
            return;
        }

        if(captcha.equals("wvphnh")){
            progressDialog.setMessage("Please Wait,While we register you...");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email_value,password_value)
                    .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(Register.this,"Registration Successfull..",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Log.d("Register Flow", "onComplete: Failed=" + task.getException().getMessage());
                                Log.d("FirebaseAuth", "onComplete" + task.getException().getMessage());
                                progressDialog.dismiss();
                                Toast.makeText(Register.this,"We Couldn't make it. Please Try again later..",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else {
            Toast.makeText(this, "Enter the correct captcha to proceed further", Toast.LENGTH_LONG).show();
        }
    }
}
