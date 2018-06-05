package com.example.test.datok;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        // 사용자가 로그인되어 있는지 (null이 아님) 확인하고 이에 따라 UI를 업데이트합니다.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null) {

            Intent startIntetnt = new Intent(MainActivity.this, StartActivity.class);
            startActivity(startIntetnt);
            finish();
        }

    }

}
