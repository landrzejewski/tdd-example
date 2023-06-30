package org.example;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class UuidMatcher extends TypeSafeMatcher<String> {

    public static UuidMatcher isUuid() {
        return new UuidMatcher();
    }

    private static String UUID_PATTERN = "\\w{8}-\\w{4}-\\w{4}-\\w{4}-\\w{12}";

    @Override
    protected boolean matchesSafely(String uuid) {
        return uuid.matches(UUID_PATTERN);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Invalid uuid value");
    }

}
