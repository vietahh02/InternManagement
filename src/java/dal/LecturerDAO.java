/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.Lecturer;

/**
 *
 * @author ADMIN
 */
public class LecturerDAO extends DBContext {

    public Lecturer getById(int lecturer_id) {
        String sql = "SELECT * FROM Lecturer WHERE lecturer_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, lecturer_id);
            ResultSet rs = statement.executeQuery();
            Dao d = new Dao();
            if (rs.next()) {
                Lecturer l = new Lecturer();
                l.setLecturer_id(rs.getInt("lecturer_id"));
                Account acc = d.getAccountByID(rs.getInt("account_id"));
                l.setAccountLecturer(acc);
                l.setFullName(rs.getString("fullName"));
                l.setEmployeeNumber(rs.getString("employeeNumber"));
                l.setDepartment(rs.getString("department"));
                l.setDepartment(rs.getString("specialization"));
                return l;

            }
        } catch (SQLException ex) {
            Logger.getLogger(LecturerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; // Subject not found
    }

    public boolean createLecturer(Lecturer lecturer) {
        String query = "INSERT INTO Lecturer (account_id, fullName, employeeNumber, department, specialization) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, lecturer.getAccountLecturer().getId());
            ps.setString(2, lecturer.getFullName());
            ps.setString(3, lecturer.getEmployeeNumber());
            ps.setString(4, lecturer.getDepartment());
            ps.setString(5, lecturer.getSpecialization());
            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Lecturer getByAccountId(int accountId) {
        String sql = "SELECT * FROM Lecturer WHERE account_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, accountId);
            ResultSet rs = statement.executeQuery();
            Dao d = new Dao();
            if (rs.next()) {
                Lecturer l = new Lecturer();
                l.setLecturer_id(rs.getInt("lecturer_id"));
                Account acc = d.getAccountByID(rs.getInt("account_id"));
                l.setAccountLecturer(acc);
                l.setFullName(rs.getString("fullName"));
                l.setEmployeeNumber(rs.getString("employeeNumber"));
                l.setDepartment(rs.getString("department"));
                l.setDepartment(rs.getString("specialization"));
                return l;

            }
        } catch (SQLException ex) {
            Logger.getLogger(LecturerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null; // Subject not found
    }

    public boolean updateLecturer(int sId, String fullName) {
        String query = "UPDATE Lecturer SET fullName = ?  WHERE lecturer_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, fullName);
            ps.setInt(2, sId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Lecturer> getAllLecturer() {
        List<Lecturer> lecturers = new ArrayList<>();
        String sql = "SELECT * FROM Lecturer";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            Dao dao = new Dao(); // Assuming Dao class exists and has method getAccountByID()
            while (rs.next()) {
                Lecturer lecturer = new Lecturer();
                lecturer.setLecturer_id(rs.getInt("lecturer_id"));
                Account acc = dao.getAccountByID(rs.getInt("account_id"));
                lecturer.setAccountLecturer(acc);
                lecturer.setFullName(rs.getString("fullName"));
                lecturer.setEmployeeNumber(rs.getString("employeeNumber"));
                lecturer.setDepartment(rs.getString("department"));
                lecturer.setSpecialization(rs.getString("specialization"));
                lecturers.add(lecturer);
            }
        } catch (SQLException ex) {
            Logger.getLogger(LecturerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lecturers;
    }

    public Lecturer getLecturerOfStudent(int studentId) {

        Dao dao = new Dao();
        try {
            // SQL query to find the lecturer of the student's class
            String query = """
                SELECT l.* FROM Lecturer l
                JOIN Class c ON l.lecturer_id = c.lecturer_id
                JOIN userClass uc ON c.class_id = uc.class_id
                WHERE uc.student_id = ?
                """;

            // Preparing the SQL statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, studentId);

            // Executing the query
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Lecturer lecturer = new Lecturer();
                lecturer.setLecturer_id(rs.getInt("lecturer_id"));
                Account acc = dao.getAccountByID(rs.getInt("account_id"));
                lecturer.setAccountLecturer(acc);
                lecturer.setFullName(rs.getString("fullName"));
                lecturer.setEmployeeNumber(rs.getString("employeeNumber"));
                lecturer.setDepartment(rs.getString("department"));
                lecturer.setSpecialization(rs.getString("specialization"));
                return lecturer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        LecturerDAO l = new LecturerDAO();
        System.out.println(l.getLecturerOfStudent(1));
    }
}
