package ru.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.salon.model.Master;
import ru.salon.model.TimeSlot;

import java.time.Instant;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    List<TimeSlot> findByStartSlotBetweenAndMaster(Instant start, Instant end, Master master);
    List<TimeSlot> findByStartSlotBetween(Instant start, Instant end);

    @Query("Select ts from TimeSlot ts where ts.master = :master and ts.startSlot>:start and ts.startSlot<:end")
    List<TimeSlot> findBetweenStartSlotAndMaster(@Param("master") Master master, @Param("start") Instant start, @Param("end") Instant end);

    List<TimeSlot> findByMasterAndStartSlotAfterAndStartSlotBefore(Master master, Instant start, Instant end);
}
