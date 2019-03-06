package ru.salon.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
@Builder
public class ExpenseCriteria {

    private Long masterId;
    private Long productId;
    private Instant start;
    private Instant end;
}
