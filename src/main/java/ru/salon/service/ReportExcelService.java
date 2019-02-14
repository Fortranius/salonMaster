package ru.salon.service;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.salon.model.Master;
import ru.salon.repository.MasterRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class ReportExcelService {

    private MasterRepository masterRepository;

    public ByteArrayInputStream  writeIntoExcel() throws IOException{
        try(
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            List<Master> masters = masterRepository.findAll();

            Sheet sheet = workbook.createSheet("Результат");
            Row row = sheet.createRow(0);
            Cell name = row.createCell(0);
            name.setCellValue("РАБОТА МАСТЕРОВ");

            Row rowMaster = sheet.createRow(2);
            Row rowSalary = sheet.createRow(3);
            rowMaster.createCell(0).setCellValue("Мастер");

            CellStyle cellStyle = rowMaster.getSheet().getWorkbook().createCellStyle();
            cellStyle.setBorderTop(BorderStyle.DOUBLE);
            cellStyle.setBorderLeft(BorderStyle.DOUBLE);
            cellStyle.setBorderRight(BorderStyle.DOUBLE);

            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            int cellNumber = 1;
            for (Master master: masters) {
                Cell masterCell = rowMaster.createCell(cellNumber);
                masterCell.setCellValue(master.getPerson().getName());
                masterCell.setCellStyle(cellStyle);
                sheet.addMergedRegion(new CellRangeAddress(2, 2, cellNumber, cellNumber + 3));

                rowSalary.createCell(cellNumber).setCellValue("услуги");
                rowSalary.createCell(cellNumber+1).setCellValue("з/пл");
                rowSalary.createCell(cellNumber+2).setCellValue("волосы");
                rowSalary.createCell(cellNumber+3).setCellValue("клей, шт");

                cellNumber = cellNumber + 4;
            }



            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
