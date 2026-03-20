package com.college.erp.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "exams")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String examName;
    private String department;
    private String subject;
    private LocalDate examDate;
    private int totalMarks;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private Integer marksObtained;
    private String grade;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getExamName() { return examName; }
    public void setExamName(String examName) { this.examName = examName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }
    public int getTotalMarks() { return totalMarks; }
    public void setTotalMarks(int totalMarks) { this.totalMarks = totalMarks; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Integer getMarksObtained() { return marksObtained; }
    public void setMarksObtained(Integer marksObtained) { this.marksObtained = marksObtained; }
    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}