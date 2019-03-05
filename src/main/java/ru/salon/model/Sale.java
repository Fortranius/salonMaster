package ru.salon.model;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "sale")
@Data
public class Sale extends AuditModel {

    @Id
    @GeneratedValue(generator = "sale_generator")
    @SequenceGenerator(
            name = "sale_generator",
            sequenceName = "sale_sequence",
            initialValue = 1000
    )
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    private Product product;

    private int countProduct;

    private Instant date;
}
