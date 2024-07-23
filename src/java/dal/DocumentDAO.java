package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Document;

public class DocumentDAO extends DBContext {

    public void uploadDocument(String title, String filePath, int userId) throws SQLException {
        String sql = "INSERT INTO Documents (title, file_path, uploaded_by) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, filePath);
            stmt.setInt(3, userId);
            stmt.executeUpdate();
        }
    }

    public void updateDocument(int documentId, String title, String filePath) throws SQLException {
        String sql = "UPDATE Documents SET title = ?, file_path = ? WHERE document_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, filePath);
            stmt.setInt(3, documentId);
            stmt.executeUpdate();
        }
    }

    public Document getDocument(int documentId) throws SQLException {
        AccountDAO adao = new AccountDAO();
        String sql = "SELECT * FROM Documents WHERE document_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, documentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Document(
                            rs.getInt("document_id"),
                            rs.getString("title"),
                            rs.getString("file_path"),
                            adao.getByID(rs.getInt("uploaded_by")),
                            rs.getDate("upload_date")
                    );
                }
            }
        }
        return null;
    }

    public List<Document> getAllDocuments(int id) throws SQLException {
        List<Document> documents = new ArrayList<>();
        String sql = "SELECT * FROM Documents where uploaded_by = ?";
        AccountDAO adao = new AccountDAO();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    documents.add(new Document(
                            rs.getInt("document_id"),
                            rs.getString("title"),
                            rs.getString("file_path"),
                            adao.getByID(rs.getInt("uploaded_by")),
                            rs.getDate("upload_date")
                    ));
                }
            }
        }
        return documents;
    }

    public void deleteDocument(int documentId) throws SQLException {
        String sql = "DELETE FROM Documents WHERE document_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, documentId);
            stmt.executeUpdate();
        }
    }
}
