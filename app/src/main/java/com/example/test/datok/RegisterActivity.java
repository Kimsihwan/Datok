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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private TextInputLayout mDisplayname;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;
    private Button mCreateBtn;

    private Toolbar mToolbar;

    //ProgressDialog
    private ProgressDialog mRegProgress;

    //Firebase Auth
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Toolbar
        mToolbar = findViewById(R.id.register_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("회원가입");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRegProgress = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();

        mDisplayname = findViewById(R.id.reg_display_name);
        mEmail = findViewById(R.id.reg_email);
        mPassword = findViewById(R.id.reg_password);
        mCreateBtn = findViewById(R.id.reg_create_btn);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String display_name = mDisplayname.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                if(!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {

                    mRegProgress.setTitle("사용자 등록 중");
                    mRegProgress.setMessage("잠시만기다려주세요.");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(display_name, email, password);
                }



            }
        });


    }

    private void register_user(String display_name, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "회원가입 성공");
                            mRegProgress.dismiss();
                            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                        } else {
                            Log.d(TAG, "회원가입 실패: ", task.getException());
                            mRegProgress.hide();
                            Toast.makeText(RegisterActivity.this, "회원가입에 실패하였습니다. 양식을 확인하시고 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
