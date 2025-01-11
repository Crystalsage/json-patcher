package org.example;

import com.google.gson.Gson;

import java.util.HashMap;

public class Util {

    public static HashMap deserializeJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, HashMap.class);
    }
}
