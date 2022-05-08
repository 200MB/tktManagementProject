package Json;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.beans.JavaBean;
import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TktJson {


    public static boolean movieExists(String id) {
        return getJsonList().stream().anyMatch(e -> e.containsValue(id));
    }

    public static ArrayList<JSONObject> getJsonList() {
        BufferedReader reader;
        ArrayList<JSONObject> jsonArr = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try {
            reader = new BufferedReader(new FileReader("src/main/resources/MoviesJson"));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() == 0) continue;
                jsonArr.add((JSONObject) parser.parse(line));
            }
            reader.close();
        } catch (IOException | ParseException e) {
            File file = new File("src/main/resources/MoviesJson");
            System.out.println("DEBUG:FILE NOT FOUND. CREATING...");
            e.printStackTrace();
        }
        return jsonArr;
    }

    //movies that can be viewed to user interface
    public static ArrayList<JSONObject> getJsonListForUsers() {
        return (ArrayList<JSONObject>) getJsonList().stream().filter(e->e.get("Show").equals("true")).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public static void addJson(String name, String url, String rating,
                               String description, String show, String id) throws IOException {
        BufferedWriter writer;

        writer = new BufferedWriter(new FileWriter("src/main/resources/MoviesJson", true));


        JSONObject json = new JSONObject();
        json.put("Name", name);
        json.put("Url", url);
        json.put("Rating", rating);
        json.put("Description", description);
        json.put("Show", show);
        json.put("id", id);
        writer.append(json.toJSONString()).append("\n");
        writer.flush();
        writer.close();
        System.out.println("DEBUG: APPENDED JSON");
    }

    private static void clearFile() throws FileNotFoundException {
        File file = new File("src/main/resources/MoviesJson");
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();
    }

    public static void updateJson(ArrayList<JSONObject> arr) throws IOException {
        BufferedWriter writer;

        writer = new BufferedWriter(new FileWriter("src/main/resources/MoviesJson", true));
        clearFile();
        for (JSONObject obj : arr) {
            writer.append(obj.toJSONString()).append("\n");
        }
        writer.close();
    }
}
