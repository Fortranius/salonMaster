package ru.salon.service.excel;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.salon.dto.AdditionalIncomeCriteria;
import ru.salon.model.AdditionalIncome;
import ru.salon.model.Master;
import ru.salon.service.AdditionalIncomeQueryService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static ru.salon.utils.Utils.DATE_FORMATTER;

@Service
@AllArgsConstructor
public class AdditionalIncomeExcelService {

    private AdditionalIncomeQueryService additionalIncomeQueryService;

    public ByteArrayInputStream getReport(AdditionalIncomeCriteria criteria, Sort sort) throws IOException {
        try(
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            List<AdditionalIncome> incomings = additionalIncomeQueryService.findAllEntityByCriteria(criteria, sort);

            reportAllIncome(workbook, criteria, incomings);
            reportAllIncomingByMaster(workbook, incomings);
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private void reportAllIncome(Workbook workbook, AdditionalIncomeCriteria criteria,  List<AdditionalIncome> incomings) {
        Sheet sheet = workbook.createSheet("Детализация дополнительного заработка мастеров");

        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4500);
        sheet.setColumnWidth(2, 3500);

        Row row = sheet.createRow(0);
        Cell name = row.createCell(0);

        CellRangeAddress range = new CellRangeAddress(0, 0, 0, 4);
        sheet.addMergedRegion(range);

        name.setCellValue("Заработок за период с " + DATE_FORMATTER.format(criteria.getStart()) + " по " + DATE_FORMATTER.format(criteria.getEnd()));

        Row title = sheet.createRow(2);
        title.createCell(0).setCellValue("Мастер");
        title.createCell(1).setCellValue("Сумма");
        title.createCell(2).setCellValue("Дата");

        int rowNumber = 3;
        for (AdditionalIncome income: incomings) {
            Row rowExpense = sheet.createRow(rowNumber);
            rowExpense.createCell(0).setCellValue(income.getMaster().getPerson().getName());
            rowExpense.createCell(1).setCellValue(income.getSum().toString());
            rowExpense.createCell(2).setCellValue(DATE_FORMATTER.format(income.getDate()));
            rowNumber++;
        }
    }

    private void reportAllIncomingByMaster(Workbook workbook, List<AdditionalIncome> incomings) {
        Sheet sheet = workbook.createSheet("Детализация по мастерам");

        Row title = sheet.createRow(0);
        title.createCell(0).setCellValue("Мастер");
        title.createCell(1).setCellValue("Сумма");

        List<Master> masters = incomings.stream().map(AdditionalIncome::getMaster).distinct().collect(Collectors.toList());
        int rowNumber = 1;
        for (Master master: masters) {
            Row rowMaster = sheet.createRow(rowNumber);
            rowMaster.createCell(0).setCellValue(master.getPerson().getName());

            BigDecimal sum = incomings
                    .stream().filter(elem -> elem.getMaster().getId().equals(master.getId()))
                    .map(AdditionalIncome::getSum).collect(Collectors.toList())
                    .stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            rowMaster.createCell(1).setCellValue(sum.toString());
            rowNumber++;
        }
    }
}
