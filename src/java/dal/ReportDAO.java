package dal;

import model.News;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import model.Account;
import model.Question;
import model.Report;
import model.Student;

public class ReportDAO extends DBContext {

    public List<Report> getAllReportForLecturer(int lid, String type, Integer class_id) {
        List<Report> reports = new ArrayList<>();
        List<Object> list = new ArrayList<>();
        AccountDAO accountDAO = new AccountDAO();
        try {
            StringBuilder query = new StringBuilder();
            query.append("""
                         SELECT r.*, c.class_name, s.fullName as sName FROM Report r 
                         JOIN Student s
                         ON s.account_id = r.student_report
                         JOIN userClass uc
                         ON uc.student_id = s.student_id
                         JOIN Class c 
                         ON c.class_id = uc.class_id
                         where sender  =  ? """);
            list.add(lid);
            if (type != null && !type.trim().isEmpty()) {
                query.append(" AND  type = ? ");
                list.add(type);
            }
            if (class_id != null) {
                query.append(" AND  c.class_id = ? ");
                list.add(class_id);
            }
            query.append(" order by r.created_at desc ");
            PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
            mapParams(preparedStatement, list);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Report q = new Report();
                    q.setId(rs.getInt("id"));
                    q.setType(rs.getString("type"));
                    q.setContent(rs.getString("content"));
                    Timestamp createdTimestamp = rs.getTimestamp("created_at");
                    if (createdTimestamp != null) {
                        q.setCreated_at(createdTimestamp.toLocalDateTime());
                    }
                    Timestamp updatedTimestamp = rs.getTimestamp("updated_at");
                    if (updatedTimestamp != null) {
                        q.setUpdated_at(updatedTimestamp.toLocalDateTime());
                    }
                    q.setClass_name(rs.getString("class_name"));
                    q.setKnowledge(rs.getFloat("knowledge"));
                    q.setSoft_skill(rs.getFloat("soft_skill"));
                    q.setFinal_grade(rs.getFloat("final"));
                    q.setAttitude(rs.getFloat("attitude"));
                    q.setStudent_name(rs.getString("sName"));
                    q.setSender(accountDAO.getByID(rs.getInt("sender")));
                    q.setStudent_report(accountDAO.getByID(rs.getInt("student_report")));
                    reports.add(q);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    public List<Report> getAllReportForAdmin(String type, Integer class_id) {
        List<Report> reports = new ArrayList<>();
        List<Object> list = new ArrayList<>();
        AccountDAO accountDAO = new AccountDAO();
        try {
            StringBuilder query = new StringBuilder();
            query.append("""
                         SELECT r.*, c.class_name, s.fullName as sName , l.fullName as lName from Report r 
                         JOIN Student s
                         ON s.account_id = r.student_report
                         JOIN userClass uc
                         ON uc.student_id = s.student_id
                         JOIN Class c 
                         ON c.class_id = uc.class_id
                         JOIN Lecturer l on l.account_id = r.sender
                          where 1 = 1 """);

            if (type != null && !type.trim().isEmpty()) {
                query.append(" AND  type = ? ");
                list.add(type);
            }
            if (class_id != null) {
                query.append(" AND  c.class_id = ? ");
                list.add(class_id);
            }
            query.append("  order by r.created_at desc ");
            PreparedStatement preparedStatement = connection.prepareStatement(query.toString());
            mapParams(preparedStatement, list);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Report q = new Report();
                    q.setId(rs.getInt("id"));
                    q.setType(rs.getString("type"));
                    q.setContent(rs.getString("content"));
                    Timestamp createdTimestamp = rs.getTimestamp("created_at");
                    if (createdTimestamp != null) {
                        q.setCreated_at(createdTimestamp.toLocalDateTime());
                    }
                    Timestamp updatedTimestamp = rs.getTimestamp("updated_at");
                    if (updatedTimestamp != null) {
                        q.setUpdated_at(updatedTimestamp.toLocalDateTime());
                    }
                    q.setKnowledge(rs.getFloat("knowledge"));
                    q.setSoft_skill(rs.getFloat("soft_skill"));
                    q.setAttitude(rs.getFloat("attitude"));
                    q.setFinal_grade(rs.getFloat("final"));
                    q.setClass_name(rs.getString("class_name"));
                    q.setStudent_name(rs.getString("sName"));
                    q.setLecturer_name(rs.getString("lName"));
                    q.setSender(accountDAO.getByID(rs.getInt("sender")));
                    q.setStudent_report(accountDAO.getByID(rs.getInt("student_report")));
                    reports.add(q);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    /**
     * Map các tham số vào PreparedStatement
     *
     * @param ps
     * @param args
     * @throws SQLException
     */
    public void mapParams(PreparedStatement ps, List<Object> args) throws SQLException {
        int i = 1;
        for (Object arg : args) {
            if (arg instanceof Date) {
                ps.setTimestamp(i++, new Timestamp(((Date) arg).getTime()));
            } else if (arg instanceof Integer) {
                ps.setInt(i++, (Integer) arg);
            } else if (arg instanceof Long) {
                ps.setLong(i++, (Long) arg);
            } else if (arg instanceof Double) {
                ps.setDouble(i++, (Double) arg);
            } else if (arg instanceof Float) {
                ps.setFloat(i++, (Float) arg);
            } else {
                ps.setString(i++, (String) arg);
            }
        }
    }

    public boolean reportExists(int studentId, String type) {
        boolean exists = false;
        String query = "SELECT 1 FROM Report WHERE student_report = ? AND type = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, studentId);
            preparedStatement.setString(2, type);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    exists = true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return exists;
    }

    public void addReport(Report report) {
        String query = "INSERT INTO [Report] (content, type,knowledge, soft_skill, attitude,final ,  created_at, updated_at, sender, student_report) VALUES (?,?, ?,?,?,?, ?, ?, ?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, report.getContent());
            preparedStatement.setString(2, report.getType());
            preparedStatement.setFloat(3, report.getKnowledge());
            preparedStatement.setFloat(4, report.getSoft_skill());
            preparedStatement.setFloat(5, report.getAttitude());
            preparedStatement.setFloat(6, report.getFinal_grade());
            preparedStatement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setTimestamp(8, null);
            preparedStatement.setInt(9, report.getSender().getId());
            preparedStatement.setInt(10, report.getStudent_report().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a news record
    public void updateReport(String content, float knowledge, float soft_skill, float attitude, float final_grade, int id) {
        String query = "UPDATE [Report] SET content = ?, knowledge = ?, soft_skill = ? , attitude = ?, final = ? , updated_at = ?  WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, content);
            preparedStatement.setFloat(2, knowledge);
            preparedStatement.setFloat(3, soft_skill);
            preparedStatement.setFloat(4, attitude);
            preparedStatement.setFloat(5, final_grade);
            preparedStatement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setInt(7, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getTeacherIdOfStudent(int id) {
        int userId = 0;
        String query = """
                       select l.lecturer_id from userClass uc JOIN Class c
                       ON uc.class_id = c.class_id
                       JOIN Lecturer l ON l.lecturer_id = c.lecturer_id
                       JOIN Student s ON s.student_id = uc.student_id
                       where s.account_id = ?""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("lecturer_id");
                    return userId;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public Report getByID(int id) {

        String query = """
                       Select * from report where id  = ?""";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            AccountDAO o = new AccountDAO();
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    Report q = new Report();
                    q.setId(rs.getInt("id"));
                    q.setType(rs.getString("type"));
                    q.setContent(rs.getString("content"));
                    Timestamp createdTimestamp = rs.getTimestamp("created_at");
                    if (createdTimestamp != null) {
                        q.setCreated_at(createdTimestamp.toLocalDateTime());
                    }
                    Timestamp updatedTimestamp = rs.getTimestamp("updated_at");
                    if (updatedTimestamp != null) {
                        q.setUpdated_at(updatedTimestamp.toLocalDateTime());
                    }
                    Account s= o.getByID(rs.getInt("student_report"));
                    q.setStudent_report(s);
                    q.setKnowledge(rs.getFloat("knowledge"));
                    q.setSoft_skill(rs.getFloat("soft_skill"));
                    q.setAttitude(rs.getFloat("attitude"));
                    q.setFinal_grade(rs.getFloat("final"));
                    return q;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        ReportDAO dAO = new ReportDAO();
        System.out.println(dAO.getByID(2));
    }
}
