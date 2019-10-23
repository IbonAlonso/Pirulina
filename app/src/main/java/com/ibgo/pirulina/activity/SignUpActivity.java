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
import com.ibgo.pirulina.model.SessionDataController;
import com.ibgo.pirulina.model.Util;
import com.ibgo.pirulina.model.json.JSONController;
import com.ibgo.pirulina.model.pojo.User;

public class SignUpActivity extends AppCompatActivity {

    private Button mCancel;
    private Button mSignUp;
    private TextView mUsername;
    private TextView mName;
    private TextView mLast;
    private TextView mPhone;
    private TextView mPass;
    private TextView mRPass;
    private SessionDataController controller;

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
                } else if (mUsername.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_empty_username), Toast.LENGTH_LONG).show();
                } else if (mPass.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_empty_password), Toast.LENGTH_LONG).show();
                } else {
                    User user = saveUserInSharedPrefs();
                    if (Util.isNetworkAvailable(getApplicationContext())) {
                        controller = SessionDataController.getInstance();
                        byte error = controller.insertUser(user);
                        if (error == JSONController.NO_ERROR) {
                            controller.setUser(user);
                            Toast.makeText(getApplicationContext(), getString(R.string.toast_user_created_success), Toast.LENGTH_SHORT).show();
                        } else if (error == JSONController.OTHER_ERROR) {
                            Toast.makeText(getApplicationContext(), getString(R.string.toast_something_gonne_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                    startActivity(MainActivity.newIntent(getApplicationContext(), user));
                    finish();
                }
            }
        });

        mCancel = findViewById(R.id.btnCancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();
            }
        });
    }

    private User saveUserInSharedPrefs() {
        SharedPreferences mPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);

        String login = mUsername.getText().toString();
        String name = mName.getText().toString();
        String last = mLast.getText().toString();
        String phone = mPhone.getText().toString();
        String password = Util.md5(mPass.getText().toString());

        SharedPreferences.Editor mEditor = mPreferences.edit();
        mEditor.putString("user", login);
        mEditor.putString("name", name);
        mEditor.putString("last", last);
        mEditor.putString("phone", phone);
        mEditor.putString("password", password);
        mEditor.commit();

        User user = new User();
        user.setLogin(login);
        user.setLast(last);
        user.setName(name);
        user.setPass(password);
        user.setPhone(phone);

        Toast.makeText(getApplicationContext(), getString(R.string.toast_user_created), Toast.LENGTH_LONG).show();

        return user;
    }
}
