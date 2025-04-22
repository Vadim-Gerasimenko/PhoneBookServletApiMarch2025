package ru.academits.gerasimenko.phonebookapi.data;

import org.apache.commons.text.StringEscapeUtils;

public class TextProcessor {
    private TextProcessor() {
    }

    public static boolean isIncorrectField(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static String getProcessedText(String text) {
        return text == null ? "" : StringEscapeUtils.escapeHtml4(text).trim();
    }
}