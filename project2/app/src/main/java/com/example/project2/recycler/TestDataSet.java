package com.example.project2.recycler;

import com.example.project2.R;

import java.util.ArrayList;
import java.util.List;

public class TestDataSet {
    public static List<TestData> getData(){
        List<TestData> result = new ArrayList();
        result.add(new TestData("美团外卖","美团外卖，送啥都快","2020-07-07","22:27:22", R.drawable.p1));
        result.add(new TestData("今日头条","你关心的，才是头条","2020-07-06","07:27:22",R.drawable.p2));
        result.add(new TestData("火山小视频","火火火火山","2020-07-05","05:27:22",R.drawable.p3));
        result.add(new TestData("抖音","记录美好生活","2020-07-04","22:27:22",R.drawable.p4));
        result.add(new TestData("淘宝","上淘宝，来淘宝","2020-07-03","04:27:22",R.drawable.p5));
        result.add(new TestData("网易云音乐","听你所爱","2020-07-02","05:27:22",R.drawable.p6));
        result.add(new TestData("爱奇艺视频","看视频来爱奇艺","2020-07-01","03:27:22",R.drawable.p7));
        result.add(new TestData("优酷视频","这世界很酷","2020-6-29","16:03:00",R.drawable.p8));
        result.add(new TestData("有道词典","学英语，有道理","2020-06-09","06:08:50",R.drawable.p9));
        return result;
    }
}
