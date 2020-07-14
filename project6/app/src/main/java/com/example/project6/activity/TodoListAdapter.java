package com.example.project6.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project6.database.TodoListEntity;
import com.example.project6.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
public class TodoListAdapter extends RecyclerView.Adapter<TodoListItemHolder> {
    private List<TodoListEntity> mDatas;
    CheckBox checkBox;
    Button deleteButton;
    TextView tv_Text;

    public TodoListAdapter() {
        mDatas = new ArrayList<>();
    }

    @NonNull
    @Override
    public TodoListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TodoListItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListItemHolder holder, final int position) {
        //这里的position是内部的要申明不可修改，只有读取权限
        holder.bind(mDatas.get(position));
        if(mOnItemClickListener!=null){
            deleteButton = holder.itemView.findViewById(R.id.delete_Button);
            checkBox = holder.itemView.findViewById(R.id.checkbox);
            tv_Text = holder.itemView.findViewById(R.id.tv_content);
            final String theContent = tv_Text.getText().toString();

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(view, position, theContent);
                }
            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onClick(view, position, theContent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @MainThread
    public void setData(List<TodoListEntity> list) {
        mDatas = list;
        notifyDataSetChanged();
    }

    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener=onItemClickListener;
    }

    public interface OnItemClickListener{
        void onClick(View v, int position, String theContent);
    }
}
