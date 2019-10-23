package com.ibgo.pirulina.model.json;

public final class JSONTag {
    public static final String TAG_CURRENT_USER = "user";
    public static final String TAG_RESPONSE = "response";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_DETAILS = "details";
    public static final String TAG_REQUEST = "request";
    public static final String RESPONSE_KO = "KO";
    public static final String RESPONSE_WRONG_PASSWORD = "WRONG_PASSWORD";
    public static final String RESPONSE_CORRECT = "CORRECT";
    public static final String RESPONSE_NOT_EXIST = "NOT_EXIST";

    public final class User {
        public static final String TAG_USER = "users";

        public static final String TAG_LOGIN = "login";
        public static final String TAG_PASSWORD = "password";
        public static final String TAG_NAME = "name";
        public static final String TAG_LASTNAME = "lastname";
        public static final String TAG_PHONE = "phone";
    }

    public final class Response {
        public static final String TAG_TIMESTAMP = "timestamp";
        public static final String TAG_MESSAGE = "message";
        public static final String TAG_DETAILS = "details";
        public static final String TAG_REQUEST = "request";
    }
}
