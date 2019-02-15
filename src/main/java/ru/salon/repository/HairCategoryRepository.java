package ru.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.salon.model.HairCategory;

@Repository
public interface HairCategoryRepository extends JpaRepository<HairCategory, Long> {
}
