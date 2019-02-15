package ru.salon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hair_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HairCategory {

    @Id
    @GeneratedValue(generator = "hair_category_generator")
    @SequenceGenerator(
            name = "hair_category_generator",
            sequenceName = "hair_category_sequence",
            initialValue = 1000
    )
    private Long id;

    private BigDecimal price;

    @Enumerated(value = EnumType.STRING)
    private MasterCategory type;
}
