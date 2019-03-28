package ru.salon.service.excel;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import ru.salon.model.Client;
import ru.salon.model.Person;
import ru.salon.repository.ClientRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

@Service
@AllArgsConstructor
public class ClientImportService {

    private final ClientRepository clientRepository;

    public void init() {
        try {
            readData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readData() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(ResourceUtils.getFile("classpath:clients.xlsx")));
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIt = sheet.iterator();
        rowIt.next();
        while(rowIt.hasNext()) {
            Row row = rowIt.next();

            Client client = new Client();
            Person person = new Person();
            person.setMail(row.getCell(2).toString());
            person.setName(row.getCell(0).toString());

            if (row.getCell(1).toString().replace(".","").length()>=11) {
                person.setPhone(row.getCell(1).toString().replace(".", "").substring(0, 11));

            } else {
                String cell = row.getCell(1).toString().replace(".", "");
                cell = cell.substring(0, cell.length()-3);
                cell = cell + "00000000000";
                cell = cell.substring(0, 11);
                person.setPhone(cell.substring(0, 11));
            }
            client.setPerson(person);
            clientRepository.save(client);
        }
        workbook.close();
    }
}
