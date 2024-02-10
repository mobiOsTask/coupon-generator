package org.example.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class ExcelWrite {

    private List<List<Object>> allData;
    private List<String> header;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public ExcelWrite(List<List<Object>> allData,List<String> header) {
        this.allData = allData;
        this.header = header;
        workbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Sheet 0");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
//        font.setBold(true);
        font.setFontHeight(13);
        style.setFont(font);

        for (int i = 0; i < header.size(); i++) {
            String s = header.get(i);
            createCell(row, i, s, style);
        }

    }

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else if (valueOfCell instanceof BigInteger) {
            BigInteger valueOfCell1 = (BigInteger) valueOfCell;
            cell.setCellValue(String.valueOf(valueOfCell1));
        } else {
            cell.setCellValue(String.valueOf(valueOfCell));
        }
        cell.setCellStyle(style);
    }

    private void write() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (List<Object> allDatum : allData) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for (Object o : allDatum) {
                createCell(row, columnCount++, o, style);
            }
        }
//        for (UpcomingCase record : studentList) {
//            Row row = sheet.createRow(rowCount++);
//            int columnCount = 0;
//            createCell(row, columnCount++, record.getId(), style);
//            createCell(row, columnCount++, record.getStudentName(), style);
//            createCell(row, columnCount++, record.getEmail(), style);
//            createCell(row, columnCount++, record.getMobileNo(), style);
//        }
    }

//    public void generateExcelFile(HttpServletResponse response) throws IOException {
//        writeHeader();
//        write();
//        ServletOutputStream outputStream = response.getOutputStream();
//        workbook.write(outputStream);
//        workbook.close();
//        outputStream.close();
//    }
}
