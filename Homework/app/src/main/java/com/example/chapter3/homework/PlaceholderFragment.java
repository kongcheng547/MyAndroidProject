package com.example.chapter3.homework;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class PlaceholderFragment extends Fragment {

    private LottieAnimationView lottieAnimationView;
    private TextView textView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件
        //在xml中已经添加了
        return inflater.inflate(R.layout.fragment_placeholder, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lottieAnimationView=getView().findViewById(R.id.lottie_anim);
        textView=getView().findViewById(R.id.text_view_lll);
        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
                lottieAnimationView.animate().alpha(0).setDuration(1000);
                textView.animate().alpha(1).setDuration(1000);
            }
        }, 5000);
    }
}
