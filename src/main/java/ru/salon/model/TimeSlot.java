package ru.salon.model;

import lombok.Data;
import ru.salon.model.enumiration.StatusOrder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "time_slots")
@Data
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

    @ManyToOne(fetch= FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    private BigDecimal allPrice;

    private BigDecimal masterWorkPrice;

    @Column(name = "start_slot")
    private Instant startSlot;

    @Column(name = "end_slot")
    private Instant endSlot;

    private StatusOrder status;

    @ManyToMany(cascade = {CascadeType.MERGE})
    private List<Procedure> procedures;

    @ManyToOne(fetch= FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "hair_id")
    private Hair hair;

    private BigDecimal hairWeight;

    private BigDecimal hairCountExtension;

    private BigDecimal hairCountRemoval;

    @ElementCollection
    @CollectionTable(name="changes", joinColumns=@JoinColumn(name="ID"))
    private List<TimeSlotChange> changes;
}
