package ru.salon.model.enumiration;

import lombok.Getter;

@Getter
public enum MasterCategory {
    GRAND_MASTER("Гранд мастер"),
    START("Начинающий мастер"),
    MIDDLE("Стандартный мастер"),
    MIDDLE_PLUS("Мастер стандарт плюс"),
    TOP("Топ мастер"),
    SUPPORT("Помощник"),
    COLORIST("Колорист"),
    TOP_COLORIST("Топ колорист"),
    TOP_LEADER("Руководитель Топ мастер"),
    CARE_MASTER("Мастер по уходовым процедурам");

    private String description;

    MasterCategory(String description) {
        this.description = description;
    }
}
