package com.ibgo.pirulina.model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.ibgo.pirulina.model.json.JSONBuilder;
import com.ibgo.pirulina.model.json.JSONController;
import com.ibgo.pirulina.model.pojo.Contact;
import com.ibgo.pirulina.model.pojo.User;

import java.util.ArrayList;
import java.util.List;

import static com.ibgo.pirulina.model.json.JSONBuilder.buildUser;

/**
 * Clase encargada de almacenar los datos de la sesion.
 * Permite cambiar el usuario.
 */
public class SessionDataController {

    private static final String UPDATE = "UPDATE";
    private static final String INSERT = "INSERT";
    private static SessionDataController ourInstance;

    private Util.Position mCurrentPos;
    private User mUser;
    private ArrayList<Contact> mContacts;

    public static SessionDataController getInstance() {
        if (null == ourInstance) {
            ourInstance = new SessionDataController();
        }
        return ourInstance;
    }

    private SessionDataController() {
        mUser = new User();
    }

    /*public boolean registerUser(User user) {
        byte error = JSONController.setData(JSONBuilder.build(user));
        if (error == JSONController.NO_ERROR) {
            this.mUser = user;
            return true;
        }
        return false;
    }*/

    public byte updateUser(User user){
        return JSONController.sendUser(buildUser(user), UPDATE);
    }

    public byte insertUser(User user){
        return JSONController.sendUser(buildUser(user), INSERT);
    }

    public void setCurrentPos(Context context) {
        if (context == null) {
            mCurrentPos = null;
            return;
        }

        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> probiders = lm.getProviders(true);
        Location currentLocation = null;
        Util.Position currentPosition = new Util.Position();

        // Check permisions
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mCurrentPos = null;
            return;
        }
        for (String probider : probiders) {
            currentLocation = lm.getLastKnownLocation(probider);
            if (currentLocation != null)
                break;
        }
        if (currentLocation != null) {
            currentPosition.latitude = currentLocation.getLatitude();
            currentPosition.longitude = currentLocation.getLongitude();
        }

        mCurrentPos = currentPosition;
    }

    public Util.Position getCurrentPos() {
        return mCurrentPos;
    }

    public ArrayList<Contact> getContacts() {
        return mContacts;
    }

    public void setContacts(ArrayList<Contact> mContacts) {
        this.mContacts = mContacts;
    }

    public int contactSize() {
        return mContacts.size();
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User usuario) {
        mUser = usuario;
    }
}
