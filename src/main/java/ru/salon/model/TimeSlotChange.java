package ru.salon.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.Instant;

@Data
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotChange {

    private Instant date;

    private String change;
}
