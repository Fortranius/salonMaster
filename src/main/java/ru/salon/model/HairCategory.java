package ru.salon.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hair_category")
@Getter
@Setter
@ToString
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
