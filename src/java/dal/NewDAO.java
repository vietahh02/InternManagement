package dal;

import model.News;
import java.util.*;
import java.sql.*;
import java.time.LocalDateTime;

public class NewDAO extends DBContext {

    // Method to retrieve all news
    public List<News> getAllNews() {
        List<News> news = new ArrayList<>();
        AccountDAO accountDAO = new AccountDAO();
        try {
            String query = "SELECT id, title, content, created_at, updated_at, created_by FROM [new]";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    News n = new News();
                    n.setId(rs.getInt("id"));
                    n.setTitle(rs.getString("title"));
                    n.setContent(rs.getString("content"));
                    Timestamp createdTimestamp = rs.getTimestamp("created_at");
                    if (createdTimestamp != null) {
                        n.setCreated_at(createdTimestamp.toLocalDateTime());
                    }
                    Timestamp updatedTimestamp = rs.getTimestamp("updated_at");
                    if (updatedTimestamp != null) {
                        n.setUpdated_at(updatedTimestamp.toLocalDateTime());
                    }
                    n.setCreated_by(accountDAO.getByID(rs.getInt("created_by")));

                    news.add(n);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return news;
    }

    // Method to add a news record
    public void addNews(News news) {
        String query = "INSERT INTO [new] (title, content, created_at, updated_at, created_by) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getContent());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(news.getCreated_at()));
            preparedStatement.setTimestamp(4, news.getUpdated_at() != null ? Timestamp.valueOf(news.getUpdated_at()) : null);
            preparedStatement.setInt(5, news.getCreated_by().getId()); // Assuming created_by is an Account object

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a news record
    public void updateNews(News news) {
        String query = "UPDATE [new] SET title = ?, content = ?, created_at = ?, updated_at = ?, created_by = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getContent());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(news.getCreated_at()));
            preparedStatement.setTimestamp(4, news.getUpdated_at() != null ? Timestamp.valueOf(news.getUpdated_at()) : null);
            preparedStatement.setInt(5, news.getCreated_by().getId()); // Assuming created_by is an Account object
            preparedStatement.setInt(6, news.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a news record
    public void deleteNews(int id) {
        String query = "DELETE FROM [new] WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public News getNewsById(int id) {
        News news = null;
        String query = "SELECT id, title, content, created_at, updated_at, created_by FROM [new] WHERE id = ? order by created_at desc";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    news = new News();
                    news.setId(rs.getInt("id"));
                    news.setTitle(rs.getString("title"));
                    news.setContent(rs.getString("content"));
                    Timestamp createdTimestamp = rs.getTimestamp("created_at");
                    if (createdTimestamp != null) {
                        news.setCreated_at(createdTimestamp.toLocalDateTime());
                    }
                    Timestamp updatedTimestamp = rs.getTimestamp("updated_at");
                    if (updatedTimestamp != null) {
                        news.setUpdated_at(updatedTimestamp.toLocalDateTime());
                    }
                    // Assuming created_by is an Account object and AccountDAO.getByID() method is implemented
                    news.setCreated_by(new AccountDAO().getByID(rs.getInt("created_by")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return news;
    }

    public static void main(String[] args) {
        NewDAO newDAO = new NewDAO();
        List<News> list = newDAO.getAllNews();
        list.forEach(System.out::println);

        // Example usage of add, update, and delete methods
        News news = new News();
        news.setTitle("New Title");
        news.setContent("New Content");
        news.setCreated_at(LocalDateTime.now());
        news.setCreated_by(new AccountDAO().getByID(1)); // Replace with valid Account object

        // Adding a new news record
        newDAO.addNews(news);

        // Updating an existing news record
        news.setId(1); // Set the ID of the news you want to update
        news.setTitle("Updated Title");
        newDAO.updateNews(news);

        // Deleting a news record
        newDAO.deleteNews(1); // Replace with the ID of the news you want to delete
    }
}
