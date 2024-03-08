package ru.hse.paganini;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class ConfigParser {
    // TODO: Maintain this method for difficult yamls (with deep > 1).
    public static String getBotParameter(String path, String param_name) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Yaml yaml = new Yaml();
        Map<String, String> data = yaml.load(inputStream);
        String result = data.get(param_name);
        return result;
    }
}
