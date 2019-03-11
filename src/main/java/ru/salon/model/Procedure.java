package ru.salon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.salon.model.enumiration.HairType;

import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Procedure procedure = (Procedure) o;
        return Objects.equals(id, procedure.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
