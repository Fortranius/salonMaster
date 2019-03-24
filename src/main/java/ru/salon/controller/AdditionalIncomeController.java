package ru.salon.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.salon.dto.AdditionalIncomeCriteria;
import ru.salon.model.AdditionalIncome;
import ru.salon.repository.AdditionalIncomeRepository;
import ru.salon.service.AdditionalIncomeQueryService;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static ru.salon.utils.Utils.FORMATTER;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AdditionalIncomeController {

    private AdditionalIncomeRepository additionalIncomeRepository;
    private AdditionalIncomeQueryService additionalIncomeQueryService;

    @GetMapping("/additionalIncomes")
    public Page<AdditionalIncome> getExpensesBetween(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String start,
                                            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String end,
                                            @RequestParam(name = "masterId", required = false) Long masterId,
                                            Pageable pageable) {
        Instant startSlot = LocalDateTime.parse(start, FORMATTER).atZone(ZoneId.of("+0")).toInstant();
        Instant endSlot = LocalDateTime.parse(end, FORMATTER).atZone(ZoneId.of("+0")).toInstant();
        return additionalIncomeQueryService.findEntityByCriteria(AdditionalIncomeCriteria.builder()
                .masterId(masterId)
                .start(startSlot)
                .end(endSlot).build(), pageable);
    }

    @PostMapping("/additionalIncome")
    public AdditionalIncome createExpense(@Valid @RequestBody AdditionalIncome additionalIncome) {
        return additionalIncomeRepository.save(additionalIncome);
    }

    @PutMapping("/additionalIncome")
    public AdditionalIncome updateExpense(@RequestBody AdditionalIncome additionalIncome) {
        return additionalIncomeRepository.save(additionalIncome);
    }
}
