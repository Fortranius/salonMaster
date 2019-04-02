package ru.salon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends AuditModel {

    @Id
    @GeneratedValue(generator = "product_generator")
    @SequenceGenerator(
            name = "product_generator",
            sequenceName = "product_sequence",
            initialValue = 1000
    )
    private Long id;

    private String description;

    private BigDecimal purchasePrice;

    private BigDecimal sellingPrice;

    @ManyToOne(fetch=FetchType.EAGER)
    private Hair hair;
}
