package ru.salon.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ExpenseCriteria {

    private Long masterId;
    private Long productId;
}
