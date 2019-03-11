package ru.salon.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.salon.model.Procedure;
import ru.salon.model.enumiration.HairType;
import ru.salon.repository.ProcedureRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProcedureService {

    private ProcedureRepository procedureRepository;

    @PostConstruct
    public void init() {
        List<Procedure> procedures = new ArrayList<>();
        procedures.add(Procedure.builder().id(1000L).description("Наращивание волос").hairType(HairType.HAIR_EXTENSION).build());
        procedures.add(Procedure.builder().id(1001L).description("Снятие волос").hairType(HairType.HAIR_REMOVAL).build());
        procedureRepository.saveAll(procedures);
    }
}
