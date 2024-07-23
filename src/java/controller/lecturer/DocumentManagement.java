
import dal.DocumentDAO;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import model.Account;
import model.Document;

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50) // 50MB
@WebServlet(name = "DocumentManagement", urlPatterns = {"/lecturer/document-manager"})
public class DocumentManagement extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        DocumentDAO documentDAO = new DocumentDAO();
        if (a != null && a.getRoleAccount().getRole_id() == 3) {
            try {
                List<Document> listDocument = documentDAO.getAllDocuments(a.getId());
                request.setAttribute("listDocument", listDocument);
                request.getRequestDispatcher("document-manager.jsp").forward(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(DocumentManagement.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            response.sendRedirect("../login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        DocumentDAO documentDAO = new DocumentDAO();
        if (a != null && a.getRoleAccount().getRole_id() == 3) {
            String action = request.getParameter("action");
            if (null != action) switch (action) {
                case "upload" -> {
                    String title = request.getParameter("title");
                    Part filePart = request.getPart("file");
                    String fileName = getFileName(filePart);
                    // Ensure uploads directory exists
                    String uploadsDirPath = getServletContext().getRealPath("") + File.separator + "uploads";
                    File uploadsDir = new File(uploadsDirPath);
                    if (!uploadsDir.exists()) {
                        uploadsDir.mkdirs();
                    }       String filePath = uploadsDirPath + File.separator + fileName;
                    filePart.write(filePath);
                    try {
                        documentDAO.uploadDocument(title, "uploads" + File.separator + fileName, a.getId());
                        session.setAttribute("notification", "Document update successfully! ");
                    } catch (SQLException ex) {
                        session.setAttribute("notificationErr", "Document update faild! ");
                        Logger.getLogger(DocumentManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }       response.sendRedirect("document-manager");
                    }
                case "update" -> {
                    int documentId = Integer.parseInt(request.getParameter("document_id"));
                    String title = request.getParameter("title");
                    Part filePart = request.getPart("file");
                    String currentFilePath = request.getParameter("current_file");
                    String filePath = currentFilePath; // Default to current file path
                    if (filePart != null && filePart.getSize() > 0) {
                        String fileName = getFileName(filePart);
                        
                        // Ensure uploads directory exists
                        String uploadsDirPath = getServletContext().getRealPath("") + File.separator + "uploads";
                        File uploadsDir = new File(uploadsDirPath);
                        if (!uploadsDir.exists()) {
                            uploadsDir.mkdirs();
                        }
                        
                        filePath = uploadsDirPath + File.separator + fileName;
                        filePart.write(filePath);
                        
                        filePath = "uploads" + File.separator + fileName; // Update with new file path
                    }       try {
                        documentDAO.updateDocument(documentId, title, filePath);
                        session.setAttribute("notification", "Document updated successfully! ");
                    } catch (SQLException ex) {
                        session.setAttribute("notificationErr", "Document update failed! ");
                        Logger.getLogger(DocumentManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }       response.sendRedirect("document-manager");
                    }
                case "delete" -> {
                    int documentId = Integer.parseInt(request.getParameter("document_id"));
                    try {
                        documentDAO.deleteDocument(documentId);
                        session.setAttribute("notification", "Document deleted successfully! ");
                    } catch (SQLException ex) {
                        session.setAttribute("notificationErr", "Document deletion failed! ");
                        Logger.getLogger(DocumentManagement.class.getName()).log(Level.SEVERE, null, ex);
                    }       response.sendRedirect("document-manager");
                    }
                default -> {
                }
            }
        } else {
            response.sendRedirect("../login");
        }
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] items = contentDisposition.split(";");
        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                return item.substring(item.indexOf("=") + 2, item.length() - 1);
            }
        }
        return "";
    }
}
