package ru.salon.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ExpenseCriteria {

    private Long masterId;
    private Long productId;
}
