package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DashboardDAO extends DBContext {

    public Map<String, Integer> getTaskCounts() {
        Map<String, Integer> taskCounts = new HashMap<>();
        String sql = "SELECT status, COUNT(*) as count FROM Task GROUP BY status";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                taskCounts.put(rs.getString("status"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskCounts;
    }

    public Map<String, Integer> getRoleCounts() {
        Map<String, Integer> roleCounts = new HashMap<>();
        String sql = "SELECT Role.role_name, COUNT(Account.id) as count " +
                     "FROM Account JOIN Role ON Account.roleid = Role.role_id " +
                     "GROUP BY Role.role_name";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                roleCounts.put(rs.getString("role_name"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roleCounts;
    }

    public int getTotalPeople() {
        int totalPeople = 0;
        String sql = "SELECT COUNT(*) as total_people FROM Account";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalPeople = rs.getInt("total_people");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalPeople;
    }
    
   

   public Map<String, Integer> getClassesForLecturers() {
    Map<String, Integer> classesForLecturers = new HashMap<>();
    String sql = "SELECT Lecturer.fullName, COUNT(Class.class_id) as class_count " +
                 "FROM Lecturer LEFT JOIN Class ON Lecturer.lecturer_id = Class.lecturer_id " +
                 "GROUP BY Lecturer.fullName";
    try {
        PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            classesForLecturers.put(rs.getString("fullName"), rs.getInt("class_count"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return classesForLecturers;
}


    public Map<String, Integer> getPassFailCounts() {
        Map<String, Integer> passFailCounts = new HashMap<>();
        String sql = "SELECT SUM(CASE WHEN final >= 4 THEN 1 ELSE 0 END) as pass_count, " +
                     "SUM(CASE WHEN final < 4 THEN 1 ELSE 0 END) as fail_count " +
                     "FROM Report";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                passFailCounts.put("pass", rs.getInt("pass_count"));
                passFailCounts.put("fail", rs.getInt("fail_count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passFailCounts;
    }

    public int getNewsCount() {
        int newsCount = 0;
        String sql = "SELECT COUNT(*) as news_count FROM new";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                newsCount = rs.getInt("news_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newsCount;
    }
    public static void main(String[] args) {
        DashboardDAO dAO = new DashboardDAO();
           Map<String, Integer> classesForLecturers = dAO.getClassesForLecturers();
           System.out.println(classesForLecturers);
    }
 
        
}
