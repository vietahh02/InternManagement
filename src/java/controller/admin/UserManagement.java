/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import dal.AccountDAO;
import dal.ClasssDAO;
import dal.LecturerDAO;
import dal.RoleDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import model.Account;
import model.Lecturer;
import model.Role;
import send_mail.SendMail;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "UserManagement", urlPatterns = {"/admin/user-managament"})
public class UserManagement extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        if (a != null && a.getRoleAccount().getRole_id() == 1) {
            AccountDAO accountDAO = new AccountDAO();
            List<Account> listA = accountDAO.getAllUser();

            request.setAttribute("account", listA);
            request.setAttribute("accounts", a);
            request.getRequestDispatcher("user-list.jsp").forward(request, response);
        } else {
            response.sendRedirect("../login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        AccountDAO accountDAO = new AccountDAO();
        if (a != null && a.getRoleAccount().getRole_id() == 1) {
            String action = request.getParameter("action");
            if (action.equals("change-status")) {
                int id = Integer.parseInt(request.getParameter("id"));
                String status = request.getParameter("status");
                if (status.equalsIgnoreCase("active")) {
                    accountDAO.changeStatus(id, "inactive");
                    session.setAttribute("notification", "Block user successfully! ");
                } else {
                    session.setAttribute("notification", "un-block user successfully! ");
                    accountDAO.changeStatus(id, "active");
                }
                response.sendRedirect("user-managament");
            }
            if (action.equals("add")) {
                String fullName = request.getParameter("name");
                String employeeNumber = request.getParameter("employeeNumber");
                String email = request.getParameter("email");
                String phone = request.getParameter("phone");
                String department = request.getParameter("department");
                String specialization = request.getParameter("specialization");

                // Extract username from email
                String username = email.split("@")[0];

                // Generate random password
                String password = generateRandomPassword(8);

                // Create new Account object
                Account newAccount = new Account();
                newAccount.setUsername(username);
                newAccount.setPassword(password);
                newAccount.setEmail(email);
                newAccount.setPhone(phone);
                newAccount.setStatus("active");
                RoleDAO rdao = new RoleDAO();
                Role role = rdao.getById(3);
                newAccount.setRoleAccount(role);

                // Insert new account into the database
                
                if (accountDAO.getByEmail(email) != null) {
                    session.setAttribute("notificationErr", "Email already exist.");
                    response.sendRedirect("user-managament");
                    return;
                }
                boolean accountCreated = accountDAO.createLecturer(newAccount);

                if (accountCreated) {
                    // Get the newly created account ID
                    Account createdAccount = accountDAO.getLatestAccount();

                    // Create new Lecturer object
                    Lecturer newLecturer = new Lecturer();
                    newLecturer.setFullName(fullName);
                    newLecturer.setEmployeeNumber(employeeNumber);
                    newLecturer.setSpecialization(specialization);
                    newLecturer.setDepartment(department);
                    newLecturer.setAccountLecturer(createdAccount);

                    // Insert new lecturer into the database
                    LecturerDAO lecturerDAO = new LecturerDAO();
                    boolean lecturerCreated = lecturerDAO.createLecturer(newLecturer);

                    if (lecturerCreated) {
                        // Send email to the user
                        SendMail sendMail = new SendMail();
                        String subject = "Successfully Registered";
                        String content = "You have been invited to join our system role lecturer.\nUsername: " + username + "\nPassword: " + password;
                        boolean emailSent = sendMail.sendMail(email, subject, content);

                        if (emailSent) {
                            session.setAttribute("notification", "Lecturer add uccessfully.");
                            response.sendRedirect("user-managament");
                        } else {
                            // Handle the error case where the email was not sent
                            session.setAttribute("notificationErr", "Failed to send email.");
                        }
                    } else {
                        // Handle the error case where the lecturer was not created
                        session.setAttribute("notificationErr", "Failed to create lecturer.");

                    }
                } else {
                    // Handle the error case where the account was not created
                    session.setAttribute("notificationErr", "Failed to create account.");

                }
            } else {
                // Handle other actions or default action
                response.sendRedirect("user-managament");
            }
        } else {
            response.sendRedirect("../login");
        }
    }

    private String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return Base64.getEncoder().withoutPadding().encodeToString(bytes).substring(0, length);
    }

}
