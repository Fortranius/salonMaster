package ru.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.salon.model.Expense;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

}
