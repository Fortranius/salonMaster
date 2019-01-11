package ru.salon.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "masters")
@Data
public class Master extends AuditModel implements Serializable {

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

    @ManyToMany
    private List<Service> services;

}
