package com.example.project6.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.project6.database.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.project6.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TodoListActivity extends AppCompatActivity {

    private TodoListAdapter mAdapter;
    private FloatingActionButton mFab;
    private CheckBox checkBox;
    private Button delete_Note;
//    static public int versionNum=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_list_activity_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.rv_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new TodoListAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mFab = findViewById(R.id.fab);
        delete_Note=findViewById(R.id.delete_Button);
        checkBox=findViewById(R.id.checkbox);
        //点击了加号
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
                startActivity(new Intent(TodoListActivity.this, AddNoteActivity.class));
            }
        });

        mFab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                        dao.deleteAll();
                        for (int i = 0; i < 20; ++i) {
                            dao.addTodo(new TodoListEntity("This is " + i + " item", new Date(System.currentTimeMillis())));
                        }
                        Snackbar.make(mFab, R.string.hint_insert_complete, Snackbar.LENGTH_SHORT).show();
                    }
                }.start();
                loadFromDatabase();
                return true;
            }
        });
//        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//            }
//        });

        mAdapter.setOnItemClickListener(new TodoListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position, String theContent) {
                final View tempView = v;
                final int tempPosition = position;
                final String tempContent = theContent;
                //如果点击了checkbox，更新状态
                if(v.getId()==R.id.checkbox){
                    changeState(tempView, tempPosition, tempContent);
                    loadFromDatabase();
                }
                //如果点击了删除按钮，删除数据库里面的内容
                else if(v.getId()==R.id.delete_Button){
                    deleteOneRecord(tempView, tempPosition, tempContent);
                    loadFromDatabase();
                }
            }
        });
        loadFromDatabase();
    }

    private void loadFromDatabase() {
        new Thread() {
            @Override
            public void run() {
                TodoListDao dao = TodoListDatabase.inst(TodoListActivity.this).todoListDao();
                //读取列表
                final List<TodoListEntity> undoList = dao.loadAll(false);
                final List<TodoListEntity> doList = dao.loadAll(true);
                Collections.reverse(undoList);
                Collections.reverse(doList);
                undoList.addAll(doList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(undoList);
                    }
                });
            }
        }.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadFromDatabase();
    }

    //点下checkbox改变记录的状态
    private void changeState(View v, int position, final String content) {
        new Thread(){
            @Override
            public void run() {
                TodoListEntity tempEntity = queryByContent(content);
                //先获取现在的check状态
                boolean nowCheck =tempEntity.getIsFinished();

                if(nowCheck==false) nowCheck=true;///更新状态
                else nowCheck = false;

                tempEntity.setIsFinished(nowCheck);
                //根据check状态更改数据库内容
                TodoListDatabase.inst(TodoListActivity.this).todoListDao().updateEntity(tempEntity);
                loadFromDatabase();
            }
        }.start();
    }

    //按X按钮就删除一条记录
    private void deleteOneRecord(View v, int position, final String content){
        new Thread(){
            @Override
            public void run() {
                final TodoListEntity oneEntity = queryByContent(content);
                TodoListDatabase.inst(TodoListActivity.this).todoListDao().deleteRecord(oneEntity);
                Snackbar.make(mFab, "删除成功", Snackbar.LENGTH_SHORT).show();
                loadFromDatabase();
            }
        }.start();
    }

    private TodoListEntity queryByContent(String content){
        final TodoListEntity tempTodo = TodoListDatabase.inst(TodoListActivity.this).todoListDao().getEntity(content);
        return tempTodo;
    }
}
