package ru.salon.model;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "expense")
@Data
public class Expense extends AuditModel {

    @Id
    @GeneratedValue(generator = "product_generator")
    @SequenceGenerator(
            name = "product_generator",
            sequenceName = "product_sequence",
            initialValue = 1000
    )
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    private Product product;

    private int countProduct;

    @ManyToOne(fetch=FetchType.EAGER)
    private Master master;

    private Instant date;
}
