package org.example.managers.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.model.data.LabWork;
import org.example.utils.io.console.Console;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Properties;


/**
 * Использует файл для сохранения и загрузки коллекции.
 */
public class DumpManager {
    private static final Gson gson;
    private static String inputFileName;
    private static String outputFileName;

    static {
        Properties properties = new Properties();

        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTypeAdapter())
                .create();

        try {
            properties.load(new FileInputStream("server\\src\\main\\resources\\application.properties"));
            inputFileName = properties.getProperty("INPUT_FILE");
            outputFileName = properties.getProperty("OUTPUT_FILE");
        } catch (IOException exception) {
            Console.printError(exception.getMessage());
        }
    }

    /**
     * Записывает коллекцию в файл.
     *
     * @param collection коллекция
     */
    public static boolean writeIntoFile(ArrayDeque<LabWork> collection) throws IOException {
        File f = new File(outputFileName);
        if (!f.exists())
            f.createNewFile();

        FileWriter fileWriter = new FileWriter(f);
        fileWriter.write(collectionToJson(collection));
        fileWriter.close();
        return true;
    }


    public static Collection<LabWork> readCollection() throws RuntimeException, IOException {
        FileReader fileReader = new FileReader(inputFileName);
        var collectionType = new TypeToken<ArrayDeque<LabWork>>() {
        }.getType();

        BufferedReader reader = new BufferedReader(fileReader);
        StringBuilder jsonString = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null)
            if (!line.equals("")) jsonString.append(line.trim());

        return gson.fromJson(jsonString.toString(), collectionType);
    }

    public static String collectionToJson(ArrayDeque<LabWork> collection){
        return gson.toJson(collection);
    }
}