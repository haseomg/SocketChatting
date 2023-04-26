package com.example.socketchatting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterActivity extends AppCompatActivity {

    EditText name;
    Button enter;
    String TAG = "[EnterActivity]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);

        initialize();
        setEnter();
    } // onCreate End

    public void initialize() {
        name = findViewById(R.id.nameEditText);
        enter = findViewById(R.id.enterBtn);
    } // initialize End

    public void setEnter() {
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "enter button onClick()");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                String userName = name.getText().toString();
                Log.i(TAG, "userName Check : " + userName);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });
    }
}