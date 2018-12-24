package ru.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.salon.model.Master;
import ru.salon.model.TimeSlot;

import java.time.Instant;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    List<TimeSlot> findByStartSlotBetweenAndMaster(Instant start, Instant end, Master master);
    List<TimeSlot> findByStartSlotBetween(Instant start, Instant end);

}
