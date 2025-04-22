package ru.academits.gerasimenko.phonebookapi.data;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class ResponseBodyFiller {
    private ResponseBodyFiller() {
    }

    public static void fill(ObjectNode responseBody, boolean success, String message) {
        responseBody.put("success", success);
        responseBody.put("message", message);
    }
}