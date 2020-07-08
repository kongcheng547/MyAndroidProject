package com.example.project2.recycler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project2.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<TestData> mDataset = new ArrayList<>();
    private IOnItemClickListener mItemClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvInfo;
        private TextView tvTime;
        private ImageView tvPic;
        private View contentView;

        public MyViewHolder(View v){
            super(v);
            contentView = v;
            tvTitle = v.findViewById(R.id.the_title);
            tvInfo = v.findViewById(R.id.the_info);
            tvPic = v.findViewById(R.id.the_pic);
            tvTime = v.findViewById(R.id.the_time);
        }

        public void onBind(int position, TestData data) {
            String tempTime = data.time;
            String tempDetailTime = data.detailTime;
            tvTitle.setText(data.title);
            tvInfo.setText(data.info);
            tvPic.setImageResource(data.pic);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date_now = new Date(System.currentTimeMillis());
            try{
                Date date_before = simpleDateFormat.parse(tempTime+" "+tempDetailTime);
                long times= date_now.getTime() -date_before.getTime();
                long days = times/ (1000 * 60* 60 * 24);
                long hours =(times-days*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
                long minutes =(times-days*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
                Log.i("time",days+" "+hours+" "+minutes);
                if(days>=5){
                    tvTime.setText(tempTime);
                }
                else if(days>=1){
                    tvTime.setText(days+"天前");
                }
                else if(hours>=1){
                    tvTime.setText(hours+"小时前");
                }
                else{
                    tvTime.setText(minutes+"分钟前");
                }
            }catch (Exception e){

            }

        }

        public void setOnClickListener(View.OnClickListener listener) {
            if (listener != null) {
                contentView.setOnClickListener(listener);
            }
        }

        public void setOnLongClickListener(View.OnLongClickListener listener) {
            if (listener != null) {
                contentView.setOnLongClickListener(listener);
            }
        }
    }

    public MyAdapter(List<TestData> myDataset) {
        mDataset.addAll(myDataset);
    }

    public void setOnItemClickListener(IOnItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_recycler, parent, false));
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.onBind(position, mDataset.get(position));
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemCLick(position, mDataset.get(position));
                }
            }
        });
        holder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemLongCLick(position, mDataset.get(position));
                }
                return false;
            }

        });
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface IOnItemClickListener {

        void onItemCLick(int position, TestData data);

        void onItemLongCLick(int position, TestData data);
    }
}
