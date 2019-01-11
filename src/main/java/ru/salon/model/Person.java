package ru.salon.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class Person implements Serializable {
    private String name;
    private String surname;
    private String patronymic;
    private String phone;
    private String mail;
}
