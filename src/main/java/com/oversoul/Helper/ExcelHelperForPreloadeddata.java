package com.oversoul.Helper;

import com.oversoul.entity.PreloadedData;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelperForPreloadeddata {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "Id", "cName", "cLevel", "gName", "gLevel","description", "keywords" };
    static String SHEET = "preloadeddata";

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static <Int> List<PreloadedData> excelToData(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<PreloadedData> preloadedDataList = new ArrayList<PreloadedData>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                PreloadedData preloadedInfo = new PreloadedData();
                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            preloadedInfo.setId((long) currentCell.getNumericCellValue());
                            break;

                        case 1:
                            preloadedInfo.setcName(currentCell.getStringCellValue());
                            break;

                        case 2:
                            preloadedInfo.setbAuthor(currentCell.getStringCellValue());
                            break;

                        case 3:
                            preloadedInfo.setbPublisher(currentCell.getStringCellValue());
                            break;
                        case 4:
                            preloadedInfo.setbPublishingYear((int) currentCell.getNumericCellValue());
                            break;
                        case 5:
                            preloadedInfo.setbTitle(currentCell.getStringCellValue());
                            break;
                        case 6:
                            preloadedInfo.setbUrl(currentCell.getStringCellValue());
                            break;
                        case 7:
                            preloadedInfo.setOcCourse(currentCell.getStringCellValue());
                            break;
                        case 8:
                            preloadedInfo.setOcDescription(currentCell.getStringCellValue());
                            break;
                        case 9:
                            preloadedInfo.setOcUrl(currentCell.getStringCellValue());
                            break;
                        case 10:
                            preloadedInfo.setOcPlatform(currentCell.getStringCellValue());
                            break;
                        case 11:
                            preloadedInfo.setOcImgUrl(currentCell.getStringCellValue());
                            break;
                        case 12:
                            preloadedInfo.setyName(currentCell.getStringCellValue());
                            break;
                        case 13:
                            preloadedInfo.setyDescription(currentCell.getStringCellValue());
                            break;
                        case 14:
                            preloadedInfo.setyUrl(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }

                    cellIdx++;
                }

                preloadedDataList.add(preloadedInfo);
            }

            workbook.close();

            return preloadedDataList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

}
