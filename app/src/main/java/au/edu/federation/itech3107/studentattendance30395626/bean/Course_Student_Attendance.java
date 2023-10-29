package au.edu.federation.itech3107.studentattendance30395626.bean;

import java.util.TreeMap;

public class Course_Student_Attendance {
    public int id_course;//Teacher - Course ID
    public String id_student;//Student ID
    public String name_student;//Student Name
    public String class_student;//Student's class
    public String date;//Date
    public boolean isAttendanced;//Whether to be present or not

    public Course_Student_Attendance(int id_course, String id_student, String name_student,String class_student,String date){
        this.id_course=id_course;
        this.id_student=id_student;
        this.name_student=name_student;
        this.class_student=class_student;
        this.date=date;
    }
    public Course_Student_Attendance(int id_course, String id_student){
        this.id_course=id_course;
        this.id_student=id_student;
    }
    public Course_Student_Attendance(){
    }
}
