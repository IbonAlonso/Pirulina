package com.ibgo.pirulina.model.json;

import com.ibgo.pirulina.model.DatabaseObject;
import com.ibgo.pirulina.model.SessionDataController;
import com.ibgo.pirulina.model.pojo.Response;
import com.ibgo.pirulina.model.pojo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ibgo.pirulina.model.json.JSONTag.RESPONSE_KO;
import static com.ibgo.pirulina.model.json.JSONTag.RESPONSE_NOT_EXIST;
import static com.ibgo.pirulina.model.json.JSONTag.RESPONSE_WRONG_PASSWORD;
import static com.ibgo.pirulina.model.json.JSONTag.TAG_CURRENT_USER;
import static com.ibgo.pirulina.model.json.JSONTag.TAG_DETAILS;
import static com.ibgo.pirulina.model.json.JSONTag.TAG_MESSAGE;
import static com.ibgo.pirulina.model.json.JSONTag.TAG_REQUEST;

public abstract class JSONController {

    /* ///////// START OF COMMON CONFIG ///////// */
    private static final String DB = "pirulina";
    /* *** Error Codes *** */
    public static final byte NO_ERROR = 0;
    public static final byte OTHER_ERROR = 1;
    public static final byte INPUT_ERROR = 2;
    public static final byte USER_NOT_EXIST_ERROR = 4;
    public static final byte EMPTY = -1;
    public static final byte USER_EMPTY = 3;

    /* ///////// START OF GET CONFIG ///////// */
    /* *** URL Constants *** */
    private static final String LINK = "&";
    private static final String DATA_TABLE = "data_table[]=";
    /* *** URL Configuration *** */
    private static final String MAIN_HOST = "http://85.61.150.74:3308/piruapi";
    private static final String FIND_USER = "/loginUser?";
    private static final String GET_USER = "/getUser?";
    private static final String LOGIN_PARAM = "login=";
    private static final String PASS_PARAM = "password=";
    public static final String URL_LOGIN_USER = "loginUser";
    public static final String URL_INSERT_USER = "insertUser";
    private static final String USER_FIELD = "LOGIN";
    private static final String USER_TABLE = "USERS";
    private static final String PASS_FIELD = "PASSWORD";
    private static final String HASH_MODE = "md5";
    /* *** Database Admin User *** */
    private static final String USERNAME = "Pirulina";
    private static final String PASSWORD = "dummy";

    /* *** Default URLs *** */
    /*private static final String BASE_URL_GET = MAIN_HOST + "db=" + DB
            + LINK + "users_table=" + USER_TABLE
            + LINK + "username_field=" + USER_FIELD
            + LINK + "password_field=" + PASS_FIELD;
    private static final String ADMIN_URL_GET = BASE_URL_GET
            + LINK + "username=" + USERNAME
            + LINK + "password=" + PASSWORD
            + LINK + "hash=" + HASH_MODE;*/
    private static String mUserURL;

    /* ///////// START OF SET CONFIG ///////// */
    private static final String BASE_URL_SET = "http:/92.187.158.213:3307/json/set_json.php";
    //    private static final String BASE_URL_SET = HOST_SET + "json=";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "ibon98";

    private static User mCurrentUser;
    /* ///////// END OF CONFIGS ///////// */

