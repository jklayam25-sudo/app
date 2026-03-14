package lumi.insert.app.service.implement;
 
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle; 
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook; 

import lumi.insert.app.dto.response.SupplyResponse;
import lumi.insert.app.dto.response.TransactionResponse;
import lumi.insert.app.service.XlsxService;

public class XlsxServiceImpl implements XlsxService{

    @Override
    public void exportTransactions(List<TransactionResponse> datas, OutputStream outputStream) {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook()) { 
            SXSSFSheet sheet = workbook.createSheet("Transactions");
            SXSSFRow titleRow = sheet.createRow(0);
            SXSSFCell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Transaction List: ");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14)); 
            
            CellStyle defaultCenter = workbook.createCellStyle();
            defaultCenter.setAlignment(HorizontalAlignment.CENTER); 
            defaultCenter.setVerticalAlignment(VerticalAlignment.CENTER); 
            titleCell.setCellStyle(defaultCenter);

            SXSSFRow headerRow = sheet.createRow(1);
            headerRow.createCell(0).setCellValue("No");
            headerRow.createCell(1).setCellValue("Invoice");
            headerRow.createCell(2).setCellValue("Customer");
            headerRow.createCell(3).setCellValue("Total Item");
            headerRow.createCell(4).setCellValue("Fee");
            headerRow.createCell(5).setCellValue("Discount");
            headerRow.createCell(6).setCellValue("Sub Total");
            headerRow.createCell(7).setCellValue("Grand Total");
            headerRow.createCell(8).setCellValue("Unpaid");
            headerRow.createCell(9).setCellValue("Paid");
            headerRow.createCell(10).setCellValue("Unrefund");
            headerRow.createCell(11).setCellValue("Refund");
            headerRow.createCell(12).setCellValue("Status");
            headerRow.createCell(13).setCellValue("Transaction Date");
            headerRow.createCell(14).setCellValue("Last Update"); 

            int i = 2;
            for (TransactionResponse data : datas) {
                SXSSFRow row = sheet.createRow(i);  

                row.createCell(0).setCellValue(i);
                row.createCell(1).setCellValue(data.invoiceId());
                row.createCell(2).setCellValue(data.customerName());
                row.createCell(3).setCellValue(data.totalItems());
                row.createCell(4).setCellValue(data.totalFee());
                row.createCell(5).setCellValue(data.totalDiscount());
                row.createCell(6).setCellValue(data.subTotal());
                row.createCell(7).setCellValue(data.grandTotal());
                row.createCell(8).setCellValue(data.totalUnpaid());
                row.createCell(9).setCellValue(data.totalPaid());
                row.createCell(10).setCellValue(data.totalUnrefunded());
                row.createCell(11).setCellValue(data.totalRefunded());
                row.createCell(12).setCellValue(String.valueOf(data.status()));
                row.createCell(13).setCellValue(data.createdAt());
                row.createCell(14).setCellValue(data.updatedAt()); 

                i++;
            }
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("Rp#,##0"));  
            sheet.setDefaultColumnStyle(7, currencyStyle);

            sheet.createFreezePane(0, 1);
            workbook.write(outputStream);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void exportSupplies(List<SupplyResponse> datas, OutputStream outputStream) {
         try (SXSSFWorkbook workbook = new SXSSFWorkbook()) { 
            SXSSFSheet sheet = workbook.createSheet("Supplies");
            SXSSFRow titleRow = sheet.createRow(0);
            SXSSFCell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Supply List: ");
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 14)); 
            
            CellStyle defaultCenter = workbook.createCellStyle();
            defaultCenter.setAlignment(HorizontalAlignment.CENTER); 
            defaultCenter.setVerticalAlignment(VerticalAlignment.CENTER); 
            titleCell.setCellStyle(defaultCenter);

            SXSSFRow headerRow = sheet.createRow(1);
            headerRow.createCell(0).setCellValue("No");
            headerRow.createCell(1).setCellValue("Invoice");
            headerRow.createCell(2).setCellValue("Supplier");
            headerRow.createCell(3).setCellValue("Total Item");
            headerRow.createCell(4).setCellValue("Fee");
            headerRow.createCell(5).setCellValue("Discount");
            headerRow.createCell(6).setCellValue("Sub Total");
            headerRow.createCell(7).setCellValue("Grand Total");
            headerRow.createCell(8).setCellValue("Unpaid");
            headerRow.createCell(9).setCellValue("Paid");
            headerRow.createCell(10).setCellValue("Unrefund");
            headerRow.createCell(11).setCellValue("Refund");
            headerRow.createCell(12).setCellValue("Status");
            headerRow.createCell(13).setCellValue("Transaction Date"); 

            int i = 2;
            for (SupplyResponse data : datas) {
                SXSSFRow row = sheet.createRow(i);  

                row.createCell(0).setCellValue(i);
                row.createCell(1).setCellValue(data.invoiceId());
                row.createCell(2).setCellValue(data.supplierName());
                row.createCell(3).setCellValue(data.totalItems());
                row.createCell(4).setCellValue(data.totalFee());
                row.createCell(5).setCellValue(data.totalDiscount());
                row.createCell(6).setCellValue(data.subTotal());
                row.createCell(7).setCellValue(data.grandTotal());
                row.createCell(8).setCellValue(data.totalUnpaid());
                row.createCell(9).setCellValue(data.totalPaid());
                row.createCell(10).setCellValue(data.totalUnrefunded());
                row.createCell(11).setCellValue(data.totalRefunded());
                row.createCell(12).setCellValue(String.valueOf(data.status()));
                row.createCell(13).setCellValue(data.createdAt()); 

                i++;
            }
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("Rp#,##0"));  
            sheet.setDefaultColumnStyle(7, currencyStyle);

            sheet.createFreezePane(0, 1);
            workbook.write(outputStream);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
}
