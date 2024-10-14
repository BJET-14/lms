package com.bjet.aki.lms.util;

import com.bjet.aki.lms.jpa.ExamEntity;
import com.bjet.aki.lms.jpa.ExamResultEntity;
import com.bjet.aki.lms.model.Course;
import com.bjet.aki.lms.model.Student;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.bjet.aki.lms.util.ExcelConstants.SHEET;

@Service
@AllArgsConstructor
@Slf4j
public class ExcelUtils {

    public static ByteArrayInputStream examResultTemplateToExcel(Course course, ExamEntity exam, List<Student> students) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Title and subtitle
            Row courseTitleRow = sheet.createRow(1);
            Cell courseTitleRowCell = courseTitleRow.createCell(0);
            courseTitleRowCell.setCellValue(course.getTitle());
            // Apply style to Main Title
            CellStyle mainTitleStyle = workbook.createCellStyle();
            Font mainTitleFont = workbook.createFont();
            mainTitleFont.setBold(true);
            mainTitleFont.setFontHeightInPoints((short) 12);  // Set font size
            mainTitleFont.setColor(IndexedColors.DARK_BLUE.getIndex()); // Set font color
            mainTitleStyle.setFont(mainTitleFont);
            mainTitleStyle.setLocked(true);
            courseTitleRowCell.setCellStyle(mainTitleStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));

            Row examTitleRow = sheet.createRow(2);
            Cell examTitleRowCell = examTitleRow.createCell(0);
            examTitleRowCell.setCellValue(exam.getName());
            // Apply style to Subtitle
            CellStyle subtitleStyle = workbook.createCellStyle();
            courseTitleRowCell.setCellValue(course.getTitle());
            Font subtitleFont = workbook.createFont();
            subtitleFont.setBold(true);
            subtitleFont.setFontHeightInPoints((short) 8);  // Set font size
            subtitleFont.setColor(IndexedColors.GREY_80_PERCENT.getIndex()); // Set font color
            subtitleStyle.setFont(subtitleFont);
            subtitleStyle.setLocked(true);
            examTitleRowCell.setCellStyle(mainTitleStyle);
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 4));

            // Header
            Row headerRow = sheet.createRow(4);

            for (int col = 0; col < ExcelConstants.HEADERS.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(ExcelConstants.HEADERS[col]);
            }

            int rowIdx = 5;
            for (Student student : students) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(student.getId());
                row.createCell(1).setCellValue(student.getFirstName() + " " + student.getLastName());
                Cell scoreCell = row.createCell(2);
                CellStyle scoreStyle = workbook.createCellStyle();
                scoreStyle.setFillBackgroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
                scoreCell.setCellStyle(scoreStyle);
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static boolean hasExcelFormat(MultipartFile file) {
        return ExcelConstants.TYPE.equals(file.getContentType());
    }

    public static boolean hasCsvFormat(MultipartFile file) {
        return CsvConstants.TYPE.equals(file.getContentType());
    }

    public static List<ExamResultEntity> excelToExamResult(InputStream inputStream, Long examId) {
        try {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<ExamResultEntity> results = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0 || rowNumber == 1 || rowNumber == 2) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                ExamResultEntity result = new ExamResultEntity();
                result.setExamId(examId);

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIdx) {
                        case 0:
                            result.setStudentId((long) currentCell.getNumericCellValue());
                            break;
                        case 2:
                            result.setMark(currentCell.getNumericCellValue());
                            break;
                        case 3:
                            result.setComment(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }
                    cellIdx++;
                }
                results.add(result);
            }

            workbook.close();
            return results;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}

class ExcelConstants {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static String[] HEADERS = {"Student Id", "Student Name", "Score", "Comment"};
    public static String SHEET = "Result";
}
