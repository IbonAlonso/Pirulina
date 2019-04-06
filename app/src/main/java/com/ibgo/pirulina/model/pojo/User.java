package com.ibgo.pirulina.model.pojo;

import com.ibgo.pirulina.model.DatabaseObject;
import com.ibgo.pirulina.model.json.JSONTag;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable, DatabaseObject {

    private UUID mId;
    private String mLogin;
    private String mPass;
    private String mName;
    private String mLast;
    private String mPhone;

    public User() {

    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID mId) {
        this.mId = mId;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String mLogin) {
        this.mLogin = mLogin;
    }

    public String getPass() {
        return mPass;
    }

    public void setPass(String mPass) {
        this.mPass = mPass;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getLast() {
        return mLast;
    }

    public void setLast(String mLast) {
        this.mLast = mLast;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    @Override
    public DatabaseObject fromJSON(JSONObject json) throws JSONException {
        this.setLogin(json.getString(JSONTag.User.TAG_LOGIN));
        this.setPass(json.getString(JSONTag.User.TAG_PASSWORD));
        this.setName(json.getString(JSONTag.User.TAG_NAME));
        this.setLast(json.getString(JSONTag.User.TAG_LASTNAME));
        this.setPhone(json.getString(JSONTag.User.TaG_PHONE));

        return this;
    }
}
