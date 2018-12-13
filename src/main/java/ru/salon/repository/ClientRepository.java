package ru.salon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.salon.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("Select c from Client c where c.person.name like %:fio% or c.person.surname like %:fio% or c.person.patronymic like %:fio% order by c.person.name")
    Page<Client> findByNameOrSurnameOrPatronymicContaining(@Param("fio")String fio, Pageable pageable);

    @Query("Select c from Client c where c.person.phone like %:phone% order by c.person.phone")
    Page<Client> findByPhoneContaining(@Param("phone")String phone, Pageable pageable);
}
