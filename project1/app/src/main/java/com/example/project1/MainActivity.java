package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button1);
        final TextView tv = findViewById(R.id.textView2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("You have click the button!");
            }
        });
        Log.i("button1", "You click on button1");
        Log.i("autoCompleteTextView","autoCompleteTextView: You have edited the text");
        Log.i("checkBox","checkBox: You have checked it!");
        Log.i("switch2","switch2: You have switched!");
    }
}
