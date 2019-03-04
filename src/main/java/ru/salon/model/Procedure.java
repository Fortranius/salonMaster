package ru.salon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "procedures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Procedure extends AuditModel {

    @Id
    @GeneratedValue(generator = "service_generator")
    @SequenceGenerator(
            name = "service_generator",
            sequenceName = "service_sequence",
            initialValue = 1000
    )
    private Long id;

    private String description;

    @Enumerated(value = EnumType.STRING)
    private HairType hairType;
}
