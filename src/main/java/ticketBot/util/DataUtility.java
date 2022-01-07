package ticketBot.util;

import org.yaml.snakeyaml.Yaml;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtility {

    private static Map<String, Object> ids = new HashMap<>();

    public DataUtility(String fileName, Class clazz) {
        InputStream inputStream = clazz.getResourceAsStream(fileName);
        Yaml yaml = new Yaml();
        ids = yaml.load(inputStream); // Map<String, List<String>>
        if(ids == null) ids = new HashMap<>();
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void addId(String channelId, String messageId) {
        List<String> list;
        if(ids.get("data") != null) list = (List<String>) ids.get("data");
        else list = new ArrayList<>();
        list.add(channelId + ":" + messageId);
        ids.put("data", list);
        System.out.println(ids);
    }

    public static void removeId(String channelId, String messageId) {
        List<String> list;
        if(ids.get("data") != null) list = (List<String>) ids.get("data");
        else return;
        list.remove(channelId + ":" + messageId);
        ids.put("data", list);
    }

    public static void removeId(String channelId) {
        List<String> list;
        if(ids.get("data") != null) list = (List<String>) ids.get("data");
        else return;
        for(String id : list) {
            String[] array = id.split(":");
            if(array[0].equals(channelId)) {
                list.remove(id);
            }
        }

        ids.put("data.yml", list);
    }

    public static void write(String fileName, Class clazz) {
        InputStream inputStream = clazz.getResourceAsStream(fileName);
        Yaml yaml = new Yaml();
        yaml.dump(ids);
    }

    public static Map<String, Object> getIds() {
        return ids;
    }
}
