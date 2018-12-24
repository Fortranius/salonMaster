package ru.salon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.salon.model.Master;
import ru.salon.model.TimeSlot;

import java.time.Instant;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    @Query("Select c from TimeSlot c where c.startSlot >= startSlot and c.endSlot <= endSlot")
    Page<TimeSlot> findByStartAndEndDate(@Param("startSlot") Instant startSlot, @Param("endSlot") Instant endSlot, Pageable pageable);

    @Query("Select c from TimeSlot c where c.startSlot >= startSlot and c.endSlot <= endSlot and c.master = master")
    Page<TimeSlot> findByStartAndEndDateAAndMasterId(@Param("startSlot") Instant startSlot,
                                                     @Param("endSlot") Instant endSlot,
                                                     @Param("master") Master master,
                                                     Pageable pageable);

}
