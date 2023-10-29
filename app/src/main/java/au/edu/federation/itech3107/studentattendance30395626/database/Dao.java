package au.edu.federation.itech3107.studentattendance30395626.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395626.bean.Course;
import au.edu.federation.itech3107.studentattendance30395626.bean.Course_Student_Attendance;
import au.edu.federation.itech3107.studentattendance30395626.bean.User;


//Add, Delete, Update, and Query SQLite databases
public class Dao {
    private Context context;
    private DataBaseHelper dbHelper;//Database Management Objects
    private SQLiteDatabase db;      //Objects that can read and write to the database

    public Dao(Context context) {
        this.context = context;
    }

    //Create and open a database (directly open if the database already exists)
    public void open() throws SQLiteException {
        dbHelper = new DataBaseHelper(context);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLiteException exception) {
            db = dbHelper.getReadableDatabase();
        }
    }

    //Close database
    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    //Add user
    public void addUser(User user) {
        ContentValues values = new ContentValues();
        values.put("account", user.account);
        values.put("password", user.password);
        db.insert("user", null, values);
    }

    //Query user
    public boolean find(User user) {
        //Query the records of where specified column elements in the user table
        Cursor cursor = db.query("user", null, "account = ?", new String[]{user.account}, null, null, null);
        if (cursor == null || cursor.getCount() < 1) {
            return false;
        }
        cursor.close();
        return true;
    }


    //Determine if the account exists
    public boolean isExist(String account) {
        Cursor cursor = db.query("user", null, "account = ?", new String[] {account}, null, null, null);
        return cursor != null && cursor.getCount() > 0;
    }

    //Find the user password based on the account, and verify the password when logging in
    public String getPassword(String account) {
        Cursor cursor = db.query("user", null, "account = ?", new String[] {account}, null, null, null);
        cursor.moveToFirst();
        String password = cursor.getString(cursor.getColumnIndex("password"));
        return password;
    }


    //Add course
    public void addCourse(Course course) {
        ContentValues values = new ContentValues();
        values.put("account", course.account);
        values.put("course", course.course);

        String dates_str="";
        for (int i=0;i<course.dates.length;i++)
            dates_str+=" "+course.dates[i];

        dates_str=dates_str.substring(1);
        values.put("dates", dates_str);

        db.insert("course", null, values);
    }

    //Query course
    public boolean find(Course course) {
        //查询course表中where指定列元素的记录
        Cursor cursor = db.query("course", null, "account = ? and course = ?", new String[]{course.account,course.course}, null, null, null);
        if (cursor == null || cursor.getCount() < 1) {
            return false;
        }
        cursor.close();
        return true;
    }

    //Query All course
    public List<Course> findAllCourse(String account) {
        List<Course> courseList=new ArrayList<>();
        //Query the records of where specified column elements in the course table
        Cursor cursor = db.query("course", null, "account = ?", new String[]{account}, null, null, null);
        if (cursor == null || cursor.getCount() < 1) {
            return courseList;
        }
        if (cursor.moveToFirst()) {
            do {
                //Traverse the cursor object, retrieve data
                Course course=new Course();
                course.id_course = cursor.getInt(cursor.getColumnIndex("id_course"));
                course.account = cursor.getString(cursor.getColumnIndex("account"));
                course.course = cursor.getString(cursor.getColumnIndex("course"));

                String dates_str=cursor.getString(cursor.getColumnIndex("dates"));
                course.dates=dates_str.split(" ");
                courseList.add(course);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return courseList;
    }

    //Delete course
    public void remove(Course course) {
        db.delete("course", "id_course = ?", new String[]{course.id_course+""});
    }



    //Query the students of a certain teacher's course
    public boolean find(Course_Student_Attendance course_student_attendance) {
        //Query the records of where specified column elements in the course_student_attendance table
        Cursor cursor = db.query("course_student_attendance", null, "id_course = ? and id_student = ?", new String[]{course_student_attendance.id_course+"",course_student_attendance.id_student}, null, null, null);
        if (cursor == null || cursor.getCount() < 1) {
            return false;
        }
        cursor.close();
        return true;
    }

    //Add students to a certain teacher's course
    public void addCourse_Student_Attendance(Course_Student_Attendance course_student_attendance) {
        ContentValues values = new ContentValues();
        values.put("id_course", course_student_attendance.id_course);
        values.put("id_student", course_student_attendance.id_student);
        values.put("name_student", course_student_attendance.name_student);
        values.put("class_student", course_student_attendance.class_student);
        values.put("date", course_student_attendance.date);
        values.put("isattendanced", course_student_attendance.isAttendanced+"");
        db.insert("course_student_attendance", null, values);
    }

    //Find all attendance for a certain day and class
    public List<Course_Student_Attendance> findAllCourse_Student_Attendance(Course course,String date,String class_student) {
        List<Course_Student_Attendance> course_student_attendanceList=new ArrayList<>();
        //Query the records of where specified column elements in the course_student_attendance table
        Cursor cursor = db.query("course_student_attendance", null, "id_course = ? and date = ?", new String[]{course.id_course+"",date}, null, null, null);
        if(class_student!=null)
            cursor =db.query("course_student_attendance", null, "id_course = ? and date = ? and class_student = ?", new String[]{course.id_course+"",date,class_student}, null, null, null);
        if (cursor == null || cursor.getCount() < 1) {
            return course_student_attendanceList;
        }
        if (cursor.moveToFirst()) {
            do {
                //Traverse the cursor object, retrieve data
                Course_Student_Attendance course_student_attendance=new Course_Student_Attendance();
                course_student_attendance.id_course= cursor.getInt(cursor.getColumnIndex("id_course"));
                course_student_attendance.id_student = cursor.getString(cursor.getColumnIndex("id_student"));
                course_student_attendance.name_student = cursor.getString(cursor.getColumnIndex("name_student"));
                course_student_attendance.class_student = cursor.getString(cursor.getColumnIndex("class_student"));
                course_student_attendance.date = cursor.getString(cursor.getColumnIndex("date"));
                course_student_attendance.isAttendanced = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isattendanced")));
                course_student_attendanceList.add(course_student_attendance);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return course_student_attendanceList;
    }

    //Update attendance
    public void update(Course_Student_Attendance course_student_attendance) {
        ContentValues values = new ContentValues();
        values.put("id_course", course_student_attendance.id_course);
        values.put("id_student", course_student_attendance.id_student);
        values.put("name_student", course_student_attendance.name_student);
        values.put("class_student", course_student_attendance.class_student);
        values.put("date", course_student_attendance.date);
        values.put("isattendanced", course_student_attendance.isAttendanced+"");
        db.update("course_student_attendance", values,
                "id_course = ? and id_student = ? and date = ?",
                new String[]{course_student_attendance.id_course+"",course_student_attendance.id_student,course_student_attendance.date});
    }


}