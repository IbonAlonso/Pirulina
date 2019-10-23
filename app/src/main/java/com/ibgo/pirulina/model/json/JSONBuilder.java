package com.ibgo.pirulina.model.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibgo.pirulina.model.pojo.User;

import java.util.HashMap;
import java.util.Map;

public abstract class JSONBuilder {

    public static String buildUser(User mUser) {
        String json = null;
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> user = new HashMap<>();

        user.put(JSONTag.User.TAG_LOGIN, mUser.getLogin());
        user.put(JSONTag.User.TAG_PASSWORD, mUser.getPass());
        user.put(JSONTag.User.TAG_NAME, mUser.getName());
        user.put(JSONTag.User.TAG_LASTNAME, mUser.getLast());
        user.put(JSONTag.User.TAG_PHONE, mUser.getPhone());

        try {
            json = mapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return json;
    }
}
