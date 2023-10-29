package au.edu.federation.itech3107.studentattendance30395626.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import au.edu.federation.itech3107.studentattendance30395626.LoginActivity;
import au.edu.federation.itech3107.studentattendance30395626.bean.User;
import au.edu.federation.itech3107.studentattendance30395626.database.Dao;
import au.edu.federation.itech3107.studentattendance30395626.R;


public class RegisterActivity extends AppCompatActivity {
    private EditText reg_name;
    private EditText reg_password,reg_password1;
    private Button btn_reg;
    private Button btn_quit;
    private Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    public void initView() {

        //Bound control
        reg_name = findViewById(R.id.reg_name);
        reg_password = findViewById(R.id.reg_password);
        reg_password1 = findViewById(R.id.reg_password1);
        btn_reg = findViewById(R.id.btn_reg);
        btn_quit = findViewById(R.id.btn_quit);
        //Click Event
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String acc = reg_name.getText().toString().trim();
                String pass = reg_password.getText().toString().trim();
                String pass1 = reg_password1.getText().toString().trim();
                
                if(acc.equals("")||pass.equals("")||pass1.equals("")) {
                    Toast.makeText(RegisterActivity.this, R.string.RegisterActivity_java__Failed1, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pass.equals(pass1)){
                    Toast.makeText(RegisterActivity.this, R.string.RegisterActivity_java__Failed2, Toast.LENGTH_SHORT).show();
                    return;
                }

                User user = new User(acc, pass);
                dao = new Dao(getApplicationContext());
                dao.open();
                if (dao.find(user) == true) {
                    Toast.makeText(RegisterActivity.this, R.string.RegisterActivity_java__Failed3, Toast.LENGTH_SHORT).show();
                } else {
                    dao.addUser(user);
                    Toast.makeText(RegisterActivity.this, R.string.RegisterActivity_java__Successfully1, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    //Pass the account and password over
                    intent.putExtra("account", acc);
                    intent.putExtra("password", pass);
                    setResult(1, intent);
                    finish();
                }
                dao.close();
            }
        });
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
