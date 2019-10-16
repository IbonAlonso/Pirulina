package com.ibgo.pirulina.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

public class SignInActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 0;
    private static Object lock = new Object();

    private TextView mSignUp;
    private TextView mUsername;
    private TextView mPassword;
    private Button mSignIn;
    private boolean mPermissionsGranted;
    private User mUser;

    private boolean askForPermissions() {
        if (mPermissionsGranted)
            return true;

        // Ask for laction permissions and saves last known location
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            mPermissionsGranted = true;
            SessionDataController.getInstance().setCurrentPos(getApplicationContext());
        }
        return mPermissionsGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPermissionsGranted = true;
                    SessionDataController.getInstance().setCurrentPos(this);
                } else {
                    mPermissionsGranted = false;
                }

                lock.notifyAll();
        }
    }

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

                if (askForPermissions()) {
                    if (correctUser()) {
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_login_succesful), Toast.LENGTH_LONG).show();
                        Intent intent = MainActivity.newIntent(getApplicationContext(), mUser);
                        startActivity(intent);
                        finish();
                    }
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

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private boolean correctUser() {

        boolean correct = false;

        if (isNetworkAvailable()) {
            byte errorCode = JSONController.logInUser(mUsername.getText().toString().trim(), Util.md5(mPassword.getText().toString().trim()));
        } else {

            SharedPreferences mPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);

            if ((mUsername.getText().toString().equals(mPreferences.getString("user", "nonUser")))
                    && Util.md5(mPassword.getText().toString()).equals(mPreferences.getString("password", "nonPass"))) {
                correct = true;
                mUser = new User();
                mUser.setLogin(mUsername.getText().toString());
                mUser.setName(mPreferences.getString("name", ""));
                mUser.setLast(mPreferences.getString("last", ""));
                mUser.setPhone(mPreferences.getString("phone", ""));
                mUser.setPass(mPreferences.getString("password", ""));
            } else if (mUsername.getText().toString().equals(mPreferences.getString("user", "nonUser"))) {
                Toast.makeText(getApplicationContext(), getString(R.string.toast_wrong_password), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.toast_invalid_login), Toast.LENGTH_LONG).show();
            }
        }

        return correct;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
