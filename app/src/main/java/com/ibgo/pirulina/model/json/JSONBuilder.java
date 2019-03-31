package com.ibgo.pirulina.model.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibgo.pirulina.model.Util;
import com.ibgo.pirulina.model.pojo.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JSONBuilder {

    public static final String INSERT = "insert";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";

    public static String build(User mUser) {
        String json = "";
        try {
            ObjectMapper mapper = new ObjectMapper();

            Map<String, Object> jsonmap = new HashMap<>();
            final Map<String, Object> table;

            jsonmap.put("db", JSONController.getDB());
            jsonmap.put("user", JSONController.getDbUser());
            jsonmap.put("password", JSONController.getDbPassword());

            table = buildInsert(mUser);

            jsonmap.put("tables", new HashMap<String, Object>() {{
                put(JSONTag.User.TAG_USER, table);
            }});
            // convert map to JSON string
            json = mapper.writeValueAsString(jsonmap);

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return json;
    }

    private static Map<String, Object> buildInsert(User mUser) {
        Map<String, Object> table = new HashMap<>();
        Map<String, Object> value = new HashMap<>();
        List<Object> values = new ArrayList<>();

        value.put(JSONTag.User.TAG_LOGIN, mUser.getLogin());
        value.put(JSONTag.User.TAG_PASSWORD, Util.md5(mUser.getPass()));
        value.put(JSONTag.User.TAG_NAME, mUser.getName());
        value.put(JSONTag.User.TAG_LASTNAME, mUser.getLast());
        value.put(JSONTag.User.TaG_PHONE, mUser.getPhone());
        values.add(value);

        table.put("action", INSERT);
        table.put("values", values);

        return table;
    }
}
