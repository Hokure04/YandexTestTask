package org.example.Utils.Readers;

import org.example.Models.UserData;
import org.example.Utils.Parser;
import org.example.Utils.PathManager;

public class CredentialsReader {

    public static  final String PATH_TO_USER_DATA = "credentials.json";
    private static UserData userData;

    public static UserData getUserData() {
        if(userData == null){
           userData = Parser.parse(PathManager.getPath(PATH_TO_USER_DATA), UserData.class);
        }
        return userData;
    }
}
