package ru.salon.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "service_category")
@Data
public class ServiceCategory extends AuditModel implements Serializable {

    @Id
    @GeneratedValue(generator = "service_category_generator")
    @SequenceGenerator(
            name = "service_category_generator",
            sequenceName = "service_category_generator",
            initialValue = 1000
    )
    private Long id;

    private BigDecimal price;

    @ManyToOne(fetch= FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "service_id")
    private Service service;

    @Enumerated(value = EnumType.STRING)
    private MasterCategory type;
}
