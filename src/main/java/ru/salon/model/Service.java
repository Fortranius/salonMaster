package ru.salon.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "services")
@Data
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
}
