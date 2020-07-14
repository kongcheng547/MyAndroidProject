package com.example.project6.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project6.R;
import com.example.project6.database.TodoListEntity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
public class TodoListItemHolder extends RecyclerView.ViewHolder {
    private TextView mContent;
    private TextView mTimestamp;
    private CheckBox checkBox;

    public TodoListItemHolder(@NonNull View itemView) {
        super(itemView);
        mContent = itemView.findViewById(R.id.tv_content);
        mTimestamp = itemView.findViewById(R.id.tv_timestamp);
        checkBox=itemView.findViewById(R.id.checkbox);
    }

    public void bind(TodoListEntity entity) {
        mContent.setText(entity.getContent());
        mTimestamp.setText(formatDate(entity.getTime()));
        if(entity.getIsFinished()==true){
            checkBox.setChecked(true);
            mContent.setPaintFlags(mContent.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mContent.setTextColor(Color.GRAY);
        }
        else{
            checkBox.setChecked(false);
            mContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mContent.setPaintFlags(mContent.getPaintFlags()&(~Paint.STRIKE_THRU_TEXT_FLAG));
            mContent.setTextColor(Color.BLACK);
        }
    }

    private String formatDate(Date date) {
        DateFormat format = SimpleDateFormat.getDateInstance();
        return format.format(date);
    }
}
