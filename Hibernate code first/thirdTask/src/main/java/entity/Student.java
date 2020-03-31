package entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student extends People {
    private Double averageGrade;
    private String attendance;
    private Set<Course> courses;


    public Student() {
    }

    @Column(name = "average_grade")
    public Double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(Double averageGrade) {
        this.averageGrade = averageGrade;
    }

    @Column
    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

  @ManyToMany
  @JoinTable(name = "students_courses",
          joinColumns = @JoinColumn(name = "students_id",referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "courses_id",referencedColumnName = "id"))
    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

}
