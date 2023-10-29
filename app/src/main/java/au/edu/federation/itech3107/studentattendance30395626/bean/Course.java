package au.edu.federation.itech3107.studentattendance30395626.bean;

import java.io.Serializable;

public class Course implements Serializable {

    public int id_course;
    public String account;//Teacher account
    public String course;//Course Name
    public String dates[]=new String[12];//Weekly class dates for 12 weeks: year-month-day
    public Course(){
    }
    public Course(String account,String course,String dates[]){
        this.account=account;
        this.course=course;
        this.dates=dates;
    }
}
