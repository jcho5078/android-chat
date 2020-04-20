package com.example.testchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.submit);
        tv = (TextView) findViewById(R.id.chat_v);

        tv.append("냥냥펀치" + "\n");
        tv.append("냥냥펀치" + "\n");
    }
}
