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
import ru.salon.dto.IncomingCriteria;
import ru.salon.model.Incoming;
import ru.salon.model.Product;
import ru.salon.service.IncomingQueryService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static ru.salon.utils.Utils.DATE_FORMATTER;

@Service
@AllArgsConstructor
public class IncomingExcelService {

    private IncomingQueryService incomingQueryService;

    public ByteArrayInputStream getReport(IncomingCriteria criteria, Sort sort) throws IOException {
        try(
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            List<Incoming> incomings = incomingQueryService.findAllEntityByCriteria(criteria, sort);

            reportAllExpense(workbook, criteria, incomings);
            reportAllIncomingByProduct(workbook, incomings);
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private void reportAllExpense(Workbook workbook, IncomingCriteria criteria,  List<Incoming> incomings) {
        Sheet sheet = workbook.createSheet("Детализация прихода");

        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4500);
        sheet.setColumnWidth(2, 3500);

        Row row = sheet.createRow(0);
        Cell name = row.createCell(0);

        CellRangeAddress range = new CellRangeAddress(0, 0, 0, 4);
        sheet.addMergedRegion(range);

        name.setCellValue("Приход за период с " + DATE_FORMATTER.format(criteria.getStart()) + " по " + DATE_FORMATTER.format(criteria.getEnd()));

        Row title = sheet.createRow(2);
        title.createCell(0).setCellValue("Продукт");
        title.createCell(1).setCellValue("Количество");
        title.createCell(2).setCellValue("Дата прихода");

        int rowNumber = 3;
        for (Incoming incoming: incomings) {
            Row rowExpense = sheet.createRow(rowNumber);
            rowExpense.createCell(0).setCellValue(incoming.getProduct().getDescription());
            rowExpense.createCell(1).setCellValue(incoming.getCountProduct());
            rowExpense.createCell(2).setCellValue(DATE_FORMATTER.format(incoming.getDate()));
            rowNumber++;
        }
    }

    private void reportAllIncomingByProduct(Workbook workbook, List<Incoming> incomings) {
        Sheet sheet = workbook.createSheet("Детализация прихода по товарам");

        Row title = sheet.createRow(0);
        title.createCell(0).setCellValue("Продукт");
        title.createCell(1).setCellValue("Количество");

        List<Product> products = incomings.stream().map(Incoming::getProduct).distinct().collect(Collectors.toList());
        int rowNumber = 1;
        for (Product product: products) {
            Row rowMaster = sheet.createRow(rowNumber);
            rowMaster.createCell(0).setCellValue(product.getDescription());

            BigDecimal countProduct = incomings
                    .stream().filter(elem -> elem.getProduct().getId().equals(product.getId()))
                    .map(incoming -> BigDecimal.valueOf(incoming.getCountProduct())).collect(Collectors.toList())
                    .stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            rowMaster.createCell(1).setCellValue(countProduct.toString());
            rowNumber++;
        }
    }
}
