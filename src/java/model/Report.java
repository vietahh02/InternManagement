/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Report {
    private int id;
    private String type;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private Account sender;
    private Account student_report;
    private String class_name;
    private String student_name;
    private String lecturer_name;
    private float knowledge;
    private float soft_skill;
    private float attitude;
    private float final_grade;
}
