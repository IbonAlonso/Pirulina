package com.ibgo.pirulina.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ibgo.pirulina.R;

public class PurpleButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purple_button);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.dialog_logoff))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences mPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPreferences.edit();
                        mEditor.putString("user", "nonUser");
                        mEditor.putString("name", "nonName");
                        mEditor.putString("last", "nonLastname");
                        mEditor.putString("phone", "nonPhone");
                        mEditor.putString("password", "nonPass");
                        mEditor.commit();
                        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
