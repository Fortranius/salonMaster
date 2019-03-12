package ru.salon.model.enumiration;

import lombok.Getter;

@Getter
public enum WorkingDay {
    $2X2("2 на 2", 2, 2),
    $5X2("5 на 2", 5, 2),
    $4X2("4 на 2", 4, 2);

    private String description;
    private int workDay;
    private int dayOff;

    WorkingDay(String description, int workDay, int dayOff) {
        this.description = description;
        this.workDay = workDay;
        this.dayOff = dayOff;
    }
}
