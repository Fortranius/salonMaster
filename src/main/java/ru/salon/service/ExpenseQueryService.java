package ru.salon.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.salon.dto.ExpenseCriteria;
import ru.salon.model.Expense;
import ru.salon.repository.ExpenseRepository;

@Service
@AllArgsConstructor
public class ExpenseQueryService {

    private ExpenseRepository expenseRepository;

    @Transactional(readOnly = true)
    public Page<Expense> findEntityByCriteria(ExpenseCriteria criteria, Pageable pageable) {
        return expenseRepository.findAll(createSpecification(criteria), pageable);
    }

    private Specification<Expense> createSpecification(ExpenseCriteria criteria) {
        Specification<Expense> specification = Specification.where(null);
        if (criteria == null) {
            return specification;
        }
        if (criteria.getMasterId() != null) {
            specification = specification.and(expenseWithMaster(criteria.getMasterId()));
        }
        if (criteria.getProductId() != null) {
            specification = specification.and(expenseWithProduct(criteria.getProductId()));
        }
        return specification;
    }

    private static Specification<Expense> expenseWithMaster(final Long masterId) {
        return (r, cq, cb) -> cb.equal(r.get("master").get("id"), masterId);
    }

    private static Specification<Expense> expenseWithProduct(final Long productId) {
        return (r, cq, cb) -> cb.equal(r.get("product").get("id"), productId);
    }
}
