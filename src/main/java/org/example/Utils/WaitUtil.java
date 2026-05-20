package org.example.Utils;

import io.restassured.response.Response;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.function.Supplier;

public class WaitUtil {

    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private static final Duration POLLING = Duration.ofSeconds(1);

    public static boolean waitForRename(Supplier<String> getNameFunc, String newExtension) {
        return waitUntil(() -> {
            String name = getNameFunc.get();
            return name != null && name.endsWith(newExtension);
        });
    }

    public static boolean waitForUsedSpaceChange(
            Supplier<Response> diskInfoSupplier,
            long usedSpaceBefore,
            long expectedDifference
    ) {
        return waitUntil(() -> {
            Response response = diskInfoSupplier.get();

            if (response.statusCode() != 200) {
                return false;
            }

            long usedSpaceAfter = response.jsonPath().getLong("used_space");

            return usedSpaceAfter - usedSpaceBefore == expectedDifference;
        });
    }

    public static boolean waitUntil(Supplier<Boolean> condition) {
        try {
            return new FluentWait<>(condition)
                    .withTimeout(TIMEOUT)
                    .pollingEvery(POLLING)
                    .ignoring(Exception.class)
                    .until(Supplier::get);
        } catch (Exception exception) {
            return false;
        }
    }
}