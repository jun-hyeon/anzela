package com.example.angela;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {
    ArrayList<Post> arrayList;
    public TimeLineAdapter(ArrayList<Post> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public TimeLineAdapter.TimeLineViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.coming_list,parent,false);
        return new TimeLineViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TimeLineAdapter.TimeLineViewHolder holder, int position) {
        String title = arrayList.get(position).getTitle();
        int count = arrayList.get(position).getCurCnt();
        String date = arrayList.get(position).getStartDate();

        holder.comingListTitle.setText(title);
        if(count == -1){
            holder.comingListCount.setText("제한없음");
        }else{
            holder.comingListCount.setText("최대 "+count+"명");
        }
        holder.comingListDate.setText(date.substring(0,date.indexOf(" ")));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class TimeLineViewHolder extends RecyclerView.ViewHolder {
        TextView comingListTitle;
        TextView comingListCount;
        TextView comingListDate;

        public TimeLineViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
           comingListTitle = (TextView)  itemView.findViewById(R.id.cominglistTitle);
           comingListCount = (TextView) itemView.findViewById(R.id.cominglistCount);
           comingListDate = (TextView) itemView.findViewById(R.id.comingDateText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Post post = arrayList.get(position);
                    int id = post.getId();
                    Log.e("POSITION",""+position + " " + post.getTitle()+" "+id);
                    Intent intent = new Intent(v.getContext(),ListDetailActivity.class);
                    intent.putExtra("id",id);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
