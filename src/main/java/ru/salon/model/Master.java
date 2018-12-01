package ru.salon.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "masters")
@Getter
@Setter
public class Master extends AuditModel {
    @Id
    @GeneratedValue(generator = "master_generator")
    @SequenceGenerator(
            name = "master_generator",
            sequenceName = "master_sequence",
            initialValue = 1000
    )
    private Long id;

    @Embedded
    private Person person;

}
