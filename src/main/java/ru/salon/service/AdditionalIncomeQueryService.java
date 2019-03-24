package ru.salon.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.salon.dto.AdditionalIncomeCriteria;
import ru.salon.model.AdditionalIncome;
import ru.salon.repository.AdditionalIncomeRepository;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class AdditionalIncomeQueryService {

    private AdditionalIncomeRepository additionalIncomeRepository;

    @Transactional(readOnly = true)
    public Page<AdditionalIncome> findEntityByCriteria(AdditionalIncomeCriteria criteria, Pageable pageable) {
        return additionalIncomeRepository.findAll(createSpecification(criteria), pageable);
    }

    @Transactional(readOnly = true)
    public List<AdditionalIncome> findAllEntityByCriteria(AdditionalIncomeCriteria criteria, Sort sort) {
        return additionalIncomeRepository.findAll(createSpecification(criteria), sort);
    }

    private Specification<AdditionalIncome> createSpecification(AdditionalIncomeCriteria criteria) {
        Specification<AdditionalIncome> specification = Specification.where(null);
        if (criteria == null) {
            return specification;
        }
        if (criteria.getMasterId() != null) {
            specification = specification.and(additionalIncomeWithMaster(criteria.getMasterId()));
        }
        if (criteria.getStart() != null) {
            specification = specification.and(additionalIncomeWithDate(criteria.getStart(), criteria.getEnd()));
        }
        return specification;
    }

    private static Specification<AdditionalIncome> additionalIncomeWithMaster(final Long masterId) {
        return (r, cq, cb) -> cb.equal(r.get("master").get("id"), masterId);
    }

    private static Specification<AdditionalIncome> additionalIncomeWithDate(final Instant start, final Instant end) {
        return (r, cq, cb) -> cb.and(
                cb.greaterThan(r.get("date"), start),
                cb.lessThan(r.get("date"), end)
        );
    }
}
