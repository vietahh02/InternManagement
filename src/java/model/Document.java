
package model;

import java.sql.Date;

/**
 *
 * @author ADMIN
 */
public class Document {

    private int documentId;
    private String title;
    private String filePath;
    private Account uploadedBy;
    private Date uploadDate;

    public Document(int documentId, String title, String filePath, Account uploadedBy, Date uploadDate) {
        this.documentId = documentId;
        this.title = title;
        this.filePath = filePath;
        this.uploadedBy = uploadedBy;
        this.uploadDate = uploadDate;
    }

    public Document() {
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Account getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(Account uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

}
