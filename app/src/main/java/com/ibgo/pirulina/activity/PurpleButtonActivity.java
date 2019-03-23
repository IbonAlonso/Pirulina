package com.ibgo.pirulina.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ibgo.pirulina.R;

public class PurpleButtonActivity extends AppCompatActivity {

    private Button mPurpleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purple_button);

        mPurpleButton = findViewById(R.id.btnPurple);
        mPurpleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Pulsado", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.dialog_logoff))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteLoginData();
                        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void deleteLoginData() {
        SharedPreferences mPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mPreferences.edit();
        mEditor.putString("user", "nonUser");
        mEditor.putString("name", "nonName");
        mEditor.putString("last", "nonLastname");
        mEditor.putString("phone", "nonPhone");
        mEditor.putString("password", "nonPass");
        mEditor.commit();
    }
}
