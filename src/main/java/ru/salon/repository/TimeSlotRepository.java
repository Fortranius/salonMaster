package ru.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.salon.model.Master;
import ru.salon.model.TimeSlot;
import ru.salon.model.enumiration.StatusOrder;

import java.time.Instant;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    List<TimeSlot> findByStartSlotBetweenAndMaster(Instant start, Instant end, Master master);
    List<TimeSlot> findByStartSlotBetweenAndMasterAndStatus(Instant start, Instant end, Master master, StatusOrder status);

    List<TimeSlot> findByStartSlotBetween(Instant start, Instant end);
    List<TimeSlot> findByStartSlotBetweenAndStatus(Instant start, Instant end, StatusOrder status);

    List<TimeSlot> findByClientId(Long clientId);

    int countByStartSlotBetweenAndMaster(Instant start, Instant end, Master master);
    int countByStartSlotBetweenAndMasterAndStatus(Instant start, Instant end, Master master, StatusOrder status);

    int countByStartSlotBetween(Instant start, Instant end);
    int countByStartSlotBetweenAndStatus(Instant start, Instant end, StatusOrder status);
}
