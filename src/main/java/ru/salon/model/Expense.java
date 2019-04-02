package ru.salon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "expense")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Expense extends AuditModel {

    @Id
    @GeneratedValue(generator = "expense_generator")
    @SequenceGenerator(
            name = "expense_generator",
            sequenceName = "expense_sequence",
            initialValue = 1000
    )
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    private Product product;

    private int countProduct;

    @ManyToOne(fetch=FetchType.EAGER)
    private Master master;

    private Instant date;

    @ManyToOne(fetch=FetchType.EAGER)
    private TimeSlot timeSlot;
}
