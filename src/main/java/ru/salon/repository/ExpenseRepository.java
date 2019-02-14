package ru.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.salon.model.Expense;
import ru.salon.model.Master;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

    List<Expense> findByDateBetweenAndMaster(Instant start, Instant end, Master master);
    List<Expense> findByDateBetween(Instant start, Instant end);

    @Query("SELECT sum(expense.countProduct) from Expense expense where expense.product.id = :productId")
    Optional<Long> sumCountProduct(@Param("productId") Long productId);
}
