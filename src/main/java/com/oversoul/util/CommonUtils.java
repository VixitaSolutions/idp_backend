package com.oversoul.util;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class CommonUtils {

    public static XSSFWorkbook convertCsvToXlsx(MultipartFile fileToUpload) throws IOException {

        try (XSSFWorkbook workBook = new XSSFWorkbook()) {
            XSSFSheet sheet = workBook.createSheet("sheet1");
            String currentLine = null;
            int rowNum = 0;
            BufferedReader br = new BufferedReader(new InputStreamReader(fileToUpload.getInputStream()));
            while ((currentLine = br.readLine()) != null) {
                String[] str = currentLine.split(",");
                XSSFRow currentRow = sheet.createRow(rowNum);
                for (int i = 0; i < str.length; i++) {
                    currentRow.createCell(i).setCellValue(str[i]);
                }
                rowNum++;
            }
            br.close();
            final File file = new File(System.getProperty("java.io.tmpdir"), fileToUpload.getOriginalFilename() + ".xlsx");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workBook.write(fileOutputStream);
            fileOutputStream.close();
            return workBook;
        }
    }
}
