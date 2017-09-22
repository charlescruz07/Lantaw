package com.cruz.lantaw.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.cruz.lantaw.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout googleLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        googleLoginBtn = (FrameLayout) findViewById(R.id.googleLoginBtn);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.googleLoginBtn:
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
                break;
        }
    }
}
