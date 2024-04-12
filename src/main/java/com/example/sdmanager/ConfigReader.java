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
        return new File(jsonObject.getString("gallerypath"));
    }

    public static void writeImagePath(String file) throws IOException {
        File configFile = new File("config.json");
        if (!configFile.exists()) {
            configFile.createNewFile();
        }

        String content = new String(Files.readAllBytes(Paths.get("config.json")), StandardCharsets.UTF_8);
        if (content.isEmpty()) {
            content = "{}";
        }

        JSONObject jsonObject = new JSONObject(content);
        jsonObject.put("gallerypath", file);
        Files.write(Paths.get("config.json"), jsonObject.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static void writeCollectionInformations(Collection collection) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("config.json")), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(content);

        JSONObject collectionInfo;
        if (jsonObject.has("collectionInformation")) {
            collectionInfo = jsonObject.getJSONObject("collectionInformation");
        } else {
            collectionInfo = new JSONObject();
        }

        int nextKey = collectionInfo.length() + 1;

        JSONObject newCollectionInfo = new JSONObject();
        newCollectionInfo.put("name", collection.getName());
        newCollectionInfo.put("profilePicture", collection.getProfilePicture());
        newCollectionInfo.put("imagefolderpath", collection.getImageFolderPath());

        collectionInfo.put(Integer.toString(nextKey), newCollectionInfo);
        jsonObject.put("collectionInformation", collectionInfo);

        Files.write(Paths.get("config.json"), jsonObject.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static Collection[] readCollectionInformation() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("config.json")), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(content);

        JSONObject collectionInfo = jsonObject.getJSONObject("collectionInformation");

        Collection[] collections = new Collection[collectionInfo.length()];

        int index = 0;
        for (String key : collectionInfo.keySet()) {
            JSONObject collection = collectionInfo.getJSONObject(key);
            collections[index] = new Collection(collection.getString("name"), collection.getString("profilePicture"), collection.getString("imagefolderpath"));
            index++;
        }

        return collections;
    }

    public static Prompt[] readPromptsInformation() throws IOException  {
        String content = new String(Files.readAllBytes(Paths.get("config.json")), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(content);

        JSONObject promptInfo = jsonObject.getJSONObject("promptInformation");

        Prompt[] prompts = new Prompt[promptInfo.length()];

        int index = 0;
        for (String key : promptInfo.keySet()) {
            JSONObject prompt = promptInfo.getJSONObject(key);
            prompts[index] = new Prompt(prompt.getString("positivePrompt"), prompt.getString("negativePrompt"), prompt.getString("profilePicture"));
            index++;
        }

        return prompts;
    }

    public static void writePromptsInformation(Prompt prompt) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("config.json")), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(content);

        JSONObject promptInfo;
        if (jsonObject.has("promptInformation")) {
            promptInfo = jsonObject.getJSONObject("promptInformation");
        } else {
            promptInfo = new JSONObject();
        }

        int nextKey = promptInfo.length() + 1;

        JSONObject newPromptInfo = new JSONObject();
        newPromptInfo.put("negativePrompt", prompt.getNegPrompt());
        newPromptInfo.put("positivePrompt", prompt.getPosPrompt());
        newPromptInfo.put("profilePicture", prompt.getProfilePicture());

        promptInfo.put(Integer.toString(nextKey), newPromptInfo);
        jsonObject.put("promptInformation", promptInfo);

        Files.write(Paths.get("config.json"), jsonObject.toString().getBytes(StandardCharsets.UTF_8));
    }
}

