package ru.salon.model.enumiration;

import lombok.Getter;

@Getter
public enum WorkingDay {
    $2X2("2 на 2"),
    $5X2("5 на 2"),
    $4X2("4 на 2");

    private String description;

    WorkingDay(String description) {
        this.description = description;
    }
}
