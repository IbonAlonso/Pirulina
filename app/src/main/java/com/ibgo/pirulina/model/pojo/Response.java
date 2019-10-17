package com.ibgo.pirulina.model.pojo;

import com.ibgo.pirulina.model.DatabaseObject;
import com.ibgo.pirulina.model.json.JSONTag;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Response implements DatabaseObject {

    private String timestamp;
    private String message;
    private String details;
    private String request;

    public Response(){}

    public Response(String message, String request) {
        super();
        this.timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        this.message = message;
        this.details = request;
    }

    public Response(String message) {
        super();
        this.timestamp = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    @Override
    public DatabaseObject fromJSON(JSONObject json) throws JSONException {
        this.setTimestamp(json.getString(JSONTag.Response.TAG_TIMESTAMP));
        this.setMessage(json.getString(JSONTag.Response.TAG_MESSAGE));
        this.setDetails(json.getString(JSONTag.Response.TAG_DETAILS));
        this.setRequest(json.getString(JSONTag.Response.TAG_REQUEST));
        return this;
    }
}
