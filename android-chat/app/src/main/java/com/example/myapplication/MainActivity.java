package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    public ListView lv;
    ArrayList<String> array = new ArrayList<>();
    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simplelist, array);
    EditText inputText;
    EditText inputName;
    String txt;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.submit);
        lv = (ListView) findViewById(R.id.chat_v);
        inputText = (EditText) findViewById(R.id.inputText);
        inputName = (EditText) findViewById(R.id.Name);

        //lv.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                array.add(inputText.getText().toString()); //버튼을 클릭시 array에 추가
                array.add(inputName.getText().toString());
                //adapter.notifyDataSetChanged(); //어댑터 새로고침
                inputText.setText("");
            }
        });

        //입력 데이터 보내기.
        //만약 서로다른 클래스에서  데이털르 주고받을거라면, 데이터를 받는 액티비티에서 intent객체.putExtra(name, data)로 데이터 보내면, 이는 getIntent().getStringExtra("name")으로 데이터를 받게됨.
        name = inputName.getText().toString();
        txt = inputText.getText().toString();//한 액티비티에서만 처리할 예정이므로 EditText의 값을 받아오는 걸로 처리.
        
    }

}
