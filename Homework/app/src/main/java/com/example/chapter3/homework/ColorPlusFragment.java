package com.example.chapter3.homework;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public final class ColorPlusFragment extends Fragment {
    private static final String KEY_EXTRA_COLOR = "extra_color";

    public interface Listener {
        void onCollectColor(int color);
    }

    private Listener mListener;

    public static ColorPlusFragment newInstance(int color) {
        ColorPlusFragment cf = new ColorPlusFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_EXTRA_COLOR, color);
        cf.setArguments(args);
        return cf;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        final int color = args != null ? args.getInt(KEY_EXTRA_COLOR, Color.BLUE) : Color.BLUE;
        View view = inflater.inflate(R.layout.fragment_color_plus, container, false);
        view.setBackgroundColor(color);
        view.findViewById(R.id.btn_collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCollectColor(color);
                }
            }
        });
        return view;
    }
}
