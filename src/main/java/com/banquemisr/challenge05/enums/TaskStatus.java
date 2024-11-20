package com.banquemisr.challenge05.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskStatus {
    TODO, IN_PROGRESS, DONE, PENDING;

    @JsonCreator
    public static TaskStatus fromValue(String value) {
        for (TaskStatus status : values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status value: " + value + ". Allowed values are [TODO, IN_PROGRESS, DONE, PENDING].");
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}

