package org.example.Utils.Readers;

import org.example.Models.Settings;
import org.example.Utils.Parser;
import org.example.Utils.PathManager;

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
