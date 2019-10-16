package com.ibgo.pirulina.model.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Response {

    private String timestamp;
    private String message;
    private String details;

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
}
