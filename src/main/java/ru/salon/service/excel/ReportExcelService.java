package ru.salon.service.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.salon.model.Master;
import ru.salon.model.TimeSlot;
import ru.salon.repository.MasterRepository;
import ru.salon.repository.TimeSlotRepository;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static ru.salon.utils.Utils.DATE_FORMATTER;

@Service
public class ReportExcelService {

    private Sheet sheet;
    private int rowCount;

    private MasterRepository masterRepository;
    private TimeSlotRepository timeSlotRepository;

    public ReportExcelService(MasterRepository masterRepository, TimeSlotRepository timeSlotRepository) {
        this.masterRepository = masterRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    public ByteArrayInputStream  writeIntoExcel(Instant start, Instant end) throws IOException{
        try(
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            List<Master> masters = masterRepository.findAll();
            sheet = workbook.createSheet("Результат");
            generateTitle(start, end);
            Row rowMaster = sheet.createRow(2);
            Row rowSalary = sheet.createRow(3);
            rowMaster.createCell(0).setCellValue("Мастер");
            generateDaysColumn(start, end);

            int cellNumber = 1;
            for (Master master: masters) {
                generateMasterTitle(rowMaster, rowSalary, cellNumber, master);
                generateSum(start, end, master, cellNumber);
                cellNumber = cellNumber + 2;
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private void generateSum(Instant start, Instant end, Master master, int cellNumber) {
        int rowNumber = 4;
        Instant currentDateStart = start;
        Instant currentDateEnd = start.plus(1, ChronoUnit.DAYS);
        while (currentDateStart.isBefore(end)) {
            generateAllSum(currentDateStart, currentDateEnd, master, cellNumber, rowNumber);
            rowNumber++;
            currentDateStart = currentDateStart.plus(1, ChronoUnit.DAYS);
            currentDateEnd = currentDateEnd.plus(1, ChronoUnit.DAYS);
        }

        generateAllSum(start, end, master, cellNumber, rowCount);
    }

    private void generateAllSum(Instant start, Instant end, Master master, int cellNumber, int rowNumber) {

        List<TimeSlot> timeSlots = timeSlotRepository
                .findByStartSlotBetweenAndMaster(start, end, master);

        BigDecimal sumIncome = timeSlots.stream().map(TimeSlot::getAllPrice).collect(Collectors.toList())
                .stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sumIncomeMaster = timeSlots.stream().map(TimeSlot::getMasterWorkPrice).collect(Collectors.toList())
                .stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(2), 2);
        sheet.getRow(rowNumber).createCell(cellNumber)
                .setCellValue(sumIncome.toString());
        sheet.getRow(rowNumber).createCell(cellNumber + 1)
                .setCellValue(sumIncomeMaster.toString());
    }

    private void generateMasterTitle(Row rowMaster,
                                     Row rowSalary,
                                     int cellNumber,
                                     Master master) {
        Cell masterCell = rowMaster.createCell(cellNumber);
        masterCell.setCellValue(master.getPerson().getName());
        masterCell.setCellStyle(masterStyle(rowMaster));

        CellRangeAddress range = new CellRangeAddress(2, 2, cellNumber, cellNumber + 1);
        sheet.addMergedRegion(range);
        RegionUtil.setBorderTop(2, range, sheet);
        RegionUtil.setBorderLeft(2, range, sheet);
        RegionUtil.setBorderRight(2, range, sheet);

        Cell serviceCell = rowSalary.createCell(cellNumber);
        serviceCell.setCellValue("услуги");
        serviceCell.setCellStyle(salaryDescriptionLeftStyle(rowSalary));

        Cell salaryCell = rowSalary.createCell(cellNumber+1);
        salaryCell.setCellValue("з/пл");
        salaryCell.setCellStyle(salaryDescriptionRightStyle(rowSalary));
    }

    private void generateTitle(Instant start, Instant end) {
        sheet.setColumnWidth(0, 4000);

        sheet.createRow(0).createCell(0)
                .setCellValue("РАБОТА МАСТЕРОВ С "
                        + DATE_FORMATTER.format(start) + " ПО "
                        + DATE_FORMATTER.format(end.minus(1, ChronoUnit.DAYS)));

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
    }

    private void generateDaysColumn(Instant start, Instant end) {
        int rowNumber = 4;
        Instant currentDate = start;
        while (currentDate.isBefore(end)) {
            LocalDateTime date = LocalDateTime.ofInstant(currentDate, ZoneId.systemDefault());
            sheet.createRow(rowNumber).createCell(0)
                    .setCellValue(date.getDayOfMonth() + " "
                            + getMonth(date.getMonthValue()));
            rowNumber++;
            currentDate = currentDate.plus(1, ChronoUnit.DAYS);
        }

        rowCount = rowNumber;
        sheet.createRow(rowCount);
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

    private static String getMonth(int month) {
        switch (month) {
            case 1:
                return "января";
            case 2:
                return "февраля";
            case 3:
                return "марта";
            case 4:
                return "апреля";
            case 5:
                return "мая";
            case 6:
                return "июня";
            case 7:
                return "июля";
            case 8:
                return "августа";
            case 9:
                return "сентября";
            case 10:
                return "октября";
            case 11:
                return "ноября";
            case 12:
                return "декабря";
            default:
                return "января";
        }
    }
}
