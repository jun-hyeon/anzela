package com.example.angela;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder> {
   ArrayList<Comment> arrayList;

    public CommentsAdapter(ArrayList<Comment> arrayList) {
        this.arrayList = arrayList;
    }

    public void setArrayList(ArrayList<Comment> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @NotNull
    @Override
    public CommentsAdapter.CommentsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment,parent,false);
        return new CommentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CommentsAdapter.CommentsViewHolder holder, int position) {
        String profilUrl = arrayList.get(position).getUser().getProfileUrl();
        String cmUid = arrayList.get(position).getUser().getUid();
        String cmRegDate = arrayList.get(position).getRegDate();
        String cmContent = arrayList.get(position).getContent();
        Glide.with(holder.itemView.getContext()).load(profilUrl).into(holder.cmProfile);
        holder.cmUid.setText(cmUid);
        holder.cmRegDate.setText(cmRegDate);
        holder.cmContent.setText(cmContent);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CommentsViewHolder extends RecyclerView.ViewHolder {
        CircleImageView cmProfile;
        TextView cmUid;
        TextView cmRegDate;
        TextView cmContent;

        public CommentsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            cmProfile = itemView.findViewById(R.id.cmProfile);
            cmUid = itemView.findViewById(R.id.cmUid);
            cmRegDate = itemView.findViewById(R.id.cmRegDate);
            cmContent = itemView.findViewById(R.id.cmContent);
        }
    }
}
