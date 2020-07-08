package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.project2.recycler.MyAdapter;
import com.example.project2.recycler.TestData;
import com.example.project2.recycler.TestDataSet;

public class MainActivity extends AppCompatActivity implements MyAdapter.IOnItemClickListener{

    private MyAdapter myAdap;   /*adpter的对象*/
    private RecyclerView reV;   /*recycler的对象*/
    private RecyclerView.LayoutManager layManager;  /*布局管理对象*/

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {//生成界面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initAll();
    }

    //初始化页面
    private void initAll(){//初始化界面
        reV = (RecyclerView) findViewById(R.id.recycler);
        reV.setHasFixedSize(true);

        layManager = new LinearLayoutManager(this);
        reV.setLayoutManager(layManager);

        myAdap = new MyAdapter(TestDataSet.getData());
        myAdap.setOnItemClickListener(this);
        reV.setAdapter(myAdap);

        reV.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onItemCLick(int position, TestData data) {
        Toast.makeText(MainActivity.this, "点击了第" + position + "条", Toast.LENGTH_SHORT).show();
        Intent it = new Intent(MainActivity.this, Navi_page.class);
        it.putExtra("position",position);
        startActivity(it);
    }

    @Override
    public void onItemLongCLick(int position, TestData data) {
        Toast.makeText(MainActivity.this, "长按了第" + position + "条", Toast.LENGTH_SHORT).show();
        Intent it = new Intent(MainActivity.this, Navi_page.class);
        it.putExtra("position",position);
        startActivity(it);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "MainActivity onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "MainActivity onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "MainActivity onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "MainActivity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "MainActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "MainActivity onDestroy");
    }
}
