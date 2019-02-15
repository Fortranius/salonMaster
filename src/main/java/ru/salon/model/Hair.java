package ru.salon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hair")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hair extends AuditModel {

    @Id
    @GeneratedValue(generator = "hair_generator")
    @SequenceGenerator(
            name = "hair_generator",
            sequenceName = "hair_sequence",
            initialValue = 1000
    )
    private Long id;

    private int minLength;

    private int maxLength;

    private BigDecimal price;
}
