package org.example.Utils;

import org.example.TestData.UserData;

import java.io.IOException;

public class CredentialsReader {

    public static  final String pathToUserData = "credentials.json";
    private static UserData userData;

    public static UserData getUserData() throws IOException {
        if(userData == null){
            String userDataFilePath = PathManager.getPath(pathToUserData);
            userData = Parser.parse(userDataFilePath, UserData.class);
        }
        return userData;
    }
}
