package com.example.sdmanager;

import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

// This class reads and writes configuration information to the config.json file
public class ConfigReader {

    // reads out the sd output folder path
    public static File returnImagePath() throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("config.json")), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(content);
        return new File(jsonObject.getString("gallerypath"));
    }

    // writes the sd output folder path
    public static void writeImagePath(String file) throws IOException {
        // in the bootupscene class, the config.json file is created if it does not exist
        File configFile = new File("config.json");
        if (!configFile.exists()) {
            configFile.createNewFile();
        }
        String content = new String(Files.readAllBytes(Paths.get("config.json")), StandardCharsets.UTF_8);
        if (content.isEmpty()) {
            content = "{}";
        }

        // write the output folder to the config.json file
        JSONObject jsonObject = new JSONObject(content);
        jsonObject.put("gallerypath", file);
        Files.write(Paths.get("config.json"), jsonObject.toString().getBytes(StandardCharsets.UTF_8));
    }

    // write the collection information
    public static void writeCollectionInformations(Collection collection) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("config.json")), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(content);

        // look for collectionInformation object
        JSONObject collectionInfo;
        if (jsonObject.has("collectionInformation")) {
            collectionInfo = jsonObject.getJSONObject("collectionInformation");
        } else {
            collectionInfo = new JSONObject();
        }

        // key for the collection for multiple collections
        long nextKey = System.currentTimeMillis();

        // write the collection information
        JSONObject newCollectionInfo = new JSONObject();
        newCollectionInfo.put("name", collection.getName());
        newCollectionInfo.put("profilePicture", collection.getProfilePicture());
        newCollectionInfo.put("imagefolderpath", collection.getImageFolderPath());
        collectionInfo.put(Long.toString(nextKey), newCollectionInfo);
        jsonObject.put("collectionInformation", collectionInfo);

        Files.write(Paths.get("config.json"), jsonObject.toString().getBytes(StandardCharsets.UTF_8));
    }

    // read the collection information
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

    // delete the collection information
    public static void deleteCollectionInformation(Collection collectionToDelete) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("config.json")), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(content);

        JSONObject collectionInfo = jsonObject.getJSONObject("collectionInformation");

        for (String key : collectionInfo.keySet()) {
            JSONObject collection = collectionInfo.getJSONObject(key);
            if (collection.getString("name").equals(collectionToDelete.getName()) &&
                    collection.getString("profilePicture").equals(collectionToDelete.getProfilePicture()) &&
                    collection.getString("imagefolderpath").equals(collectionToDelete.getImageFolderPath())) {
                collectionInfo.remove(key);
                break;
            }
        }
        jsonObject.put("collectionInformation", collectionInfo);
        Files.write(Paths.get("config.json"), jsonObject.toString().getBytes(StandardCharsets.UTF_8));
    }

    // read the prompt information
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

    // write the prompt information
    public static void writePromptsInformation(Prompt prompt) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("config.json")), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(content);

        JSONObject promptInfo;
        if (jsonObject.has("promptInformation")) {
            promptInfo = jsonObject.getJSONObject("promptInformation");
        } else {
            promptInfo = new JSONObject();
        }
        long nextKey = System.currentTimeMillis();

        JSONObject newPromptInfo = new JSONObject();
        newPromptInfo.put("positivePrompt", prompt.getPosPrompt());
        newPromptInfo.put("negativePrompt", prompt.getNegPrompt());
        newPromptInfo.put("profilePicture", prompt.getProfilePicture());

        promptInfo.put(Long.toString(nextKey), newPromptInfo);
        jsonObject.put("promptInformation", promptInfo);

        Files.write(Paths.get("config.json"), jsonObject.toString().getBytes(StandardCharsets.UTF_8));
    }

    // delete the prompt information
    public static void deletePromptInformation(Prompt promptToDelete) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("config.json")), StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(content);

        JSONObject promptInfo = jsonObject.getJSONObject("promptInformation");

        for (String key : promptInfo.keySet()) {
            JSONObject prompt = promptInfo.getJSONObject(key);
            if (prompt.getString("positivePrompt").equals(promptToDelete.getPosPrompt()) &&
                    prompt.getString("negativePrompt").equals(promptToDelete.getNegPrompt()) &&
                    prompt.getString("profilePicture").equals(promptToDelete.getProfilePicture())) {
                promptInfo.remove(key);
                break;
            }
        }
        jsonObject.put("promptInformation", promptInfo);
        Files.write(Paths.get("config.json"), jsonObject.toString().getBytes(StandardCharsets.UTF_8));
    }
}

