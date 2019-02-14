package ru.salon.controller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.salon.service.ReportExcelService;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private ReportExcelService reportExcelService;

    @GetMapping("/getMastersReport")
    public ResponseEntity<InputStreamResource> getMastersReport() throws IOException {
        ByteArrayInputStream in = reportExcelService.writeIntoExcel();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=отчет.xlsx");
        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new InputStreamResource(in));
    }
}
