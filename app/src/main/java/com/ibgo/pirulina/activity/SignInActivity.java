package com.ibgo.pirulina.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ibgo.pirulina.R;
import com.ibgo.pirulina.model.Util;

public class SignInActivity extends AppCompatActivity {

    private TextView mSignUp;
    private TextView mUsername;
    private TextView mPassword;
    private Button mSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mUsername = findViewById(R.id.edtUserName);
        mPassword = findViewById(R.id.edtPassword);

        mSignIn = findViewById(R.id.btnSignIn);
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (correctUser()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_login_succesful), Toast.LENGTH_LONG).show();
                }
            }
        });

        mSignUp = findViewById(R.id.txtSignUpBtn);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });
    }

    private boolean correctUser() {

        Boolean correct = false;

        SharedPreferences mPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);

        if ((mUsername.getText().toString().equals(mPreferences.getString("user", "nonUser")))
                && Util.md5(mPassword.getText().toString()).equals(mPreferences.getString("password", "nonPass"))) {
            correct = true;
        } else if (mUsername.getText().toString().equals(mPreferences.getString("user", "nonUser"))) {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_wrong_password), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_invalid_login), Toast.LENGTH_LONG).show();
        }

        return correct;
    }
}
