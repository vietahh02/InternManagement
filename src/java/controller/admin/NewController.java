package controller.admin;
import dal.NewDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import model.*;
 

/**
 *
 * @author ADMIN
 */
@MultipartConfig
@WebServlet(name = "NewController", urlPatterns = {"/admin/new-list"})
public class NewController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
      HttpSession session = request.getSession();
      Account a = (Account)session.getAttribute("account");
      NewDAO newDao = new NewDAO();
      if(a!=null && a.getRoleAccount().getRole_id() == 1){
          List<News> news = newDao.getAllNews();
          request.setAttribute("news",news);
          request.getRequestDispatcher("news.jsp").forward(request, response);
      }else{
          response.sendRedirect("../login");
      }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account a = (Account) session.getAttribute("account");
        String action = request.getParameter("action");

        if (a != null && a.getRoleAccount().getRole_id() == 1) {
            NewDAO newDAO = new NewDAO();

            switch (action) {
                case "add" -> {
                    String title = request.getParameter("title");
                    String content = request.getParameter("content");
                    // Validate inputs
                    if (title != null && !title.isEmpty() && content != null && !content.isEmpty()) {
                        News news = new News();
                        news.setTitle(title);
                        news.setContent(content);
                        news.setCreated_at(LocalDateTime.now());
                        news.setCreated_by(a);

                        newDAO.addNews(news);
                        session.setAttribute("notification", "News added successfully!");
                    } else {
                        session.setAttribute("notificationErr", "Title and content cannot be empty!");
                    }
                }
                case "edit" -> {
                    String newsIdStr = request.getParameter("id");
                    String title = request.getParameter("title");
                    String content = request.getParameter("content");
                    if (newsIdStr != null && !newsIdStr.isEmpty() && title != null && !title.isEmpty() && content != null && !content.isEmpty()) {
                        int newsId = Integer.parseInt(newsIdStr);
                        News existingNews = newDAO.getNewsById(newsId);

                        if (existingNews != null) {
                            existingNews.setTitle(title);
                            existingNews.setContent(content);
                            existingNews.setUpdated_at(LocalDateTime.now());

                            newDAO.updateNews(existingNews);
                            session.setAttribute("notification", "News updated successfully!");
                        } else {
                            session.setAttribute("notificationErr", "News not found for ID: " + newsId);
                        }
                    } else {
                        session.setAttribute("notificationErr", "Invalid input for edit operation!");
                    }
                }
                case "delete" -> {
                    String newsIdStr = request.getParameter("id");
                    if (newsIdStr != null && !newsIdStr.isEmpty()) {
                        int newsId = Integer.parseInt(newsIdStr);
                        newDAO.deleteNews(newsId);
                        session.setAttribute("notification", "News deleted successfully!");
                    } else {
                        session.setAttribute("notificationErr", "Invalid news ID for deletion!");
                    }

                }
                default -> {
                    return;
                }
            }

            response.sendRedirect("new-list");
        } else {
            response.sendRedirect("../login");
        }
    }

}
