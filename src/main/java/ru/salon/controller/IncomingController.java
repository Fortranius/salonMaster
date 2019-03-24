package ru.salon.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.salon.dto.IncomingCriteria;
import ru.salon.model.Incoming;
import ru.salon.repository.IncomingRepository;
import ru.salon.service.IncomingQueryService;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static ru.salon.utils.Utils.FORMATTER;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class IncomingController {

    private IncomingRepository incomingRepository;
    private IncomingQueryService incomingQueryService;

    @GetMapping("/incomings")
    public Page<Incoming> getIncomingBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String start,
                                            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String end,
                                            @RequestParam(name = "productId", required = false) Long productId,
                                            Pageable pageable) {
        Instant startSlot = LocalDateTime.parse(start, FORMATTER).atZone(ZoneId.of("+0")).toInstant();
        Instant endSlot = LocalDateTime.parse(end, FORMATTER).atZone(ZoneId.of("+0")).toInstant();
        return incomingQueryService.findEntityByCriteria(IncomingCriteria.builder()
                .productId(productId)
                .start(startSlot)
                .end(endSlot).build(), pageable);
    }

    @PutMapping("/incoming")
    public Incoming updateIncoming(@RequestBody Incoming incoming) {
        return incomingRepository.save(incoming);
    }

    @PostMapping("/incoming")
    public Incoming createIncoming(@Valid @RequestBody Incoming incoming) {
        return incomingRepository.save(incoming);
    }
}
