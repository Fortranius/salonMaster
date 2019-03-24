package ru.salon.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "additional_income")
@Data
public class AdditionalIncome extends AuditModel {

    @Id
    @GeneratedValue(generator = "expense_generator")
    @SequenceGenerator(
            name = "additional_income_generator",
            sequenceName = "additional_income_sequence",
            initialValue = 1000
    )
    private Long id;

    @ManyToOne(fetch= FetchType.EAGER)
    private Master master;

    private BigDecimal sum;

    private Instant date;
}
