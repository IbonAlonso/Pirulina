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

public class SignUpActivity extends AppCompatActivity {

    private Button mCancel;
    private Button mSignUp;
    private TextView mUsername;
    private TextView mName;
    private TextView mLast;
    private TextView mPhone;
    private TextView mPass;
    private TextView mRPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mUsername = findViewById(R.id.edtUsername);
        mName = findViewById(R.id.edtName);
        mLast = findViewById(R.id.edtLast);
        mPhone = findViewById(R.id.edtPhone);
        mPass = findViewById(R.id.edtPassword01);
        mRPass = findViewById(R.id.edtPassword02);

        mSignUp = findViewById(R.id.btnRegister);
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mPass.getText().toString().equals(mRPass.getText().toString())) {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_password_not_validated), Toast.LENGTH_LONG).show();
                } else {

                    SharedPreferences mPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);

                    SharedPreferences.Editor mEditor = mPreferences.edit();
                    mEditor.putString("user", mUsername.getText().toString());
                    mEditor.putString("name", mName.getText().toString());
                    mEditor.putString("last", mLast.getText().toString());
                    mEditor.putString("phone", mPhone.getText().toString());
                    mEditor.putString("password", Util.md5(mPass.getText().toString()));
                    mEditor.commit();
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_user_created), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                }
            }
        });

        mCancel = findViewById(R.id.btnCancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
            }
        });
    }
}
