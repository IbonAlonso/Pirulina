package com.ibgo.pirulina.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ibgo.pirulina.R;
import com.ibgo.pirulina.fragments.MyUserFragment;
import com.ibgo.pirulina.fragments.PurpleButtonFragment;
import com.ibgo.pirulina.model.pojo.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MyUserFragment.OnFragmentInteractionListener {

    private static final String EXTRA_USER =
            "com.ibgo.pirulina.activity.user";
    private static User mUser;
    private static Dialog adviceDialog;

    public static Intent newIntent(Context packageContext, User user) {
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(EXTRA_USER, user);
        mUser = user;
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager mFm = getSupportFragmentManager();
        Fragment mFragment = mFm.findFragmentById(R.id.fragment_container);

        if (mFragment == null) {
            mFragment = new PurpleButtonFragment();
            mFm.beginTransaction().add(R.id.fragment_container, mFragment).commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            int count = getFragmentManager().getBackStackEntryCount();

            if (count == 0) {
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
            } else {
                getFragmentManager().popBackStack();
            }


        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_my_user:
                MyUserFragment fragment = MyUserFragment.newIntent(mUser);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.replace(R.id.fragment_container, fragment).commit();
                break;
            case R.id.action_log_out:
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
                break;
            case R.id.action_contacts:
                /*TODO Contactos*/
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_sexist:
                /*TODO Opcion ataque sexista*/
                showAdviceDialog(R.string.option_sexist);
                break;
            case R.id.nav_racist:
                /*TODO Opcion ataque racista*/
                showAdviceDialog(R.string.option_racist);
                break;
            case R.id.nav_homophobic:
                /*TODO Opcion ataque homofobico*/
                showAdviceDialog(R.string.option_homophobic);
                break;
            case R.id.nav_info_points:
                /*TODO Opcion puntos de informacion*/
                break;
            case R.id.nav_info:
                /*TODO Opcion informacion*/
                break;
            case R.id.nav_about_us:
                /*TODO Quienes somos*/
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showAdviceDialog(int type) {
        adviceDialog = new Dialog(MainActivity.this);
        adviceDialog.setContentView(R.layout.dialog_layout);
        ImageView image = adviceDialog.findViewById(R.id.img_dialog);
        switch(type){
            case R.string.option_homophobic:
                adviceDialog.setTitle(R.string.option_homophobic);
                image.setImageResource(R.drawable.homofobikoak);
                break;
            case R.string.option_racist:
                adviceDialog.setTitle(R.string.option_racist);
                image.setImageResource(R.drawable.arrazistak);
                break;
            case R.string.option_sexist:
                adviceDialog.setTitle(R.string.option_sexist);
                image.setImageResource(R.drawable.sexistak);
                break;
        }

        adviceDialog.show();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
