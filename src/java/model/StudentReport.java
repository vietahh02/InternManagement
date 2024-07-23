/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;

/**
 *
 * @author ADMIN
 */
public class StudentReport {

    private Student student;
    private Lecturer lecturer;
    private Report report;
    private Date date;

    public StudentReport(Student student, Lecturer lecturer, Report report, Date date) {
        this.student = student;
        this.lecturer = lecturer;
        this.report = report;
        this.date = date;
    }

    public StudentReport() {
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "StudentReport{" + "student=" + student + ", lecturer=" + lecturer + ", report=" + report + ", date=" + date + '}';
    }

}
