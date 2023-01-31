package com.oversoul.Helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.oversoul.entity.CompetencyMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "Id", "cName", "cLevel", "gName", "gLevel","keywords" };
    static String SHEET = "Sheet1";

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<CompetencyMap> excelToTutorials(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<CompetencyMap> compentencies = new ArrayList<CompetencyMap>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                CompetencyMap competence = new CompetencyMap();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
//                        case 0:
//                            competence.setId((long) currentCell.getNumericCellValue());
//                            break;

                        case 0:
                            competence.setcName(currentCell.getStringCellValue());
                            break;

                        case 1:
                            competence.setcLevel((int) currentCell.getNumericCellValue());
                            break;

                        case 2:
                            competence.setgName(currentCell.getStringCellValue());
                            break;
                        case 3:
                            competence.setgLevel((int) currentCell.getNumericCellValue());
                            break;
                        case 4:
                            competence.setKeywords(currentCell.getStringCellValue());
                            break;
                            
                        default:
                            break;
                    }

                    cellIdx++;
                }

                compentencies.add(competence);
            }

            workbook.close();

            return compentencies;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
