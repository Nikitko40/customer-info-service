package ru.iskhakov.serialize;

public interface Serializer<T, R> {
    String serialize(T obj);

    R deserialize(String value, Class<T> tClass);
}
