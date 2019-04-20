package ru.salon.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.salon.dto.AdditionalIncomeCriteria;
import ru.salon.dto.ExpenseCriteria;
import ru.salon.dto.IncomingCriteria;
import ru.salon.dto.StaticData;
import ru.salon.service.ReportService;
import ru.salon.service.excel.AdditionalIncomeExcelService;
import ru.salon.service.excel.ExpenseExcelService;
import ru.salon.service.excel.IncomingExcelService;
import ru.salon.service.excel.ReportExcelService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static ru.salon.utils.Utils.FORMATTER;

@RestController
@AllArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private ReportExcelService reportExcelService;
    private ExpenseExcelService expenseExcelService;
    private IncomingExcelService incomingExcelService;
    private AdditionalIncomeExcelService additionalIncomeExcelService;
    private ReportService reportService;

    @GetMapping("/getAdditionalIncomingReport")
    public ResponseEntity<InputStreamResource> getAdditionalIncomingReport(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String start,
                                                                 @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String end,
                                                                 @RequestParam(name = "masterId", required = false) Long masterId,
                                                                 Sort sort) throws IOException {
        Instant startSlot = LocalDateTime.parse(start, FORMATTER).atZone(ZoneId.of("+0")).toInstant();
        Instant endSlot = LocalDateTime.parse(end, FORMATTER).atZone(ZoneId.of("+0")).toInstant();
        ByteArrayInputStream in = additionalIncomeExcelService.getReport(AdditionalIncomeCriteria.builder()
                .masterId(masterId)
                .start(startSlot)
                .end(endSlot).build(), sort);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=отчет.xlsx");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    @GetMapping("/getIncomingReport")
    public ResponseEntity<InputStreamResource> getIncomingReport(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String start,
                                                                 @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String end,
                                                                 @RequestParam(name = "productId", required = false) Long productId,
                                                                 Sort sort) throws IOException {
        Instant startSlot = LocalDateTime.parse(start, FORMATTER).atZone(ZoneId.of("+0")).toInstant();
        Instant endSlot = LocalDateTime.parse(end, FORMATTER).atZone(ZoneId.of("+0")).toInstant();
        ByteArrayInputStream in = incomingExcelService.getReport(IncomingCriteria.builder()
                .productId(productId)
                .start(startSlot)
                .end(endSlot).build(), sort);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=отчет.xlsx");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    @GetMapping("/getExpensesReport")
    public ResponseEntity<InputStreamResource> getExpensesReport(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String start,
                                                                 @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String end,
                                                                 @RequestParam(name = "masterId", required = false) Long masterId,
                                                                 @RequestParam(name = "productId", required = false) Long productId,
                                                                 Sort sort) throws IOException {
        Instant startSlot = LocalDateTime.parse(start, FORMATTER).atZone(ZoneId.of("+0")).toInstant();
        Instant endSlot = LocalDateTime.parse(end, FORMATTER).atZone(ZoneId.of("+0")).toInstant();
        ByteArrayInputStream in = expenseExcelService.getReport(ExpenseCriteria.builder()
                .masterId(masterId)
                .productId(productId)
                .start(startSlot)
                .end(endSlot).build(), sort);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=отчет.xlsx");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    @GetMapping("/getMastersReport")
    public ResponseEntity<InputStreamResource> getMastersReport(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String start,
                                                                 @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String end) throws IOException {
        Instant startSlot = LocalDateTime.parse(start, FORMATTER).atZone(ZoneId.of("+0")).toInstant();
        Instant endSlot = LocalDateTime.parse(end, FORMATTER).atZone(ZoneId.of("+0")).toInstant();
        ByteArrayInputStream in = reportExcelService.writeIntoExcel(startSlot, endSlot);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=отчет.xlsx");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }

    @GetMapping("/getStatisticMastersReport")
    public ResponseEntity<StaticData> getStatisticMastersReport(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String start,
                                                                @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String end,
                                                                @RequestParam(name = "masterId", required = false) Long masterId) {
        return ResponseEntity
                .ok()
                .body(reportService.getStatisticBetweenDate(LocalDateTime.parse(start, FORMATTER).atZone(ZoneId.of("+0")).toInstant(),
                        LocalDateTime.parse(end, FORMATTER).atZone(ZoneId.of("+0")).toInstant(), masterId));
    }

    @GetMapping("/getIncomesBetweenDate")
    public ResponseEntity<StaticData> getIncomesBetweenDate(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String start,
                                                                @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) String end,
                                                                @RequestParam(name = "masterId", required = false) Long masterId) {
        return ResponseEntity
                .ok()
                .body(reportService.getIncomesBetweenDate(LocalDateTime.parse(start, FORMATTER).atZone(ZoneId.of("+0")).toInstant(),
                        LocalDateTime.parse(end, FORMATTER).atZone(ZoneId.of("+0")).toInstant(), masterId));
    }
}
