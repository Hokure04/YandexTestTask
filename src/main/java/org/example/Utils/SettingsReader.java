package org.example.Utils;

import org.example.Models.Settings;

public class SettingsReader {

    private static final String PATH_TO_SETTINGS = "settings.json";

    private static Settings settings;

    public static Settings getSettings(){
        if(settings == null){
            settings = Parser.parse(PathManager.getPath(PATH_TO_SETTINGS), Settings.class);
        }
        return settings;
    }
}
