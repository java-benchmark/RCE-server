package com.defensepoint.rce.util;

public class LoggerUtil {

    private LoggerUtil() {}

    public static String logFileSanitizer(String message) {
        return message.replaceAll("[\n|\r|\t]", "_")
                .replace("<", "&lt")
                .replace(">", "&gt");
    }

    public static String logDatabaseSanitizer(String message) {
        return message.replace("<", "&lt")
                .replace(">", "&gt");
    }

}