    public static byte setData(String jsonString) {
        String result = doRequestPOST(jsonString);

        try {
            JSONObject json = new JSONObject(result);
            if (json.has(TAG_MESSAGE)) {
                return OTHER_ERROR;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return OTHER_ERROR;
        }
        return NO_ERROR;
    }

    public static byte logInUser(String username, String password) {
        mUserURL = MAIN_HOST + FIND_USER + LOGIN_PARAM + username + LINK + PASS_PARAM + password;

        String result = doRequestGET((mUserURL));

        try {
            SessionDataController controller = SessionDataController.getInstance();
            HashMap<String, List<DatabaseObject>> data = parseJSON(result);

            // Check for errors
            if (((Response) (data.get(TAG_CURRENT_USER)).get(0)).getMessage().equals(RESPONSE_KO)) {
                if (((Response) (data.get(TAG_CURRENT_USER)).get(0)).getDetails().equals(RESPONSE_WRONG_PASSWORD)) {
                    return INPUT_ERROR;
                }
                if (((Response) (data.get(TAG_CURRENT_USER)).get(0)).getDetails().equals(RESPONSE_NOT_EXIST)) {
                    return USER_NOT_EXIST_ERROR;
                }
                if (data.isEmpty()) {
                    return USER_EMPTY;
                }
            }

            mUserURL = MAIN_HOST + GET_USER + LOGIN_PARAM + username;

            result = doRequestGET(mUserURL);

            data = parseJSON(result);

            User user = (User) data.get(TAG_CURRENT_USER).get(0);
            mCurrentUser = user;
            controller.setUser(user);

        } catch (JSONException e) {
            e.printStackTrace();
            return OTHER_ERROR;
        }
        return NO_ERROR;
    }

    private static String doRequestGET(String s) {
        String response = null;

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(s).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();

            String buffer;
            while ((buffer = br.readLine()) != null) {
                builder.append(buffer);
            }

            response = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private static String doRequestPOST(String s) {
        String response = null;
        s = "json=".concat(s);

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(BASE_URL_SET).openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.getOutputStream().write(s.getBytes(StandardCharsets.UTF_8));

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();

            String buffer;
            while ((buffer = br.readLine()) != null) {
                builder.append(buffer);
            }

            response = builder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private static HashMap<String, List<DatabaseObject>> parseJSON(String jsonString, String... dataTables) throws JSONException {
        HashMap<String, List<DatabaseObject>> fullData = new HashMap<>();
        List<DatabaseObject> data;
        DatabaseObject object;

        JSONObject json = new JSONObject(jsonString);
        JSONArray jsonData;

        byte errorCode = handleError(json);
        if (errorCode == NO_ERROR) { // If there has been no errors in the doRequest
            if (json.has(TAG_REQUEST)) {
                switch ((String) json.get(TAG_REQUEST)) {
                    case URL_LOGIN_USER:
                        object = new Response();
                        data = new ArrayList<>();
                        data.add(object.fromJSON(json));
                        fullData.put(TAG_CURRENT_USER, data);
                        break;
                }
            } else if (json.has(JSONTag.User.TAG_LOGIN)) {
                object = new User();
                data = new ArrayList<>();
                data.add(object.fromJSON(json));
                fullData.put(TAG_CURRENT_USER, data);
            }
            /*for (String tag : dataTables) { // Each data table requested
                boolean validTag = true;
                jsonData = json.getJSONObject(JSONTag.TAG_DATA).getJSONArray(tag);

                // Each object inside current tag
                data = new ArrayList<>();
                for (int i = 0; i < jsonData.length() && validTag; i++) {
                    JSONObject jsonObject = jsonData.getJSONObject(i);
                    switch (tag) {
                        case JSONTag.User.TAG_USER:
                            object = new User();
                            break;
                        default:
                            validTag = false;
                            continue;
                    }
                    data.add(object.fromJSON(jsonObject));
                }
                if (!data.isEmpty()) {
                    fullData.put(tag, data);
                }
            }*/

            // Check if there is get_user in doRequest
            if (json.has(TAG_CURRENT_USER)) {
                JSONObject jsonObject = json.getJSONObject(TAG_CURRENT_USER);
                object = new User();
                data = new ArrayList<>();
                data.add(object.fromJSON(jsonObject));
                fullData.put(TAG_CURRENT_USER, data);
            }

        } else if (errorCode == INPUT_ERROR) { // User input error
            return fullData;
        }

        return fullData;
    }

    private static byte handleError(JSONObject json) throws JSONException {
        if (json.has(TAG_MESSAGE)) {
            if (!json.get(TAG_MESSAGE).equals(JSONTag.RESPONSE_KO)) {
                return NO_ERROR;
            }

            String error = (String) json.get(JSONTag.TAG_DETAILS);
            System.out.println(error);

            if (error.equalsIgnoreCase(JSONTag.RESPONSE_WRONG_PASSWORD)) {
                return INPUT_ERROR;
            } else if (error.equalsIgnoreCase(JSONTag.RESPONSE_NOT_EXIST)) {
                return USER_NOT_EXIST_ERROR;
            }
        } else if (json.has(JSONTag.User.TAG_LOGIN)) {
            return NO_ERROR;
        }

        return OTHER_ERROR;
    }

    public static String getDB() {
        return DB;
    }

    public static String getDbUser() {
        return DB_USER;
    }

    public static String getDbPassword() {
        return DB_PASSWORD;
    }
}

