package ru.salon.service.excel;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.salon.model.Master;
import ru.salon.repository.MasterRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static ru.salon.utils.Utils.DATE_FORMATTER;

@Service
@AllArgsConstructor
public class ReportExcelService {

    private MasterRepository masterRepository;

    public ByteArrayInputStream  writeIntoExcel(Instant start, Instant end) throws IOException{
        try(
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            List<Master> masters = masterRepository.findAll();

            Sheet sheet = workbook.createSheet("Результат");
            Row row = sheet.createRow(0);
            Cell name = row.createCell(0);
            name.setCellValue("РАБОТА МАСТЕРОВ С " + DATE_FORMATTER.format(start) + " ПО " + DATE_FORMATTER.format(end));

            Row rowMaster = sheet.createRow(2);
            Row rowSalary = sheet.createRow(3);
            rowMaster.createCell(0).setCellValue("Мастер");

            int cellNumber = 1;
            for (Master master: masters) {
                Cell masterCell = rowMaster.createCell(cellNumber);
                masterCell.setCellValue(master.getPerson().getName());
                masterCell.setCellStyle(masterStyle(rowMaster));

                CellRangeAddress range = new CellRangeAddress(2, 2, cellNumber, cellNumber + 3);
                sheet.addMergedRegion(range);
                RegionUtil.setBorderTop(2, range, sheet);
                RegionUtil.setBorderLeft(2, range, sheet);
                RegionUtil.setBorderRight(2, range, sheet);

                Cell serviceCell = rowSalary.createCell(cellNumber);
                serviceCell.setCellValue("услуги");
                serviceCell.setCellStyle(salaryDescriptionLeftStyle(rowSalary));

                Cell salaryCell = rowSalary.createCell(cellNumber+1);
                salaryCell.setCellValue("з/пл");
                salaryCell.setCellStyle(salaryDescriptionMiddleStyle(rowSalary));

                Cell hairCell = rowSalary.createCell(cellNumber+2);
                hairCell.setCellValue("волосы");
                hairCell.setCellStyle(salaryDescriptionMiddleStyle(rowSalary));

                Cell klayCell = rowSalary.createCell(cellNumber+3);
                klayCell.setCellValue("клей, шт");
                klayCell.setCellStyle(salaryDescriptionRightStyle(rowSalary));

                cellNumber = cellNumber + 4;
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private CellStyle masterStyle(Row row) {
        CellStyle cellStyleMaster = row.getSheet().getWorkbook().createCellStyle();
        cellStyleMaster.setAlignment(HorizontalAlignment.CENTER);
        return cellStyleMaster;
    }

    private CellStyle salaryDescriptionLeftStyle(Row row) {
        CellStyle cellStyleMaster = row.getSheet().getWorkbook().createCellStyle();
        cellStyleMaster.setBorderBottom(BorderStyle.MEDIUM);
        cellStyleMaster.setBorderLeft(BorderStyle.MEDIUM);
        cellStyleMaster.setAlignment(HorizontalAlignment.CENTER);
        return cellStyleMaster;
    }

    private CellStyle salaryDescriptionRightStyle(Row row) {
        CellStyle cellStyleMaster = row.getSheet().getWorkbook().createCellStyle();
        cellStyleMaster.setBorderBottom(BorderStyle.MEDIUM);
        cellStyleMaster.setBorderRight(BorderStyle.MEDIUM);
        cellStyleMaster.setAlignment(HorizontalAlignment.CENTER);
        return cellStyleMaster;
    }

    private CellStyle salaryDescriptionMiddleStyle(Row row) {
        CellStyle cellStyleMaster = row.getSheet().getWorkbook().createCellStyle();
        cellStyleMaster.setBorderBottom(BorderStyle.MEDIUM);
        cellStyleMaster.setAlignment(HorizontalAlignment.CENTER);
        return cellStyleMaster;
    }
}
