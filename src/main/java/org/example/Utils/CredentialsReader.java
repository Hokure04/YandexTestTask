package org.example.Utils;

import org.example.TestData.UserData;

public class CredentialsReader {

    public static  final String pathToUserData = "credentials.json";
    private static UserData userData;

    public static UserData getUserData(){
        if(userData == null){
            String userDataFilePath = PathManager.getPath(pathToUserData);
            userData =
        }
    }
}
