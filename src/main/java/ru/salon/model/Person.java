package ru.salon.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class Person implements Serializable {
    private String name;
    private String surname;
    private String patronymic;
    private String phone;
    private String mail;
}
