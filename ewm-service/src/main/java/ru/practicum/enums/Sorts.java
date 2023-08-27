package ru.practicum.enums;

public enum Sorts {
    EVENT_DATE, VIEWS;

    public static Sorts fromString(String state) {
        return Sorts.valueOf(state);
    }
}
