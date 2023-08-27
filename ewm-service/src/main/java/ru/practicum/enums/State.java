package ru.practicum.enums;

public enum State {
    PENDING, PUBLISHED, CANCELED;

    public static State fromString(String state) {
        return State.valueOf(state);
    }
}
