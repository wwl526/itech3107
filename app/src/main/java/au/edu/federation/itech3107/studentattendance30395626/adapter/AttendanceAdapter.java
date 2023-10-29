package au.edu.federation.itech3107.studentattendance30395626.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395626.R;
import au.edu.federation.itech3107.studentattendance30395626.bean.Course_Student_Attendance;


public class AttendanceAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Course_Student_Attendance> list ;
    HashMap<Integer,View> location = new HashMap<>();


    public AttendanceAdapter(Context context, List<Course_Student_Attendance> list) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.list =list;
    }


    public void setData(List<Course_Student_Attendance> reviews) {
        this.list = reviews;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get View
     * @param position
     * @param convertView
     * return convertView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(location.get(position) == null) {
            convertView = layoutInflater.inflate(R.layout.attendance_adapter,null);
            Course_Student_Attendance course_student_attendance = (Course_Student_Attendance) getItem(position);
            holder = new ViewHolder(convertView,course_student_attendance,position);
            location.put(position,convertView);
            convertView.setTag(holder);
        }else {
            convertView = location.get(position);
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    /**
     * Static ViewHolder.java
     */
    class ViewHolder {
        TextView tv_id_student,tv_name_student;
        CheckBox checkbox;

        public ViewHolder(View itemView, Course_Student_Attendance course_student_attendance, int position) {
            tv_id_student = itemView.findViewById(R.id.tv_id_student);
            tv_name_student=itemView.findViewById(R.id.tv_name_student);
            checkbox=itemView.findViewById(R.id.checkbox);
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!course_student_attendance.isAttendanced) {
                        checkbox.setChecked(true);
                        course_student_attendance.isAttendanced=true;
                    }
                    else{
                        checkbox.setChecked(false);
                        course_student_attendance.isAttendanced=false;
                    }

                }
            });

            tv_id_student.setText(course_student_attendance.id_student);
            tv_name_student.setText(course_student_attendance.name_student);
            checkbox.setChecked(course_student_attendance.isAttendanced);
        }
    }
}
