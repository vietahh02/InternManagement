package dal;

import model.News;
import java.util.*;
import java.sql.*;
import java.time.LocalDateTime;
import model.Question;

public class QuestionDAO extends DBContext {

 

    public List<Question> getAllQuestionForStudent(int studentId) {
        List<Question> questions = new ArrayList<>();
        AccountDAO accountDAO = new AccountDAO();
        try {
            String query = "SELECT * FROM Question where sender  = ? order by created_at desc";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, studentId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Question q = new Question();
                    q.setId(rs.getInt("id"));
                    q.setTitle(rs.getString("title"));
                    q.setContent(rs.getString("content"));
                    Timestamp createdTimestamp = rs.getTimestamp("created_at");
                    if (createdTimestamp != null) {
                        q.setCreated_at(createdTimestamp.toLocalDateTime());
                    }
                    Timestamp updatedTimestamp = rs.getTimestamp("updated_at");
                    if (updatedTimestamp != null) {
                        q.setUpdated_at(updatedTimestamp.toLocalDateTime());
                    }
                    q.setSender(accountDAO.getByID(rs.getInt("sender")));
                    q.setReceiver(accountDAO.getByID(rs.getInt("receiver")));
                    q.setStatus(rs.getString("status"));
                    q.setReply(rs.getString("reply"));

                    questions.add(q);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }
    public List<Question> getAllQuestionLecturer(int lecturer) {
        List<Question> questions = new ArrayList<>();
        AccountDAO accountDAO = new AccountDAO();
        try {
            String query = "SELECT * FROM Question where receiver  = ? order by created_at desc";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, lecturer);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Question q = new Question();
                    q.setId(rs.getInt("id"));
                    q.setTitle(rs.getString("title"));
                    q.setContent(rs.getString("content"));
                    Timestamp createdTimestamp = rs.getTimestamp("created_at");
                    if (createdTimestamp != null) {
                        q.setCreated_at(createdTimestamp.toLocalDateTime());
                    }
                    Timestamp updatedTimestamp = rs.getTimestamp("updated_at");
                    if (updatedTimestamp != null) {
                        q.setUpdated_at(updatedTimestamp.toLocalDateTime());
                    }
                    q.setSender(accountDAO.getByID(rs.getInt("sender")));
                    q.setReceiver(accountDAO.getByID(rs.getInt("receiver")));
                    q.setStatus(rs.getString("status"));
                    q.setReply(rs.getString("reply"));

                    questions.add(q);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public void addQuestion(Question question) {
        String query = "INSERT INTO [Question] (title, content, created_at, updated_at, sender, receiver, status) VALUES (?, ?, ?, ?, ?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, question.getTitle());
            preparedStatement.setString(2, question.getContent());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(question.getCreated_at()));
            preparedStatement.setTimestamp(4,  null);
            preparedStatement.setInt(5, question.getSender().getId()); // Assuming created_by is an Account object
            preparedStatement.setInt(6, question.getReceiver().getId()); // Assuming created_by is an Account object
            preparedStatement.setString(7, "Processing");

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a news record
    public void updateNews(Question question) {
        String query = "UPDATE [Question] SET title = ?, content = ?,  updated_at = ? , reply = ?, status = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, question.getTitle());
            preparedStatement.setString(2, question.getContent());

            preparedStatement.setTimestamp(3, question.getUpdated_at() != null ? Timestamp.valueOf(question.getUpdated_at()) : null);
            preparedStatement.setString(4, question.getReply());
            preparedStatement.setString(5, question.getStatus());
            preparedStatement.setInt(6, question.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a news record
    public void deleteNews(int id) {
        String query = "DELETE FROM [Question] WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Question getQuestionById(int id) {
        Question question = null;
        String query = "SELECT * FROM [Question] WHERE id = ? order by created_at desc";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    question = new Question();
                    question.setId(rs.getInt("id"));
                    question.setTitle(rs.getString("title"));
                    question.setContent(rs.getString("content"));
                    Timestamp createdTimestamp = rs.getTimestamp("created_at");
                    if (createdTimestamp != null) {
                        question.setCreated_at(createdTimestamp.toLocalDateTime());
                    }
                    Timestamp updatedTimestamp = rs.getTimestamp("updated_at");
                    if (updatedTimestamp != null) {
                        question.setUpdated_at(updatedTimestamp.toLocalDateTime());
                    }
                    // Assuming created_by is an Account object and AccountDAO.getByID() method is implemented
                    question.setSender(new AccountDAO().getByID(rs.getInt("sender")));
                    question.setReceiver(new AccountDAO().getByID(rs.getInt("sender")));
                    question.setReply(rs.getString("reply"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return question;
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

    public static void main(String[] args) {
        QuestionDAO dAO = new QuestionDAO();
        System.out.println(dAO.getAllQuestionLecturer(3));
    }
}
