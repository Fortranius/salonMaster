package ru.salon.model;

import lombok.Getter;

@Getter
public enum HairType {
    HAIR_EXTENSION("Наращивание волос"),
    HAIR_REMOVAL("Снятие волос");

    private String description;

    HairType(String description) {
        this.description = description;
    }
}
