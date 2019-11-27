package com.ibgo.pirulina.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ibgo.pirulina.R;
import com.ibgo.pirulina.model.SessionDataController;
import com.ibgo.pirulina.model.json.JSONController;
import com.ibgo.pirulina.model.pojo.User;

import java.io.IOException;
import java.net.InetAddress;

public class SplashActivity extends AppCompatActivity {

    private static final String host = "http://85.61.150.74:3308/piruapi/ping";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new CheckUser().execute();
            }
        }, 2000);
    }

    class CheckUser extends AsyncTask<String, String, String> {

        private Intent intent;

        @Override
        protected String doInBackground(String... strings) {

            SharedPreferences mPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);

            if (mPreferences.getString("user", "nonUser").equals("nonUser")) {
                intent = new Intent(getApplicationContext(), SignInActivity.class);
            } else {
                User mUser = new User();
                try {
                    if (isNetworkAvailable()) {
                        if (InetAddress.getByName(host).isReachable(10000)) {
                            byte error = JSONController.logInUser(mPreferences.getString("user", ""), mPreferences.getString("password", ""));
                            if (error == JSONController.NO_ERROR) {
                                intent = MainActivity.newIntent(getApplicationContext(), SessionDataController.getInstance().getUser());
                            } else {
                                intent = new Intent(getApplicationContext(), SignInActivity.class);
                                finish();
                            }
                        }
                    } else {
                        mUser.setLogin(mPreferences.getString("user", ""));
                        mUser.setName(mPreferences.getString("name", ""));
                        mUser.setLast(mPreferences.getString("last", ""));
                        mUser.setPhone(mPreferences.getString("phone", ""));
                        mUser.setPass(mPreferences.getString("password", ""));
                        SessionDataController.getInstance().setUser(mUser);
                        intent = MainActivity.newIntent(getApplicationContext(), mUser);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            finish();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            startActivity(intent);
            finish();
        }

        private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
    }
}
