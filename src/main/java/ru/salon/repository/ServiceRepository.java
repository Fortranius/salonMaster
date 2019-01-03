package ru.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.salon.model.Service;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {

}
