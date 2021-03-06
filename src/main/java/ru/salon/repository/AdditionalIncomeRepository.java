package ru.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.salon.model.AdditionalIncome;
import ru.salon.model.Master;

import java.time.Instant;
import java.util.List;

@Repository
public interface AdditionalIncomeRepository extends JpaRepository<AdditionalIncome, Long>, JpaSpecificationExecutor<AdditionalIncome> {

    List<AdditionalIncome> findByDateBetweenAndMaster(Instant start, Instant end, Master master);
}
