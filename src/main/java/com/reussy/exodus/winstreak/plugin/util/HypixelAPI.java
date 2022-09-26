package com.reussy.exodus.winstreak.plugin.util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HypixelAPI {

    private final String apiKey;
    private final String uuid;
    private int value;

    public HypixelAPI(String apiKey, String uuid) {
        this.apiKey = apiKey;
        this.uuid = uuid;
    }

    private String requestHttpGet(String url) throws Exception {

        StringBuilder response = new StringBuilder();

        URL URL = new URL(url);

        HttpURLConnection connection = (HttpURLConnection) URL.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader bufferedReader = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            response.append(line);
        }

        bufferedReader.close();

        return response.toString();
    }

    public HypixelAPI parse() throws Exception {

        String json = requestHttpGet("https://api.hypixel.net/player?uuid=" + uuid + "&key=" + apiKey);

        JSONObject jsonObject = new JSONObject(json);

        this.value = jsonObject.getJSONObject("player").getJSONObject("stats").getJSONObject("Bedwars").getInt("winstreak");
        return this;
    }

    public int getValue() {
        return value;
    }
}
