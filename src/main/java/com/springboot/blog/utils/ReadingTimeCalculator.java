package com.springboot.blog.utils;

import java.util.Arrays;

public class ReadingTimeCalculator {

    // Average Words Per Minute for adults
    private static final int AVERAGE_WPM = 200;

    public static String calculateReadingTime(String content) {
        // Count words in content
        int wordCount = countWords(content);

        // Calculate reading time
        int readingTimeMinutes = calculateReadingTimeMinutes(wordCount);

        // Format the reading time
        return formatReadingTime(readingTimeMinutes);
    }

    private static int countWords(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }

        // Split the content into words
        String[] words = content.split("\\s+");

        // Filter out empty strings
        return (int) Arrays.stream(words)
                .filter(word -> !word.trim().isEmpty())
                .count();
    }

    private static int calculateReadingTimeMinutes(int wordCount) {
        // Calculate reading time in minutes
        return (int) Math.ceil((double) wordCount / AVERAGE_WPM);
    }

    private static String formatReadingTime(int readingTimeMinutes) {
        // Format the reading time as "X min read"
        return readingTimeMinutes + " min read";
    }
}

