package com.springboot3_1_4.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

// класс для pretty форматирования данных в JSON-e
// используется для "красивого" вывода JSON-a в консоль
public class JsonFormatter {

    public static String formatJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
            Object jsonObject = objectMapper.readValue(json, Object.class);
            return objectWriter.writeValueAsString(jsonObject);
        } catch (Exception e) {
            System.out.println("Some problem : " + e.getMessage());
            return json;
        }
    }

}
