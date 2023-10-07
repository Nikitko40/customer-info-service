package ru.iskhakov.serialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import ru.iskhakov.serialize.in.Criteria;
import ru.iskhakov.serialize.in.Stat;
import ru.iskhakov.serialize.out.SearchResult;
import ru.iskhakov.serialize.out.StatResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ObjectSerializer {

    public String serializeSearch(SearchResult obj) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        return gson.toJson(obj);
    }

    public String serializeStat(StatResult obj) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        return gson.toJson(obj);
    }

    public List<Criteria> deserializeSearch(String json) {
        Type listOfMyClassObject = new TypeToken<ArrayList<Criteria>>() {
        }.getType();
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
        return gson.fromJson(json, listOfMyClassObject);
    }

    public Stat deserializeStat(String json) {
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
        return gson.fromJson(json, Stat.class);
    }
}
