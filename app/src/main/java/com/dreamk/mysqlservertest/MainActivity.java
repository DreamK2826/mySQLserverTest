package com.dreamk.mysqlservertest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.dreamk.mysqlservertest.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MainActivity extends AppCompatActivity {
    Button btn_test_connection,btn_register,btn_login,btn_check;

    EditText et_sqlAddress,et_sqlPort,et_userName,et_password,et_id_check;

    TextView tv_username1,tv_isDel1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findView();
        setListener();

    }

    private void findView() {
        btn_test_connection = findViewById(R.id.btn_test_connection);
        btn_register = findViewById(R.id.btn_register);
        btn_login = findViewById(R.id.btn_login);
        btn_check = findViewById(R.id.btn_check);

        et_sqlAddress = findViewById(R.id.et_sqlAddress);
        et_sqlPort = findViewById(R.id.et_sqlPort);
        et_userName = findViewById(R.id.et_userName);
        et_password = findViewById(R.id.et_password);
        et_id_check = findViewById(R.id.et_id_check);

        tv_username1 = findViewById(R.id.tv_username1);
        tv_isDel1 = findViewById(R.id.tv_isDel1);
    }

    private void setListener(){

        btn_check.setOnClickListener(v ->{
            new Thread(() -> {
                String id_et = et_id_check.getText().toString();
                String ip_et = et_sqlAddress.getText().toString();
                String port_et_str = et_sqlPort.getText().toString();
                int port_et = 3306;
                if(!port_et_str.isEmpty()){
                    port_et = Integer.parseInt(port_et_str);
                }
                if(id_et.isEmpty()){
                    id_et=("1");
                }
                String sqlUser = "userAndroid",sqlPassword = "2826DreamK",dbName = "mytest1";
                ResultSet resultSet;
                try{
                    Connection connection = JDBCUtils.getConnection(sqlUser,sqlPassword,dbName,ip_et, port_et);
                    String sql1 = "select * from mytest1.test1 where id = (?)";
                    if(connection != null){

                        PreparedStatement ps = connection.prepareStatement(sql1);
                        ps.setInt(1,Integer.parseInt(id_et));
                        resultSet = ps.executeQuery();

                        while (resultSet.next()) {
                            String a = resultSet.getString("username");
                            String b = resultSet.getString("isDel");

                            runOnUiThread(() -> {
                                tv_username1.setText(a);
                                tv_isDel1.setText(b);
                            });
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
//                throw new RuntimeException(e);
                }

            }).start();

        });

    }


}