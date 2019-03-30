package com.ibgo.pirulina.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ibgo.pirulina.R;

public class SplashActivity extends AppCompatActivity {

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
                finish();
            } else {
                intent = new Intent(getApplicationContext(), MainActivity.class);
                finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            startActivity(intent);
            finish();
        }

    }
}
