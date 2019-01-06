package ru.salon.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "expense")
@Getter
@Setter
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
}
