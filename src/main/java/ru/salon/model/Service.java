package ru.salon.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "services")
@Getter
@Setter
public class Service extends AuditModel {

    @Id
    @GeneratedValue(generator = "service_generator")
    @SequenceGenerator(
            name = "service_generator",
            sequenceName = "service_sequence",
            initialValue = 1000
    )
    private Long id;

    private String description;

    private BigDecimal maxPrice;

    private BigDecimal minPrice;
}