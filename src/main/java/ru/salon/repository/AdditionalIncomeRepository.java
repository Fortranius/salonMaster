package ru.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.salon.model.AdditionalIncome;

@Repository
public interface AdditionalIncomeRepository extends JpaRepository<AdditionalIncome, Long>, JpaSpecificationExecutor<AdditionalIncome> {
}
