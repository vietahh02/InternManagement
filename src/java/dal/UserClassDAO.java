/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Account;
import model.ClassStudentDTO;
import model.Student;
import model.UserClass;

/**
 *
 * @author ADMIN
 */
public class UserClassDAO extends DBContext {

    public void addStudentToClass(int studentId, int classId) {
        String query = "INSERT INTO userClass (student_id, class_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, studentId);
            ps.setInt(2, classId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isStudentInClass(int studentId) {
        String query = "SELECT COUNT(*) FROM UserClass WHERE student_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Student> getStudentsNotInClass(int classId) {
        List<Student> students = new ArrayList<>();

        String sql = " SELECT * FROM Student WHERE student_id NOT IN (SELECT student_id FROM UserClass WHERE class_id = ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Student s = new Student();
                s.setStudent_id(rs.getInt("student_id"));
                s.setFullName(rs.getString("fullName"));
                s.setRollNumber(rs.getString("rollNumber"));
                s.setBirthDate(rs.getDate("birthDate"));
                s.setSchoolYear(rs.getInt("schoolYear"));
                s.setCompany(rs.getString("Company"));
                s.setMajor(rs.getString("Major"));
                s.setJobTitle(rs.getString("JobTitle"));
                s.setLinkCv(rs.getString("LinkCV"));
                students.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public boolean canDeleteUserClass(int userClassId) {
        String sql = "SELECT COUNT(*) FROM Attendance WHERE userClass_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userClassId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteUserClass(int userClassId) {
        String sql = "DELETE FROM UserClass WHERE userClass_id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, userClassId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<UserClass> getAllUserClasses(int classId) {
        List<UserClass> userClasses = new ArrayList<>();
        StudentDAO sdao = new StudentDAO();
        ClasssDAO cdao = new ClasssDAO();
        String query = "SELECT * FROM UserClass WHERE class_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    UserClass userClass = new UserClass();
                    userClass.setId(rs.getInt("userClass_id"));
                    Student s = sdao.getById(rs.getInt("student_id"));
                    model.Class c = cdao.getById(rs.getInt("class_id"));
                    userClass.setClasses(c);
                    userClass.setStudent(s);
                    userClasses.add(userClass);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userClasses;
    }

    public List<ClassStudentDTO> getAllUserClasse(int classId) {
        List<ClassStudentDTO> userClasses = new ArrayList<>();
        String query = """
                       Select s.student_id , s.rollNumber, a.email, s.fullName, a.phone, s.Major, s.Company, s.JobTitle, s.LinkCV from Account a
                       JOIN Student s  ON a.id = s.account_id
                       JOIN userClass uc ON uc.student_id = s.student_id
                       JOIN Class c on uc.class_id = c.class_id
                       where c.class_id= ?""";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ClassStudentDTO userClass = new ClassStudentDTO();
                    userClass.setStudent_id(rs.getInt("student_id"));
                    userClass.setRollNumber(rs.getString("rollNumber"));
                    userClass.setEmail(rs.getString("email"));
                    userClass.setFullName(rs.getString("fullName"));
                    userClass.setPhone(rs.getString("phone"));
                    userClass.setMajor(rs.getString("Major"));
                    userClass.setCompany(rs.getString("Company"));
                    userClass.setJobTitle(rs.getString("JobTitle"));
                    userClass.setLinkCV(rs.getString("LinkCV"));
                    userClasses.add(userClass);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userClasses;
    }

    public List<String> getEmailsByClassId(int classId) {
        List<String> emails = new ArrayList<>();
        String query = """
                       SELECT a.email FROM Student s JOIN UserClass uc ON s.student_id = uc.student_id 
                       JOIN Account a on a.id = s.account_id
                       WHERE uc.class_id  = ? """;
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    emails.add(rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emails;
    }

    public static void main(String[] args) {
        UserClassDAO s = new UserClassDAO();
        List<String> l = s.getEmailsByClassId(2);
        System.out.println(l.size());
    }
}
