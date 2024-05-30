package com.dreamk.mysqlservertest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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


public class MainActivity extends AppCompatActivity {
    private Button btn_test_connection, btn_register, btn_login, btn_check;

    private EditText et_sqlAddress, et_sqlPort, et_userName, et_password, et_id_check;

    private TextView tv_username1, tv_isDel1;
    private SharedPreferences sp, sp2;

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
            sp2 = getSharedPreferences("userCfg1", Context.MODE_PRIVATE);

            if (sp.getBoolean("isSaved", false)) {
                et_sqlPort.setText(sp.getString("port", ""));
                et_sqlAddress.setText(sp.getString("addr", ""));
            }
            if (sp2.getBoolean("userIsSaved", false)) {
                et_userName.setText(sp2.getString("username", ""));
                et_password.setText(sp2.getString("password", ""));
            }


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

    private void setSPuser() {
        SharedPreferences.Editor editor = sp2.edit();
        editor.putString("username", et_userName.getText().toString());
        editor.putString("password", et_password.getText().toString());
        editor.putBoolean("userIsSaved", true);
        editor.apply();
    }


    private void setListener() {
        btn_test_connection.setOnClickListener(v -> {
            setSP();
            new Thread(() -> {
                String sqlUser = "userAndroid";
                String sqlPassword = "2826DreamK";
                String dbName = "mytest1";
                String ip_et = et_sqlAddress.getText().toString();
                String port_et_str = et_sqlPort.getText().toString();
                int port_et = 3306;
                if (!port_et_str.isEmpty()) {
                    port_et = Integer.parseInt(port_et_str);
                }
                Connection connection = JDBCUtils.getConnection(sqlUser, sqlPassword, dbName, ip_et, port_et);
                runOnUiThread(() -> {
                    if (connection == null) {
                        ToastUtil.show(this, "connection is null!", Gravity.TOP);
                    } else {
                        ToastUtil.show(this, "connection is OK!", Gravity.TOP);
                        try {
                            connection.close();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }).start();
        });

        btn_check.setOnClickListener(v -> {
            setSP();
            new Thread(() -> {
                String sqlUser = "userAndroid";
                String sqlPassword = "2826DreamK";
                String dbName = "mytest1";
                String ip_et = et_sqlAddress.getText().toString();
                String port_et_str = et_sqlPort.getText().toString();
                int port_et = 3306;
                if (!port_et_str.isEmpty()) {
                    port_et = Integer.parseInt(port_et_str);
                }
                String id_et = et_id_check.getText().toString();
                if (id_et.isEmpty()) {
                    id_et = ("-1");
                }
                ResultSet resultSet;
                try {
                    Connection connection = JDBCUtils.getConnection(sqlUser, sqlPassword, dbName, ip_et, port_et);
                    String sql1 = "select * from mytest1.test1 where id = (?)";
                    if (connection != null) {
                        PreparedStatement ps = connection.prepareStatement(sql1);
                        ps.setInt(1, Integer.parseInt(id_et));
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
                                ToastUtil.show(this, "未查询到数据！", Gravity.BOTTOM);
                                tv_username1.setText("无数据");
                                tv_isDel1.setText("无数据");
                            });
                        }
                        resultSet.close();
                        ps.close();
                        connection.close();
                    } else {
                        runOnUiThread(() -> ToastUtil.show(this, "无法连接至mySQL！", Gravity.BOTTOM));
                    }
                } catch (SQLException e) {
                    Log.e("SQL:", "btn_check.setListener: ", e);

                    throw new RuntimeException(e);
                }
            }).start();
        });

        btn_register.setOnClickListener(v -> new Thread(() -> {
            String sqlUser = "userAndroid";
            String sqlPassword = "2826DreamK";
            String dbName = "mytest1";
            String ip_et = et_sqlAddress.getText().toString();
            String port_et_str = et_sqlPort.getText().toString();
            int port_et = 3306;
            if (!port_et_str.isEmpty()) {
                port_et = Integer.parseInt(port_et_str);
            }
            String username_et = et_userName.getText().toString();
            String password_et = et_password.getText().toString();
            if (!username_et.isEmpty() && !password_et.isEmpty()) {
                setSPuser();
                Connection connection = JDBCUtils.getConnection(sqlUser, sqlPassword, dbName, ip_et, port_et);
                if (!(connection == null)) {
                    try {
                        String un0 = et_userName.getText().toString();
                        String up0 = et_password.getText().toString();
                        connection.setAutoCommit(false);
                        //先查询数据库是否存在相同用户名
                        PreparedStatement preparedStatement;
                        preparedStatement = connection.prepareStatement("select * from mytest1.test1 where username = ?");
                        preparedStatement.setString(1, un0);
                        preparedStatement.executeQuery();
                        connection.commit();
                        ResultSet rs = preparedStatement.executeQuery();
                        if (rs.next()) {
                            runOnUiThread(() -> ToastUtil.show(this, "该用户名已存在！", Gravity.CENTER));
                            connection.commit();
                            preparedStatement.close();
                            connection.close();
                        } else {
                            //不存在相同用户名时，插入数据到SQL
                            preparedStatement = connection.prepareStatement("insert into mytest1.test1(username, password, isDel) values(?,?,?)");
                            preparedStatement.setString(1, un0);
                            preparedStatement.setString(2, up0);
                            preparedStatement.setInt(3, 0);
                            preparedStatement.executeUpdate();
                            connection.commit();
                            preparedStatement.close();
                            connection.close();
                            runOnUiThread(() -> ToastUtil.show(this, "注册成功！", Gravity.CENTER));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    runOnUiThread(() -> ToastUtil.show(this, "Connection is Null", Gravity.CENTER));
                }
            } else {
                runOnUiThread(() -> ToastUtil.show(this, "请先检查输入是否有误！", Gravity.CENTER));
            }
        }).start());

        btn_login.setOnClickListener(v -> new Thread(() -> {
            String sqlUser = "userAndroid";
            String sqlPassword = "2826DreamK";
            String dbName = "mytest1";
            String ip_et = et_sqlAddress.getText().toString();
            String port_et_str = et_sqlPort.getText().toString();
            int port_et = 3306;
            if (!port_et_str.isEmpty()) {
                port_et = Integer.parseInt(port_et_str);
            }
            setSPuser();
            Connection connection = JDBCUtils.getConnection(sqlUser, sqlPassword, dbName, ip_et, port_et);
            if (!(connection == null)) {
                try {
                    //查询是否有一样用户名和密码并且isDel != 1 的用户
//                    connection.setAutoCommit(false);
                    PreparedStatement preparedStatement = connection.prepareStatement("select * from mytest1.test1 where isDel != '1' && username = ? && password = ?");
                    String un1 = et_userName.getText().toString();
                    String up1 = et_password.getText().toString();
                    preparedStatement.setString(1, un1);
                    preparedStatement.setString(2, up1);
                    preparedStatement.execute();
//                    connection.commit();

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {

                        runOnUiThread(() -> ToastUtil.show(this, "正在登录...", Gravity.CENTER));
                        Intent intent = new Intent(this, MainActivity2.class);
                        startActivity(intent);
                    } else {
                        runOnUiThread(() -> ToastUtil.show(this, "无法登录！请检查账户是否正确！", Gravity.CENTER));
                    }

                    preparedStatement.close();
                    connection.close();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                runOnUiThread(() -> ToastUtil.show(this, "Connection is Null", Gravity.CENTER));

            }

        }).start());
    }
}