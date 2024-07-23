package controller.admin;

import dal.StudentReportDAO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import model.Account;
import model.StudentReport;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@WebServlet(name = "StudentReportManagement", urlPatterns = {"/admin/student-report"})
public class StudentReportManagement extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        int cid = Integer.parseInt(request.getParameter("class"));
        String type = request.getParameter("type");
        String reportType = request.getParameter("reportType");

        if (a != null && a.getRoleAccount().getRole_id() == 1) {
            if ("passFail".equals(reportType)) {
                generatePassFailReport(response, cid);
            } else {
                generateExistingReport(response, cid, type);
            }
        }
    }

    private void generatePassFailReport(HttpServletResponse response, int cid) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pass_Fail_Report");

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);

            Row headerRow = sheet.createRow(0);
            String[] headers = {"Student Name", "Student Roll Number", "Grade (Final)", "Status"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(cellStyle);
            }

            StudentReportDAO srdao = new StudentReportDAO();
            List<StudentReport> list = srdao.getAllStudentsByClassId(cid, "Final");

            int startRow = 1;
            for (StudentReport report : list) {
                Row row = sheet.createRow(startRow++);
                Cell nameCell = row.createCell(0);
                nameCell.setCellValue(report.getStudent().getFullName());
                nameCell.setCellStyle(cellStyle);

                Cell rollNumberCell = row.createCell(1);
                rollNumberCell.setCellValue(report.getStudent().getRollNumber());
                rollNumberCell.setCellStyle(cellStyle);

                Cell gradeCell = row.createCell(2);
                gradeCell.setCellValue(report.getReport().getFinal_grade());
                gradeCell.setCellStyle(cellStyle);

                Cell statusCell = row.createCell(3);
                String status = "Not Graded";
                if (report.getReport().getFinal_grade() >= 4) {
                    status = "Passed";
                } else if (report.getReport().getFinal_grade() < 4) {
                    status = "Failed";
                }
                statusCell.setCellValue(status);
                statusCell.setCellStyle(cellStyle);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Save to a new file
            String newFilePath = getServletContext().getRealPath("/") + "static/pass-fail-report.xlsx";
            try (FileOutputStream fos = new FileOutputStream(new File(newFilePath))) {
                workbook.write(fos);
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=pass-fail-report.xlsx");
            try (FileInputStream downloadFis = new FileInputStream(new File(newFilePath)); OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = downloadFis.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateExistingReport(HttpServletResponse response, int cid, String type) throws IOException {
        String filePath = getServletContext().getRealPath("/") + "static/evaluation.xlsx";
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            // Define the cell style for centering text and adding borders
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            StudentReportDAO srdao = new StudentReportDAO();
            List<StudentReport> list = srdao.getAllStudentsByClassId(cid, type);
            list.forEach(System.out::println);
            // Write data to the Excel file
            int startRow = 13; // Row index starts at 0, so 13 means row 14
            for (StudentReport report : list) {
                Row row = sheet.getRow(startRow++);
                row.getCell(1).setCellValue(report.getStudent().getRollNumber()); // Student ID
                row.getCell(2).setCellValue(""); // Mã nhân viên
                row.getCell(3).setCellValue(report.getStudent().getFullName()); // Fullname
                row.getCell(4).setCellValue(report.getStudent().getJobTitle()); // Bộ phận
                row.getCell(5).setCellValue(report.getLecturer().getFullName()); // cán bộ quản lý
                row.getCell(6).setCellValue(""); // Trợ cấp
                row.getCell(7).setCellValue(report.getReport().getContent()); // Nhận xét
                row.getCell(8).setCellValue(getTwoDecimal(report.getReport().getKnowledge())); // kn
                row.getCell(9).setCellValue(getTwoDecimal(report.getReport().getSoft_skill())); // s
                row.getCell(10).setCellValue(getTwoDecimal(report.getReport().getAttitude())); // a
                row.getCell(11).setCellValue(getTwoDecimal(report.getReport().getFinal_grade())); // f
            }
            Row dateRow = sheet.getRow(35);
            Cell dateCell = dateRow.getCell(10);
            dateCell.setCellValue("Ha Noi, " + getCurrentTimeStamp());
            String newFilePath = getServletContext().getRealPath("/") + "static/student-report.xlsx";
            try (FileOutputStream fos = new FileOutputStream(new File(newFilePath))) {
                workbook.write(fos);
            }
            workbook.close();

            // Prepare for download
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=student-report.xlsx");
            try (FileInputStream downloadFis = new FileInputStream(new File(newFilePath)); OutputStream out = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = downloadFis.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    public final String getCurrentTimeStamp() {
        SimpleDateFormat formDate = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = formDate.format(new Date());
        return strDate;
    }

    private String getTwoDecimal(float value) {
        return String.format("%.2f", value);
    }
}
