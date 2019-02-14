package ru.salon.model;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "incoming")
@Data
public class Incoming extends AuditModel {

    @Id
    @GeneratedValue(generator = "incoming_generator")
    @SequenceGenerator(
            name = "incoming_generator",
            sequenceName = "incoming_sequence",
            initialValue = 1000
    )
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER)
    private Product product;

    private int countProduct;

    private Instant date;
}
