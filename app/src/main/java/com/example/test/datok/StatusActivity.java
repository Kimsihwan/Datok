package com.example.test.datok;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StatusActivity extends AppCompatActivity {

    private static final String TAG = "StatusActivity";

    private Toolbar mToolbar;

    private TextInputLayout mStatus;
    private Button mSavebtn;

    //Firebase
    private DatabaseReference mStatusDatabase;
    private FirebaseUser mCurrentUser;

    //Progress
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        //Firebase
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();
        mStatusDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);



        mToolbar = findViewById(R.id.status_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("계정 상태");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String status_value = getIntent().getStringExtra("status_value");

        mStatus = findViewById(R.id.status_input);
        mSavebtn = findViewById(R.id.status_save_btn);

        mStatus.getEditText().setText(status_value);

        mSavebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Progress
                mProgress = new ProgressDialog(StatusActivity.this);
                mProgress.setTitle("변경 하는중");
                mProgress.setMessage("잠시만 기다려 주세요.");
                mProgress.show();

                String status = mStatus.getEditText().getText().toString();

                mStatusDatabase.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()) {
                            Log.d(TAG, "상태 변경 완료");
                            mProgress.dismiss();
                        } else {
                            Log.d(TAG, "상태 변경 실패", task.getException());
                            Toast.makeText(getApplicationContext(), "변경할수 없습니다.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });
    }
}
