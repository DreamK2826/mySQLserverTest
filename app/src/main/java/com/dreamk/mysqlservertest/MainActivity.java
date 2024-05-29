package com.dreamk.mysqlservertest;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.dreamk.mysqlservertest.utils.ToastUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MainActivity extends AppCompatActivity {
    Button btn_test_connection,btn_register,btn_login,btn_check;

    EditText et_sqlAddress,et_sqlPort,et_userName,et_password,et_id_check;

    TextView tv_username1,tv_isDel1;
    SharedPreferences sp;


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

        new Thread(() -> {
            findView();
            setListener();
            sp = getSharedPreferences("addrCfg1", Context.MODE_PRIVATE);
            if (sp.getBoolean("isSaved", false)) {
                et_sqlPort.setText(sp.getString("port", ""));
                et_sqlAddress.setText(sp.getString("addr", ""));
            }
//         else {
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString("addr", et_sqlAddress.getText().toString());
//            editor.putString("port", et_sqlPort.getText().toString());
//            editor.putBoolean("isSaved", true);
//            editor.apply();
//        }
        }).start();

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

    private void setSP() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("addr", et_sqlAddress.getText().toString());
        editor.putString("port", et_sqlPort.getText().toString());
        editor.putBoolean("isSaved", true);
        editor.apply();
    }

    private void setListener(){
        btn_test_connection.setOnClickListener(v -> {
            setSP();
            new Thread(() -> {
                String sqlUser = "userAndroid", sqlPassword = "2826DreamK", dbName = "mytest1";
                String ip_et = et_sqlAddress.getText().toString();
                String port_et_str = et_sqlPort.getText().toString();
                int port_et = 3306;
                if (!port_et_str.isEmpty()) {
                    port_et = Integer.parseInt(port_et_str);
                }
                Connection connection = JDBCUtils.getConnection(sqlUser, sqlPassword, dbName, ip_et, port_et);
                runOnUiThread(() -> {
                    if (connection == null) {
                        ToastUtil.show(this, "connection is null!");
                    } else {
                        ToastUtil.show(this, "connection is OK!");
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }).start();
        });

        btn_check.setOnClickListener(v ->{
            setSP();
            new Thread(() -> {
                String id_et = et_id_check.getText().toString();
                String ip_et = et_sqlAddress.getText().toString();
                String port_et_str = et_sqlPort.getText().toString();
                int port_et = 3306;
                if(!port_et_str.isEmpty()){
                    port_et = Integer.parseInt(port_et_str);
                }
                if(id_et.isEmpty()){
                    id_et = ("-1");
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
                        boolean hasData = false;
                        while (resultSet.next()) {
                            hasData = true;

                            final String[] a = {resultSet.getString("username")};
                            final String[] b = {resultSet.getString("isDel")};
                            runOnUiThread(() -> {
                                tv_username1.setText(a[0]);
                                tv_isDel1.setText(b[0]);
                            });
                        }
                        if (!hasData) {
                            runOnUiThread(() -> {
                                ToastUtil.show(this, "未查询到数据！");
                                tv_username1.setText("无数据");
                                tv_isDel1.setText("无数据");
                            });
                        }
                        resultSet.close();
                        ps.close();
                        connection.close();
                    } else {
                        runOnUiThread(() -> ToastUtil.show(this, "无法连接至mySQL！"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }).start();
        });
    }
}