package com.example.project6.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.room.Database;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;
import com.example.project6.R;
import com.example.project6.database.*;
import com.google.android.material.snackbar.Snackbar;
import com.example.project6.activity.TodoListActivity;


public class AddNoteActivity extends Activity {

    private EditText editText;
    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note_layout);
        editText=findViewById(R.id.add_note);
        confirm=findViewById(R.id.confirm_Button);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.toString()!="Please input your note here")
                {
                    TodoListDao dao = TodoListDatabase.inst(AddNoteActivity.this).todoListDao();
                    dao.addTodo(new TodoListEntity(editText.getText().toString(),new Date(System.currentTimeMillis())));
                    Snackbar.make(confirm, "本条数据已插入", Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    Snackbar.make(confirm,"Please input!",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}
