package ru.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.salon.model.Hair;

import java.util.List;

@Repository
public interface HairRepository extends JpaRepository<Hair, Long> {

    List<Hair> findAllByOrderByMinLength();
}
