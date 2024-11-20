package com.banquemisr.challenge05.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskPriority {
    HIGH, MEDIUM, LOW;

    @JsonCreator
    public static TaskPriority fromValue(String value) {
        for (TaskPriority priority : values()) {
            if (priority.name().equalsIgnoreCase(value)) {
                return priority;
            }
        }
        throw new IllegalArgumentException("Invalid priority value: " + value + ". Allowed values are [HIGH, MEDIUM, LOW].");
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}

