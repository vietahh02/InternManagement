/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.beans.Statement;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import model.Account;
import model.Lecturer;
import model.Role;

/**
 *
 * @author ADMIN
 */
public class AccountDAO extends DBContext {

    public List<Account> getAllUser() {
        List<Account> accounts = new ArrayList<>();

        RoleDAO roleDAO = new RoleDAO();
        try {
            String query = """
                         Select * from Account where id != 1 ORDER BY id DESC
                         """;

            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(query);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Account a = new Account();
                    a.setId(rs.getInt("id"));
                    a.setUsername(rs.getString("username"));
                    a.setStatus(rs.getString("status"));
                    a.setAddress(rs.getString("address"));
                    a.setPhone(rs.getString("phone"));
                    a.setEmail(rs.getString("email"));
                    a.setRoleAccount(roleDAO.getById(rs.getInt("roleId")));

                    accounts.add(a);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }   

    public Account getByEmail(String email) {
        Account account = null;
        RoleDAO roleDAO = new RoleDAO();
        try {
            String query = """
                           SELECT * FROM Account WHERE email = ?
                           """;

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    account = new Account();
                    account.setId(rs.getInt("id"));
                    account.setUsername(rs.getString("username"));
                    account.setStatus(rs.getString("status"));
                    account.setAddress(rs.getString("address"));
                    account.setPhone(rs.getString("phone"));
                    account.setEmail(rs.getString("email"));
                    account.setRoleAccount(roleDAO.getById(rs.getInt("roleId")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public Account getByID(int id) {
        Account account = null;
        RoleDAO roleDAO = new RoleDAO();
        StudentDAO studentDAO = new StudentDAO();
        LecturerDAO lecturerDAO = new LecturerDAO();
        try {
            String query = """
                             select a.*, l.lecturer_id as lId, s.student_id as sId from Account a
                             LEFT JOIN Student s on s.account_id = a.id
                             LEFT JOIN Lecturer l on l.account_id = a.id where a.id = ?
                           """;

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    account = new Account();
                    account.setId(rs.getInt("id"));
                    account.setUsername(rs.getString("username"));
                    account.setStatus(rs.getString("status"));
                    account.setAddress(rs.getString("address"));
                    account.setPhone(rs.getString("phone"));
                    account.setEmail(rs.getString("email"));
                    account.setRoleAccount(roleDAO.getById(rs.getInt("roleId")));
                    account.setStudent(studentDAO.getById(rs.getInt("sId")));
                    account.setLecturer(lecturerDAO.getById(rs.getInt("lId")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public boolean create(Account account) {
        String query = "INSERT INTO Account (username, password, email, roleId, status, phone) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.setString(3, account.getEmail());
            ps.setInt(4, 2); // student role
            ps.setString(5, "active");
            ps.setString(6, account.getPhone());
            int row = ps.executeUpdate();
            return row > 0;
        } catch (SQLException e) {
            return false;
        }
    }
    public boolean createLecturer(Account account) {
        String query = "INSERT INTO Account (username, password, email, roleId, status, phone) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.setString(3, account.getEmail());
            ps.setInt(4, 3); 
            ps.setString(5, "active");
            ps.setString(6, account.getPhone());
            int row = ps.executeUpdate();
            return row > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public Account getLatestAccount() {
        Account account = null;
        RoleDAO roleDAO = new RoleDAO();
        String query = "SELECT top 1 * FROM Account ORDER BY id DESC ";
        try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                account = new Account();
                account.setId(rs.getInt("id"));
                account.setUsername(rs.getString("username"));
                account.setStatus(rs.getString("status"));
                account.setAddress(rs.getString("address"));
                account.setPhone(rs.getString("phone"));
                account.setEmail(rs.getString("email"));
                account.setRoleAccount(roleDAO.getById(rs.getInt("roleId")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }

    public boolean changeStatus(int accountId, String newStatus) {
        String query = "UPDATE Account SET status = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, newStatus);
            ps.setInt(2, accountId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean updateAccount(int accountId, String address, String phone) {
        String query = "UPDATE Account SET address = ? , phone = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, address);
            ps.setString(2, phone);
            ps.setInt(3, accountId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

   
}
