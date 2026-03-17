package lumi.insert.app.service.xlsx;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.f4b6a3.uuid.UuidCreator;

import lumi.insert.app.core.entity.nondatabase.SupplyStatus;
import lumi.insert.app.core.entity.nondatabase.TransactionStatus;
import lumi.insert.app.dto.response.SupplyResponse;
import lumi.insert.app.dto.response.TransactionResponse;
import lumi.insert.app.service.implement.XlsxServiceImpl;

@ExtendWith(MockitoExtension.class)
public class XlsxServiceTest {
    
    @InjectMocks
    XlsxServiceImpl xlsxServiceImpl;

    @Test
    void exportTransactions_validData_returnStream() throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        TransactionResponse transactionResponse = new TransactionResponse(UuidCreator.getTimeOrderedEpochFast(), 
            "INV-012", 
            null, 
            "TEST LTE.", 
            1L, 
            2L, 
            3L, 
            4L, 
            5L, 
            6L, 
            7L, 
            8L,
             9L, 
             TransactionStatus.COMPLETE, 
             null, 
             null, 
             LocalDateTime.now(), 
             LocalDateTime.now()
            );

        xlsxServiceImpl.exportTransactions(List.of(transactionResponse, transactionResponse), out);

        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new ByteArrayInputStream(out.toByteArray()));

        XSSFSheet sheet = xssfWorkbook.getSheet("Transaction");
        assertEquals(transactionResponse.invoiceId(), sheet.getRow(2).getCell(1).getStringCellValue()); 
        assertEquals(transactionResponse.status().toString(), sheet.getRow(2).getCell(12).getStringCellValue()); 
        assertEquals(transactionResponse.status().toString(), sheet.getRow(3).getCell(12).getStringCellValue());
        xssfWorkbook.close();
    }

    @Test
    void exportSupplies_validData_returnStream() throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        SupplyResponse supplyResponse = new SupplyResponse(UuidCreator.getTimeOrderedEpochFast(), 
            "INV-012", 
            null ,
            "TEST LTE.", 
            2L, 
            3L, 
            4L, 
            5L, 
            6L, 
            7L, 
            8L,
             9L,
             10L, 
             SupplyStatus.COMPLETE, 
             null, 
             null, 
             LocalDateTime.now()
            );

        xlsxServiceImpl.exportSupplies(List.of(supplyResponse, supplyResponse), out);

        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new ByteArrayInputStream(out.toByteArray()));

        XSSFSheet sheet = xssfWorkbook.getSheet("Supplies");
        assertEquals(supplyResponse.invoiceId(), sheet.getRow(2).getCell(1).getStringCellValue()); 
        assertEquals(supplyResponse.status().toString(), sheet.getRow(2).getCell(12).getStringCellValue()); 
        assertEquals(supplyResponse.status().toString(), sheet.getRow(3).getCell(12).getStringCellValue());
        xssfWorkbook.close();
    }
}
