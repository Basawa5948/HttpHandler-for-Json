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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button signin;
    Button signup;
    EditText email;
    Bundle bundle;
    int flagfromlogin = 0;
    EditText password;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    private static final int TIME = 2000;
    private long backPressed;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        bundle = new Bundle();

        if(firebaseAuth.getCurrentUser()!=null){
            finish();
            Intent intent = new Intent(Login.this,MainActivity.class);
            startActivity(intent);
        }

        signin = (Button) findViewById(R.id.login_button);
        signup = (Button) findViewById(R.id.register_button);
        email = (EditText) findViewById(R.id.email_login);
        password = (EditText) findViewById(R.id.password_login);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logMeIn();
            }
        });
    }

    private void logMeIn() {
        String email_value = email.getText().toString().trim();
        String password_value = password.getText().toString().trim();

        if(TextUtils.isEmpty(email_value)){
            Toast.makeText(this,"Please Enter EmailId to Proceed Further",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password_value)){
            Toast.makeText(this,"Please Enter Password to Proceed Further",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Please Wait,While we Log you In...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email_value,password_value)
                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(Login.this,"Login Successfull..",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Log.d("FirebaseAuth", "onComplete" + task.getException().getMessage());
                            progressDialog.dismiss();
                            Toast.makeText(Login.this,"We Couldn't make it. Please Try again later..",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
