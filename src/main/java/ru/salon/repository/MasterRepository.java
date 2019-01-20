package ru.salon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.salon.model.Master;

import java.util.List;

@Repository
public interface MasterRepository extends JpaRepository<Master, Long> {

    @Query("Select c from Master c where c.person.name like %:name% order by c.person.name")
    List<Master> findByNameContaining(@Param("name")String name);

}
