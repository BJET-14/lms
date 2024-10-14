package com.bjet.aki.lms.util;

import com.bjet.aki.lms.exception.CommonException;
import com.bjet.aki.lms.jpa.ExamEntity;
import com.bjet.aki.lms.jpa.ExamResultEntity;
import com.bjet.aki.lms.model.Course;
import com.bjet.aki.lms.model.Student;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvUtils {

    public static ByteArrayInputStream examResultTemplateToCsv(Course course, ExamEntity exam, List<Student> students) {
        final CSVFormat format = CSVFormat.DEFAULT.withHeader("Student Id", "Student Name", "Score", "Comment");

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
            for (Student student : students) {
                String name = student.getFirstName() + " " + student.getLastName();
                List<String> data = Arrays.asList(
                        String.valueOf(student.getId()),
                        name,
                        "",
                        ""
                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new CommonException("16", "fail to import data to CSV file: " + e.getMessage());
        }
    }

    public static List<ExamResultEntity> csvToExamResult(InputStream inputStream, Long examId) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<ExamResultEntity> results = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                ExamResultEntity result = new ExamResultEntity()
                        .setStudentId(Long.parseLong(csvRecord.get(0)))
                        .setExamId(examId)
                        .setMark(csvRecord.get(2) != null ? Double.parseDouble(csvRecord.get(2)) : 0)
                        .setComment(csvRecord.get(3));

                results.add(result);
            }

            return results;
        } catch (IOException e) {
            throw new CommonException("18", "fail to parse CSV file: " + e.getMessage());
        }
    }
}

class CsvConstants {
    public static String TYPE = "text/csv";
    public static String[] HEADERS = {"Student Id", "Student Name", "Score", "Comment"};
}
