package ru.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.salon.model.Incoming;

import java.util.Optional;

@Repository
public interface IncomingRepository extends JpaRepository<Incoming, Long> {

    @Query("SELECT sum(incoming.countProduct) from Incoming incoming where incoming.product.id = :productId")
    Optional<Long> sumCountProduct(@Param("productId") Long productId);
}
