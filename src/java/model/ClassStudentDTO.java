/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ADMIN
 */
public class ClassStudentDTO {
    private int student_id;
    private String rollNumber;
    private String email;
    private String fullName;
    private String phone;
    private String Major;
    private String Company;
    private String JobTitle;
    private String LinkCV;

    public ClassStudentDTO() {
    }

    public ClassStudentDTO(int student_id, String rollNumber, String email, String fullName, String phone, String Major, String Company, String JobTitle, String LinkCV) {
        this.student_id = student_id;
        this.rollNumber = rollNumber;
        this.email = email;
        this.fullName = fullName;
        this.phone = phone;
        this.Major = Major;
        this.Company = Company;
        this.JobTitle = JobTitle;
        this.LinkCV = LinkCV;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMajor() {
        return Major;
    }

    public void setMajor(String Major) {
        this.Major = Major;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String Company) {
        this.Company = Company;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String JobTitle) {
        this.JobTitle = JobTitle;
    }

    public String getLinkCV() {
        return LinkCV;
    }

    public void setLinkCV(String LinkCV) {
        this.LinkCV = LinkCV;
    }

    @Override
    public String toString() {
        return "ClassStudentDTO{" + "student_id=" + student_id + ", rollNumber=" + rollNumber + ", email=" + email + ", fullName=" + fullName + ", phone=" + phone + ", Major=" + Major + ", Company=" + Company + ", JobTitle=" + JobTitle + ", LinkCV=" + LinkCV + '}';
    }
    
    
    
}
