package org.example.Utils;

import org.example.Models.TestData;

public class TestDataReader {

    private static final String PATH_TO_TEST_DATA = "testData.json";

    private static TestData testData;

    public static TestData getTestData() {
        if (testData == null) {
            testData = Parser.parse(PathManager.getPath(PATH_TO_TEST_DATA), TestData.class);
        }

        return testData;
    }
}
