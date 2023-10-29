package au.edu.federation.itech3107.studentattendance30395626;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import au.edu.federation.itech3107.studentattendance30395626.activity.MainActivity;
import au.edu.federation.itech3107.studentattendance30395626.activity.RegisterActivity;
import au.edu.federation.itech3107.studentattendance30395626.database.Dao;


public class LoginActivity extends AppCompatActivity {
    private Button btn_login;    //Login button
    private Button btn_register; //Registration button
    private EditText et_account; //Account input box
    private EditText et_password;//Password input box
    private Dao dao;         //Database object
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        initView();
    }

    public void initView() {
        //Bound control
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);

        //Anonymous internal class implementation button click event
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String acc = et_account.getText().toString().trim();
                String pass = et_password.getText().toString().trim();

                if(acc.equals("")||pass.equals("")) {
                    Toast.makeText(LoginActivity.this, R.string.LoginActivity_java__Failed1, Toast.LENGTH_SHORT).show();
                    return;
                }

                dao = new Dao(getApplicationContext());
                dao.open();
                if (dao.isExist(acc) == false) {
                    Toast.makeText(LoginActivity.this, R.string.LoginActivity_java__Failed2, Toast.LENGTH_SHORT).show();
                } else {
                    if (dao.getPassword(acc).equals(pass)) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("account",acc);
                        Toast.makeText(LoginActivity.this, R.string.LoginActivity_java__Successfully1, Toast.LENGTH_SHORT).show();
                        //Create an intent object and jump to it
                        startActivity(intent);
                        //Destroy the activity
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.LoginActivity_java__Failed3, Toast.LENGTH_SHORT).show();
                    }
                }
                dao.close();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            //Determine if the result code is equal to 1, and if it is equal to 1, accept the returned data.
            if(requestCode == 1 && resultCode == 1){
                String name = data.getStringExtra("acc");
                String password = data.getStringExtra("pass");
                et_account.setText(name);
                et_password.setText(password);
            }
        }
    }
}

