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
import ru.salon.dto.ExpenseCriteria;
import ru.salon.model.Expense;
import ru.salon.model.Master;
import ru.salon.model.Product;
import ru.salon.service.ExpenseQueryService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static ru.salon.utils.Utils.DATE_FORMATTER;

@Service
@AllArgsConstructor
public class ExpenseExcelService {

    private ExpenseQueryService expenseQueryService;

    public ByteArrayInputStream getReport(ExpenseCriteria criteria, Sort sort) throws IOException {
        try(
                Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream out = new ByteArrayOutputStream();
        ) {
            List<Expense> expenses = expenseQueryService.findAllEntityByCriteria(criteria, sort);

            reportAllExpense(workbook, criteria, expenses);
            reportAllExpenseByMaster(workbook, expenses);
            reportAllExpenseByProduct(workbook, expenses);
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

    private void reportAllExpense(Workbook workbook, ExpenseCriteria criteria, List<Expense> expenses) {
        Sheet sheet = workbook.createSheet("Детализация расходов");

        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4500);
        sheet.setColumnWidth(2, 3500);
        sheet.setColumnWidth(3, 4500);
        sheet.setColumnWidth(4, 4500);

        Row row = sheet.createRow(0);
        Cell name = row.createCell(0);

        CellRangeAddress range = new CellRangeAddress(0, 0, 0, 4);
        sheet.addMergedRegion(range);

        name.setCellValue("Расходы за период с " + DATE_FORMATTER.format(criteria.getStart()) + " по " + DATE_FORMATTER.format(criteria.getEnd()));

        Row title = sheet.createRow(2);
        title.createCell(0).setCellValue("Мастер");
        title.createCell(1).setCellValue("Продукт");
        title.createCell(2).setCellValue("Количество");
        title.createCell(3).setCellValue("Дата расхода");
        title.createCell(4).setCellValue("Сумма расхода");

        int rowNumber = 3;
        for (Expense expense: expenses) {
            Row rowExpense = sheet.createRow(rowNumber);
            rowExpense.createCell(0).setCellValue(expense.getMaster().getPerson().getName());
            rowExpense.createCell(1).setCellValue(expense.getProduct().getDescription());
            rowExpense.createCell(2).setCellValue(expense.getCountProduct());
            rowExpense.createCell(3).setCellValue(DATE_FORMATTER.format(expense.getDate()));
            rowExpense.createCell(4).setCellValue(expense.getProduct().getPurchasePrice().multiply(BigDecimal.valueOf(expense.getCountProduct())).toString());
            rowNumber++;
        }

        BigDecimal sumExpense = expenses
                .stream().map(expense -> expense.getProduct().getPurchasePrice().multiply(BigDecimal.valueOf(expense.getCountProduct()))).collect(Collectors.toList())
                .stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        Row rowAll = sheet.createRow(++rowNumber);
        rowAll.createCell(3).setCellValue("Общая сумма");
        rowAll.createCell(4).setCellValue(sumExpense.toString());
    }

    private void reportAllExpenseByMaster(Workbook workbook, List<Expense> expenses) {
        Sheet sheet = workbook.createSheet("Детализация расходов по мастерам");
        initSheet(sheet, "Мастер");

        List<Master> masters = expenses.stream().map(Expense::getMaster).distinct().collect(Collectors.toList());
        int rowNumber = 1;
        for (Master master: masters) {
            Row rowMaster = sheet.createRow(rowNumber);
            rowMaster.createCell(0).setCellValue(master.getPerson().getName());

            BigDecimal sumExpenseByMaster = expenses
                    .stream().filter(elem -> elem.getMaster().getId().equals(master.getId()))
                    .map(expense -> expense.getProduct().getPurchasePrice().multiply(BigDecimal.valueOf(expense.getCountProduct()))).collect(Collectors.toList())
                    .stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            rowMaster.createCell(1).setCellValue(sumExpenseByMaster.toString());
            rowNumber++;
        }
    }

    private void reportAllExpenseByProduct(Workbook workbook, List<Expense> expenses) {
        Sheet sheet = workbook.createSheet("Детализация расходов по товарам");
        initSheet(sheet, "Продукт");

        List<Product> products = expenses.stream().map(Expense::getProduct).distinct().collect(Collectors.toList());
        int rowNumber = 1;
        for (Product product: products) {
            Row rowMaster = sheet.createRow(rowNumber);
            rowMaster.createCell(0).setCellValue(product.getDescription());

            BigDecimal sumExpenseByMaster = expenses
                    .stream().filter(elem -> elem.getProduct().getId().equals(product.getId()))
                    .map(expense -> expense.getProduct().getPurchasePrice().multiply(BigDecimal.valueOf(expense.getCountProduct()))).collect(Collectors.toList())
                    .stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            rowMaster.createCell(1).setCellValue(sumExpenseByMaster.toString());
            rowNumber++;
        }
    }

    private void initSheet(Sheet sheet, String cellName) {
        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4500);
        Row title = sheet.createRow(0);
        title.createCell(0).setCellValue(cellName);
        title.createCell(1).setCellValue("Сумма расходов");
    }
}
