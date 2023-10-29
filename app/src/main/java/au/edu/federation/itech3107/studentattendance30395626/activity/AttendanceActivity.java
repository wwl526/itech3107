package au.edu.federation.itech3107.studentattendance30395626.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import au.edu.federation.itech3107.studentattendance30395626.R;
import au.edu.federation.itech3107.studentattendance30395626.adapter.AttendanceAdapter;
import au.edu.federation.itech3107.studentattendance30395626.bean.Course;
import au.edu.federation.itech3107.studentattendance30395626.bean.Course_Student_Attendance;
import au.edu.federation.itech3107.studentattendance30395626.database.Dao;

public class AttendanceActivity extends AppCompatActivity {
    Dao dao;
    Course course;
    Spinner spinner_date;
    ListView listView ;
    List<Course_Student_Attendance> course_student_attendanceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tattendance);
        course=(Course)getIntent().getSerializableExtra("course");

        dao = new Dao(getApplicationContext());

        setTitle(getString(R.string.AttendanceActivity_java__Title1_1)+course.course+getString(R.string.AttendanceActivity_java__Title1_2));

        //Select Date
        spinner_date = (Spinner) findViewById(R.id.spinner_date);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,course.dates);
        spinner_date.setAdapter(adapter);
        spinner_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String date=course.dates[i];
                dao.open();
                course_student_attendanceList=dao.findAllCourse_Student_Attendance(course,date,null);
                dao.close();
                listView.setAdapter(new AttendanceAdapter(AttendanceActivity.this, course_student_attendanceList));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView=findViewById(R.id.listView);

    }

    //Save Attendance
    public void save(View v){
        if(course_student_attendanceList!=null) {
            dao.open();
            for (int i = 0; i < course_student_attendanceList.size(); i++) {
                dao.update(course_student_attendanceList.get(i));
            }
            dao.close();
            Toast.makeText(AttendanceActivity.this, R.string.AttendanceActivity_java__Successfully1, Toast.LENGTH_SHORT).show();
        }
    }
}