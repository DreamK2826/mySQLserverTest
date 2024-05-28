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

public class MainActivity extends AppCompatActivity {
    Button[] buttons = {findViewById(R.id.btn_test_connection),findViewById(R.id.btn_register),
            findViewById(R.id.btn_login),findViewById(R.id.btn_check)};
    EditText[] editTexts = {findViewById(R.id.et_sqlAddress),findViewById(R.id.et_sqlPort),
            findViewById(R.id.et_userName),findViewById(R.id.et_password),
            findViewById(R.id.et_id_check)};
    TextView[] textViews = {findViewById(R.id.tv_username1),findViewById(R.id.tv_isDel1)};

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



    }


}