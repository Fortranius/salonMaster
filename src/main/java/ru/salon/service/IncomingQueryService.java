package ru.salon.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.salon.dto.IncomingCriteria;
import ru.salon.model.Incoming;
import ru.salon.repository.IncomingRepository;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class IncomingQueryService {

    private IncomingRepository incomingRepository;

    @Transactional(readOnly = true)
    public Page<Incoming> findEntityByCriteria(IncomingCriteria criteria, Pageable pageable) {
        return incomingRepository.findAll(createSpecification(criteria), pageable);
    }

    @Transactional(readOnly = true)
    public List<Incoming> findAllEntityByCriteria(IncomingCriteria criteria, Sort sort) {
        return incomingRepository.findAll(createSpecification(criteria), sort);
    }

    private Specification<Incoming> createSpecification(IncomingCriteria criteria) {
        Specification<Incoming> specification = Specification.where(null);
        if (criteria == null) {
            return specification;
        }
        if (criteria.getProductId() != null) {
            specification = specification.and(incomingWithProduct(criteria.getProductId()));
        }
        if (criteria.getStart() != null) {
            specification = specification.and(incomingWithDate(criteria.getStart(), criteria.getEnd()));
        }
        return specification;
    }

    private static Specification<Incoming> incomingWithProduct(final Long productId) {
        return (r, cq, cb) -> cb.equal(r.get("product").get("id"), productId);
    }

    private static Specification<Incoming> incomingWithDate(final Instant start, final Instant end) {
        return (r, cq, cb) -> cb.and(
                cb.greaterThan(r.get("date"), start),
                cb.lessThan(r.get("date"), end)
        );
    }
}
