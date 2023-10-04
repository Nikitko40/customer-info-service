package ru.iskhakov.serialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ObjectSerializer<T, R> implements Serializer<T, R> {
    @Override
    public String serialize(T obj) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();

        return gson.toJson(obj);
    }

    @Override
    public R deserialize(String value, Class<T> tClass) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();

        return (R) gson.fromJson(value, tClass);
    }
}
