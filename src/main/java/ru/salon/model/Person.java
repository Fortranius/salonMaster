package ru.salon.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class Person {
    private String firstname;
    private String lastname;
    private String midname;
    private String phone;
    private String mail;
}
