package ru.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.salon.model.Client;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("Select c from Client c where c.person.name like %:filter% or c.person.surname like %:filter% or c.person.patronymic like %:filter%")
    List<Client> findByNameOrSurnameOrPatronymicContaining(@Param("filter")String filter);

    @Query("Select c from Client c where c.person.phone like %:phone%")
    List<Client> findByPhoneContaining(@Param("phone")String phone);
}
