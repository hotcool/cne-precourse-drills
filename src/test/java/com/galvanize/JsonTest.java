package com.galvanize;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.fail;

public class JsonTest {

    @Test
    public void systemPropertiesWorked() {
        Path baseDirectory = Paths.get("").toAbsolutePath().normalize();
        Path filePath = Paths.get(baseDirectory.toString(), "system-properties.json");

        Gson gson = new Gson();
        JsonReader reader = null;
        Type collectionType = new TypeToken<Map<String, String>>(){}.getType();

        try {
            reader = new JsonReader(new FileReader(filePath.toString()));

            try {
                Map<String, String> result = gson.fromJson(reader, collectionType);

                if (result == null) {
                    System.err.println(String.format("\n\nExpected %s to contain JSON generated by running the application but it does not", filePath));
                    fail();
                } else {
                    if (!result.containsKey("java.runtime.name") || !result.containsKey("os.name")) {
                        System.err.println(String.format("\n\nExpected %s to contain JSON generated by running the application it does not", filePath));
                        fail();
                    }
                }
            } catch (JsonSyntaxException e) {
                System.err.println(String.format("\n\nExpected %s to contain valid JSON but it did not\n\n", filePath));
                e.printStackTrace();
                fail();
            }
        } catch (FileNotFoundException e) {
            System.err.println(String.format("\n\nExpected to find a file named %s but did not", filePath));
            System.err.println("\n\nDid you forget that step?\n\n");
            e.printStackTrace();
            fail();
        }
    }

}