package ru.salon.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "time_slots")
@Getter
@Setter
public class TimeSlot extends AuditModel implements Serializable {

    @Id
    @GeneratedValue(generator = "time_slots_generator")
    @SequenceGenerator(
            name = "time_slots_generator",
            sequenceName = "time_slots_sequence",
            initialValue = 1000
    )
    private Long id;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "master_id", nullable = false)
    private Master master;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private BigDecimal price;

    @Column(name = "start_slot")
    private Instant startSlot;

    @Column(name = "end_slot")
    private Instant endSlot;

    @ManyToOne(fetch= FetchType.EAGER)
    private Service service;

    private StatusOrder status;
}
