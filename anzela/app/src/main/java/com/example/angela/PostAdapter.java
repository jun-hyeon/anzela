package com.example.angela;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    ArrayList<Post> arrayList;


    public PostAdapter(ArrayList<Post> arrayList) {
        this.arrayList = arrayList;


    }

    @NonNull
    @NotNull
    @Override
    public PostAdapter.PostViewHolder
    onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView   = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recycler,parent,false);
        return new PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PostAdapter.PostViewHolder holder, int position) {
        String title = arrayList.get(position).getTitle();
        int count = arrayList.get(position).getCurCnt();
        String date = arrayList.get(position).getStartDate();
        holder.list_Title.setText(title);
        holder.listCount.setText("최대 "+count+"명");
        holder.setDate.setText(String.format(date, "yyyy. mm. dd"));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        TextView list_Title;
        TextView listCount;
        TextView setDate;

        public PostViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            list_Title = itemView.findViewById(R.id.list_Title);
            listCount = itemView.findViewById(R.id.listCount);
            setDate = itemView.findViewById(R.id.setDate);
        }
    }
}
