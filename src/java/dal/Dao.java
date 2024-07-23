/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Account;
import model.Attendance;
import model.Lecturer;
import model.Role;
import model.Student;
import model.Support;
import model.TypeSupport;
import model.UserClass;

/**
 *
 * @author ADMIN
 */
public class Dao extends DBContext {

    public Account getAccountByEmail(String email) {
        String sql = """
                     select id,username,password,roleId from Account where email = ?
                     """;
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            RoleDAO r = new RoleDAO();
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account a = new Account();
                a.setId(rs.getInt("id"));
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                Role role = r.getById(rs.getInt("roleId"));
                a.setRoleAccount(role);
                return a;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public Account getAccountByID(int id) {
        String sql = """
                     select * from Account where id = ?""";
        try {
            RoleDAO r = new RoleDAO();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account a = new Account();
                a.setId(rs.getInt("id"));
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                a.setEmail(rs.getString("email"));
                a.setAddress(rs.getString("address"));
                a.setPhone(rs.getString("phone"));
                a.setStatus(rs.getString("status"));
                Role role = r.getById(rs.getInt("roleId"));
                a.setRoleAccount(role);
                return a;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public Account getByUsernameAndPass(String username, String password) {
        String sql = """
                     select * 
                     from Account a, Student s 
                     where [username] = ? and [password] = ?""";
        RoleDAO r = new RoleDAO();
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, username);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Account a = new Account();
                a.setId(rs.getInt("id"));
                a.setUsername(rs.getString("username"));
                a.setPassword(rs.getString("password"));
                a.setEmail(rs.getString("email"));
                a.setAddress(rs.getString("address"));
                a.setPhone(rs.getString("phone"));
                a.setStatus(rs.getString("status"));
                Role role = r.getById(rs.getInt("roleId"));
                a.setRoleAccount(role);
                return a;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public boolean checkAccount(String userName, String password) {
        String sql = "select * from Account where [username] = ? and [password] = ?";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, userName);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public void setNewPassword(String userName, String password) {
        String sql = """
                     UPDATE [dbo].[Account]
                        SET [password] = ?
                      WHERE [username] = ?""";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, password);
            st.setString(2, userName);
            st.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * ****************************************************************
     * @param password
     * @return 
     */
    
    public ArrayList<model.Class> getAllClassesByLecturer(int lecturerID) {
        ArrayList<model.Class> list = new ArrayList<>();
        String sql = "select * from Class where lecturer_id = ? and status like 'ACTIVE'";
        
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, lecturerID);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                model.Class c = new model.Class();
                int classId = rs.getInt("class_id");
                c.setClass_id(classId);
                c.setClass_name(rs.getString("class_name"));
                c.setStatus(rs.getString("status"));
                c.setCheckDate(checkAttendanceDate(classId));
                list.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }
    
    public String checkAttendanceDate(int idClass) {
        String sql = """
                     select *
                     from userClass uc, Attendance a
                     where  uc.userClass_id = a.userClass_id and uc.class_id = ? and date = ? """;
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, idClass);
            st.setDate(2, Date.valueOf(getDateNow()));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                return "1";
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return "0";
    }  
    
    public boolean addAttendance(String[] array, String classId) {
        if ("1".equals(checkAttendanceDate(Integer.parseInt(classId)))) {
            return false;
        }
        List<String> list = Arrays.asList(array);
        ArrayList<UserClass> students = new ArrayList<>();
        String sql = """
                     select * from Student s, userClass us
                     where s.student_id = us.student_id and us.class_id = ? """;
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, Integer.parseInt(classId));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                UserClass uc = new UserClass();
                uc.setId(rs.getInt("userClass_id"));
                Student s = new Student();
                s.setRollNumber(rs.getString("rollNumber"));
                uc.setStudent(s);
                students.add(uc);
            }
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        String sql1 = "";
        String date = getDateNow();
        for (int i = 0; i < students.size(); i++) {
            if (list.contains(students.get(i).getStudent().getRollNumber())) {
                sql1 = "INSERT INTO [dbo].[Attendance] ([userClass_id] ,[date], [status]) VALUES (?,?,'present')";
            } else {
                sql1 = "INSERT INTO [dbo].[Attendance] ([userClass_id] ,[date], [status]) VALUES (?,?,'absent')";
            }
            try {
                PreparedStatement st1 = connection.prepareStatement(sql1);
                st1.setInt(1, students.get(i).getId());
                st1.setDate(2, Date.valueOf(date));
                st1.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e);
                return false;
            }
        }
        return true;
    }
    
    public boolean updateAttendance(String[] array, String classId) {
        List<String> list = Arrays.asList(array);
        ArrayList<Student> students = new ArrayList<>();
        String sql = """
                     select * from Student s, userClass us, Attendance a
                     where s.student_id = us.student_id and us.class_id = ? and us.userClass_id = a.userClass_id and date = ? """;
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, Integer.parseInt(classId));
            st.setDate(2, Date.valueOf(getDateNow()));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Student s = new Student();
                s.setRollNumber(rs.getString("rollNumber"));
                Attendance a = new Attendance();
                a.setAttendanceId(rs.getString("attendance_id"));
                s.setAttendance(a);
                students.add(s);
            }
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        String sql1 = "";
        for (int i = 0; i < students.size(); i++) {
            if (list.contains(students.get(i).getRollNumber())) {
                sql1 = "UPDATE [dbo].[Attendance] SET [status] = 'present' WHERE attendance_id = ?";
            } else {
                sql1 = "UPDATE [dbo].[Attendance] SET [status] = 'absent' WHERE attendance_id = ?";
            }
            try {
                PreparedStatement st1 = connection.prepareStatement(sql1);
                st1.setInt(1, Integer.parseInt(students.get(i).getAttendance().getAttendanceId()));
                st1.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e);
                return false;
            }
        }
        return true;
    }
    
    public ArrayList<Student> getStudentInClass(int lectureId, String classId, String today) {
        ArrayList<Student> list = new ArrayList<>();
        String sql;
        if ("0".equals(today)) {
            sql = """
                  select *
                  from Class c, userClass uc, Student s
                  where c.lecturer_id = ? and c.class_id = ? and c.class_id = uc.class_id and uc.student_id = s.student_id""";
            try {
                PreparedStatement st = connection.prepareStatement(sql);
                st.setInt(1, lectureId);
                st.setString(2, classId);
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    Student s = new Student();
                    s.setRollNumber(rs.getString("rollNumber"));
                    s.setFullName(rs.getString("fullName"));
                    list.add(s);
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        } else {
            sql = """
                  select *, a.status as stu
                  from Class c, userClass uc, Student s, Attendance a
                  where c.lecturer_id = ? and c.class_id = ? and c.class_id = uc.class_id and uc.student_id = s.student_id and uc.userClass_id = a.userClass_id and date like ? """;
            try {
                PreparedStatement st = connection.prepareStatement(sql);
                st.setInt(1, lectureId);
                st.setString(2, classId);
                st.setDate(3, Date.valueOf(getDateNow()));
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    Student s = new Student();
                    s.setRollNumber(rs.getString("rollNumber"));
                    s.setFullName(rs.getString("fullName"));
                    s.setStatus(rs.getString("stu"));
                    list.add(s);
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        return list;
    }
    
    public ArrayList<Student> getAttendentForLecturer(String date, String classId) {
        ArrayList<Student> list = new ArrayList<>();
        String sql = "select *\n"
                + "from Attendance a, userClass uc, Student s\n"
                + "where uc.class_id = ? and uc.userClass_id = a.userClass_id and a.date = ? and uc.student_id = s.student_id\n"
                + "order by date asc";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, Integer.parseInt(classId));
            st.setDate(2, Date.valueOf(date));
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Student s = new Student();
                s.setRollNumber(rs.getString("rollNumber"));
                s.setFullName(rs.getString("fullName"));
                s.setStatus(rs.getString("status"));
                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }
    
        private String getDateNow() {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd");
            LocalDate localDate = LocalDate.now();
            return dtf.format(localDate);
        }
    
    public boolean checkPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^*&+=-_])(?=\\S+$).{8,20}$";
        return password.matches(regex);
    }

    public ArrayList<String> getMonthAndYear(int internId) {
        ArrayList<String> list = new ArrayList<>();
        String sql = """
                     SELECT FORMAT(a.date, 'MM') AS month, YEAR(a.date) as year
                     FROM Attendance a
                     INNER JOIN userClass uc ON uc.userClass_id = a.userClass_id
                     INNER JOIN Student s ON uc.student_id = s.student_id
                     WHERE s.student_id = ?
                     GROUP BY FORMAT(a.date, 'MM'), YEAR(a.date), FORMAT(a.date, 'MM') 
                     ORDER BY YEAR(a.date), FORMAT(a.date, 'MM') DESC;""";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, internId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String s = rs.getString("year") + "-" + rs.getString("month");
                list.add(s);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public ArrayList<Attendance> getAttendentForIntern(int InternId, String mAy) {
        ArrayList<Attendance> list = new ArrayList<>();
        String sql = """
                     select *
                     from Attendance a, userClass uc, Lecturer l, Class c
                     where uc.userClass_id = a.userClass_id and uc.student_id = ? and date like ? and uc.class_id = c.class_id and c.lecturer_id = l.lecturer_id
                     order by date asc""";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, InternId);
            st.setString(2, mAy + "%");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Attendance a = new Attendance();
                a.setAttendanceId(rs.getString("attendance_id"));
                a.setDate(rs.getString("date"));
                a.setStatus(rs.getString("status"));
                Lecturer lec = new Lecturer();
                lec.setFullName(rs.getString("fullname"));
                a.setLec(lec);
                list.add(a);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public class Dt {

        private String date;
        private String amount;
    }

    public List<Dt> getAmountClassAtt(int id, String date) {
        List<Dt> list = new ArrayList<>();
        String sql = """
                     select a.date, count(a.date) as cls 
                     from (select date, class_id
                     from Attendance a, userClass uc
                     where a.userClass_id = uc.userClass_id 
                     group by date, class_id) a, (select date from Attendance a, userClass uc, Class c, Lecturer l
                     where a.userClass_id = uc.userClass_id and uc.class_id = c.class_id and l.lecturer_id = c.lecturer_id and l.lecturer_id = ? and date like ?
                     group by date) b
                     where a.date = b.date
                     group by a.date""";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            st.setString(2, date + "%");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Dt d = new Dt();
                d.setDate(rs.getString("date"));
                d.setAmount(rs.getString("cls"));
                list.add(d);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<String> getClassOn(int id, String date) {
        List<String> list = new ArrayList<>();
        String sql = """
                     select c.class_id, c.class_name
                     from Class c, userClass uc, Attendance a
                     where c.class_id = uc.class_id and uc.userClass_id = a.userClass_id and c.lecturer_id = ? and date like ?
                     group by c.class_id, c.class_name""";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            st.setString(2, date);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("class_name"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public List<String> getCLassUn(int id) {
        List<String> list = new ArrayList<>();
        String sql = """
                     select class_id, class_name
                     from Class
                     where lecturer_id = ?""";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("class_name"));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public class Ne {

        private String className;
        private String on;
    }

    public List<Ne> getA(int id, String date) {
        List<Ne> list = new ArrayList<>();
        List<String> list1 = getClassOn(id, date);
        List<String> list2 = getCLassUn(id);
        if (compareDates(date, getDateNow())) {
            for (String list11 : list1) {
                Ne n = new Ne();
                n.setClassName(list11);
                n.setOn("1");
                list.add(n);
            }
        } else {
            for (int i = 0; i < list2.size(); i++) {
                Ne n = new Ne();
                n.setClassName(list2.get(i));
                if (list1.contains(list2.get(i))) {
                    n.setOn("1");
                } else {
                    n.setOn("0");
                }
                list.add(n);
            }
        }
        return list;
    }

    public boolean compareDates(String dateString1, String dateString2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            java.util.Date date1 = dateFormat.parse(dateString1);
            java.util.Date date2 = dateFormat.parse(dateString2);
            return date1.before(date2);
        } catch (ParseException e) {
            System.err.println("Error parsing dates: " + e.getMessage());
            return false;
        }
    }

    public List<TypeSupport> getTypes() {
        List<TypeSupport> list = new ArrayList<>();
        String sql = """
                     select * 
                     from TypeSupport""";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                TypeSupport support = new TypeSupport();
                support.setType_id(rs.getInt("type_id"));
                support.setTitle(rs.getString("title"));
                list.add(support);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }

    public boolean addSupport(Support s) {
        String sql = """
                     INSERT INTO [dbo].[Support]
                                ([content]
                                ,[create_date]
                                ,[status]
                                ,[id]
                                ,[type_id])
                          VALUES (?,?,?,?,?)""";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, s.getContent());
            st.setDate(2, Date.valueOf(getDateNow()));
            st.setString(3, s.getStatus());
            st.setInt(4, s.getAccount().getId());
            st.setInt(5, s.getTs().getType_id());
            st.execute();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public List<Support> getAllSupportByAccountID(int id) {
        List<Support> list = new ArrayList<>();
        String sql = """
                     select * 
                     from Support s, TypeSupport ts
                     where s.type_id = ts.type_id and id = ?""";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Support support = new Support();
                support.setSuport_id(rs.getInt("support_id"));
                support.setContent(rs.getString("content"));
                support.setCreate_date(rs.getString("create_date"));
                support.setEnd_date(rs.getString("end_date"));
                support.setProcess_note(rs.getString("process_note"));
                support.setStatus(rs.getString("status"));
                TypeSupport ts = new TypeSupport();
                ts.setType_id(rs.getInt("type_id"));
                ts.setTitle(rs.getString("title"));
                support.setTs(ts);
                list.add(support);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }
    
    public List<Support> getAllSupport() {
        List<Support> list = new ArrayList<>();
        String sql = """
                     select * 
                     from Support s, TypeSupport ts
                     where s.type_id = ts.type_id """;
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Support support = new Support();
                support.setSuport_id(rs.getInt("support_id"));
                support.setContent(rs.getString("content"));
                support.setCreate_date(rs.getString("create_date"));
                support.setEnd_date(rs.getString("end_date"));
                support.setProcess_note(rs.getString("process_note"));
                support.setStatus(rs.getString("status"));
                TypeSupport ts = new TypeSupport();
                ts.setType_id(rs.getInt("type_id"));
                ts.setTitle(rs.getString("title"));
                support.setTs(ts);
                list.add(support);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return list;
    }
    
    public boolean answerSupport(Support s) {
        String sql = """
                     UPDATE [dbo].[Support]
                        SET [process_note] = ?
                           ,[end_date] = ?
                           ,[status] = ?
                      WHERE support_id = ?""";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, s.getProcess_note());
            st.setDate(2, Date.valueOf(getDateNow()));
            st.setString(3, s.getStatus());
            st.setInt(4, s.getSuport_id());
            st.execute();
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
        return true;
    }
    
    
    
    
    
    public static void main(String[] args) {
        Dao d = new Dao();
        Account a = d.getByUsernameAndPass("lecturer_user", "password123");
        String[] b = {};
        System.out.println(d.getStudentInClass(1, "1", "2024-06-13"));
    }
}