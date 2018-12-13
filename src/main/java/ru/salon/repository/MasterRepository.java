package ru.salon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.salon.model.Master;

@Repository
public interface MasterRepository extends JpaRepository<Master, Long> {

    @Query("Select c from Master c where c.person.name like %:fio% or c.person.surname like %:fio% or c.person.patronymic like %:fio% order by c.person.name")
    Page<Master> findByNameOrSurnameOrPatronymicContaining(@Param("fio")String fio, Pageable pageable);

}
