package ru.salon.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "clients")
@Getter
@Setter
public class Client extends AuditModel implements Serializable {
    @Id
    @GeneratedValue(generator = "client_generator")
    @SequenceGenerator(
            name = "client_generator",
            sequenceName = "client_sequence",
            initialValue = 1000
    )
    private Long id;

    @Embedded
    private Person person;
}
