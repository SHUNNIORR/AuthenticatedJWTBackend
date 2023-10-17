package com.unkownkoder.services;

import com.unkownkoder.models.Transaction;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;
@Service
public class FileServices {
    public String generateExcelBase64(List<Transaction> transactions) throws IOException {

        // Crea un libro de Excel.
        Workbook workbook = new XSSFWorkbook();

        // Crea una hoja de Excel.
        Sheet sheet = workbook.createSheet("Transactions");

        // Crea una fila de encabezado.
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Transaction ID");
        headerRow.createCell(1).setCellValue("Transaction Date");
        headerRow.createCell(2).setCellValue("Amount");
        headerRow.createCell(3).setCellValue("Transaction Type");
        headerRow.createCell(4).setCellValue("Source Account ID");
        headerRow.createCell(5).setCellValue("Target Account ID");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        // Crea una fila para cada transacción.
        for (int i = 0; i < transactions.size(); i++) {
            Row row = sheet.createRow(i + 1);

            Transaction transaction = transactions.get(i);
            row.createCell(0).setCellValue(transaction.getTransactionId());
            Cell dateCell = row.createCell(1);
            dateCell.setCellValue(dateFormat.format(transaction.getTransactionDate()));
            row.createCell(2).setCellValue(transaction.getAmount());
            row.createCell(3).setCellValue(transaction.getTransactionType().toString());
            row.createCell(4).setCellValue(transaction.getSourceAccount().getAccountId());
            row.createCell(5).setCellValue(transaction.getTargetAccount().getAccountId());
        }

        // Guarda el libro de Excel en un flujo de bytes.
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);

        // Convierte el flujo de bytes a una cadena Base64.
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String base64String = Base64.getEncoder().encodeToString(bytes);

        // Cierra el flujo de bytes.
        byteArrayOutputStream.close();

        // Devuelve la cadena Base64.
        return base64String;
    }
}
