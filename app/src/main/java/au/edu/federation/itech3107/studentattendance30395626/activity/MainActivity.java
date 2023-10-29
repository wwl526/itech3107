package au.edu.federation.itech3107.studentattendance30395626.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395626.bean.Course;
import au.edu.federation.itech3107.studentattendance30395626.bean.Course_Student_Attendance;
import au.edu.federation.itech3107.studentattendance30395626.database.Dao;
import au.edu.federation.itech3107.studentattendance30395626.R;

public class MainActivity extends AppCompatActivity {
    Dao dao;
    String account;
    ListView listView;
    List<Course> courseList;
    List<String> courseStrList=new ArrayList<>();

    boolean isLongClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        account=getIntent().getStringExtra("account");

        dao = new Dao(getApplicationContext());

        listView=findViewById(R.id.listView);
        //Long press on the course, use student ID and name to add students for each course
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                isLongClick=true;
                AlertDialog.Builder customizeDialog = new AlertDialog.Builder(MainActivity.this);
                customizeDialog.setTitle(R.string.MainActivity_java__Select);
                customizeDialog.setPositiveButton(R.string.MainActivity_java__AddStudents, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addStudent(i);
                    }
                });
                customizeDialog.setNegativeButton(R.string.MainActivity_java__DeleteCourse, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeCourse(i);
                    }
                });
                customizeDialog.show();
                return false;
            }
        });
        //Click on the course/unit to display a new page for marking attendance status
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(isLongClick) {
                    isLongClick=false;
                    return;
                }
                Course course=courseList.get(i);
                Intent intent=new Intent(MainActivity.this, AttendanceActivity.class);
                intent.putExtra("course",course);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateListView();
    }

    //Update ListView
    void updateListView(){
        dao.open();
        courseList=dao.findAllCourse(account);
        dao.close();
        courseStrList.clear();
        for (Course course : courseList) {
            courseStrList.add(course.course);
        }

        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, courseStrList));
    }

    //Add course
    EditText[] et_dates=new EditText[12];
    String date;
    String dates[];
    public void addCourse(View v){
        dates=new String[12];

        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(this);

        final View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.course_dialog, null);

        EditText editText = dialogView.findViewById(R.id.et_course);
        et_dates[0]=dialogView.findViewById(R.id.et_date1);
        et_dates[1]=dialogView.findViewById(R.id.et_date2);
        et_dates[2]=dialogView.findViewById(R.id.et_date3);
        et_dates[3]=dialogView.findViewById(R.id.et_date4);
        et_dates[4]=dialogView.findViewById(R.id.et_date5);
        et_dates[5]=dialogView.findViewById(R.id.et_date6);
        et_dates[6]=dialogView.findViewById(R.id.et_date7);
        et_dates[7]=dialogView.findViewById(R.id.et_date8);
        et_dates[8]=dialogView.findViewById(R.id.et_date9);
        et_dates[9]=dialogView.findViewById(R.id.et_date10);
        et_dates[10]=dialogView.findViewById(R.id.et_date11);
        et_dates[11]=dialogView.findViewById(R.id.et_date12);
        for(int i=0;i<et_dates.length;i++){
            int finalI = i;
            et_dates[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus)
                        selectDate(finalI);
                }
            });
        }

        customizeDialog.setTitle(R.string.MainActivity__AddCourse);
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the input in the EditView
                String courseStr = editText.getText().toString().trim();
                if(courseStr.equals("")){
                    Toast.makeText(MainActivity.this, R.string.MainActivity_java__Failed1, Toast.LENGTH_SHORT).show();
                    return;
                }
                for(int i=0;i<dates.length;i++){
                    if(dates[i]==null){
                        Toast.makeText(MainActivity.this, getString(R.string.MainActivity_java__Failed2_1)+(i+1)+getString(R.string.MainActivity_java__Failed2_2), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Course course=new Course(account,courseStr,dates);

                dao.open();
                if (dao.find(course) == true) {
                    Toast.makeText(MainActivity.this, R.string.MainActivity_java__Failed3, Toast.LENGTH_SHORT).show();
                } else {
                    dao.addCourse(course);
                    updateListView();
                    Toast.makeText(MainActivity.this, getString(R.string.MainActivity_java__Successfully1_1)+courseStr+getString(R.string.MainActivity_java__Successfully1_2), Toast.LENGTH_SHORT).show();
                }
                dao.close();
            }
        });
        customizeDialog.setNegativeButton(R.string.No,null);
        customizeDialog.show();
    }

    //Select Date
    void selectDate(int index){
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.date_dialog, null);
        date=null;
        CalendarView cv = dialogView.findViewById(R.id.calendarView);
        //Add an event listener for the date change event of the CalendarView component
        cv.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String str_year=year+"";
            String str_month=(month+1<10)?("0"+(month+1)):(""+(month+1));
            String str_dayOfMonth=(dayOfMonth<10)?("0"+dayOfMonth):(""+dayOfMonth);
            date=str_year+"-"+str_month+"-"+str_dayOfMonth;

        });
        customizeDialog.setTitle(getString(R.string.MainActivity_java__SetDate1_1)+(index+1)+getString(R.string.MainActivity_java__SetDate1_2));
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(date==null){
                    Toast.makeText(MainActivity.this, R.string.MainActivity_java__Failed4, Toast.LENGTH_SHORT).show();
                    return;
                }
                dates[index]=date;
                et_dates[index].setText(date);
            }
        });
        customizeDialog.setNegativeButton(R.string.No,null);
        customizeDialog.show();
    }

    //Delete course
    public void removeCourse(int index){
        Course course=courseList.get(index);
        dao.open();
        dao.remove(course);
        dao.close();
        updateListView();
        Toast.makeText(MainActivity.this, R.string.MainActivity_java__Successfully2, Toast.LENGTH_SHORT).show();
    }

    //Add Students
    void addStudent(int index){
        Course course=courseList.get(index);
        AlertDialog.Builder customizeDialog = new AlertDialog.Builder(MainActivity.this);
        final View dialogView = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.student_dialog, null);
        EditText et_studentId = dialogView.findViewById(R.id.et_studentId);
        EditText et_studentName = dialogView.findViewById(R.id.et_studentName);
        EditText et_studentClass= dialogView.findViewById(R.id.et_studentClass);
        customizeDialog.setTitle(R.string.MainActivity_java__Dialog_AddStudents);
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Obtain input content from EditView
                String studentId= et_studentId.getText().toString().trim();
                String studentName= et_studentName.getText().toString().trim();
                String studentClass= et_studentClass.getText().toString().trim();
                if(studentId.equals("")||studentName.equals("")||studentClass.equals("")){
                    Toast.makeText(MainActivity.this, R.string.MainActivity_java__Failed5, Toast.LENGTH_SHORT).show();
                    return;
                }

                Course_Student_Attendance course_student_attendance=new Course_Student_Attendance(course.id_course,studentId);
                dao.open();
                if (dao.find(course_student_attendance) == true) {
                    Toast.makeText(MainActivity.this, R.string.MainActivity_java__Failed6, Toast.LENGTH_SHORT).show();
                }
                else {
                    for (String date : course.dates) {
                        course_student_attendance=new Course_Student_Attendance(course.id_course,studentId,studentName,studentClass,date);
                        dao.addCourse_Student_Attendance(course_student_attendance);
                    }
                    Toast.makeText(MainActivity.this, getString(R.string.MainActivity_java__Successfully3_1)+studentId+getString(R.string.MainActivity_java__Successfully3_2)+studentName+getString(R.string.MainActivity_java__Successfully3_3), Toast.LENGTH_SHORT).show();
                }
                dao.close();
            }
        });
        customizeDialog.setNegativeButton(R.string.No,null);
        customizeDialog.show();
    }


}