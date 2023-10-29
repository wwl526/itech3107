package au.edu.federation.itech3107.studentattendance30395626.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE = "attendance.db";
    public static final int VERSION = 1;

    //Define table building statements as string constants
    public static final String CREATE_USER = "create table user ("
            + "account text primary key,"
            + "password text)";
    public static final String CREATE_COURSE = "create table course ("
            + "id_course INTEGER primary key autoincrement,"
            + "account text,"
            + "course text,"
            + "dates text)";
    public static final String CREATE_Course_Student_Attendance = "create table course_student_attendance  ("
            + "id_course int,"
            + "id_student text,"
            + "name_student text,"
            + "class_student text,"
            + "date text,"
            + "isattendanced text)";


    //Constructor when creating DB objects
    public DataBaseHelper(Context context) {
        super(context, DATABASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_COURSE);
        db.execSQL(CREATE_Course_Student_Attendance);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



}
