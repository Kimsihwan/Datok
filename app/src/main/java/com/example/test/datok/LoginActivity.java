package com.example.test.datok;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private Toolbar mToolbar;

    private TextInputLayout mLoginEmail;
    private TextInputLayout mLoginPassword;

    private Button mLogin_btn;

    private ProgressDialog mLoginProgress;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_login);

        mToolbar = findViewById(R.id.login_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("로그인");

        mLoginProgress = new ProgressDialog(this);


        mLoginEmail = findViewById(R.id.login_email);
        mLoginPassword = findViewById(R.id.login_password);
        mLogin_btn = findViewById(R.id.login_btn);

        mLogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mLoginEmail.getEditText().getText().toString();
                String password = mLoginPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {

                    mLoginProgress.setTitle("로그인 중");
                    mLoginProgress.setMessage("확인 중입니다. 잠시만 기다려주세요.");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();
                    loginUser(email, password);
                }


            }
        });

    }

    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    Log.d(TAG, "로그인 성공");
                    mLoginProgress.dismiss();
                    Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                } else {
                    Log.d(TAG, "로그인 실패: ", task.getException());
                    mLoginProgress.hide();
                    Toast.makeText(LoginActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
