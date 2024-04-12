package com.example.sdmanager;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigReader {
    public static File returnImagePath() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("config.json")), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(content);
        return new File(jsonObject.getString("imagepath"));
    }

    public static void writeImagePath(String file) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imagepath", file);
        Files.write(Paths.get("config.json"), jsonObject.toString().getBytes(StandardCharsets.UTF_8));
    }
}
