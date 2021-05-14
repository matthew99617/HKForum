package com.example.hkforum.adapter;

import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hkforum.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;

import com.example.hkforum.model.Comment;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.myViewHolder> {
    private ArrayList<Comment> list;
    private Context context;
    private String title;

    public CommentAdapter(String title, ArrayList<Comment> list, Context context) {
        this.list = list;
        this.context = context;
        this.title = title;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_comment, parent, false);
        return new myViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.myViewHolder holder, int position) {
        Comment comment = list.get(position);
        System.out.println(comment.getShowTitle());
        if (comment.getShowTitle().equals(title)){
            holder.constraintLayoutComment.setVisibility(View.VISIBLE);
            holder.tvUsername.setText(comment.getShowUsername());
            holder.tvComment.setText(comment.getShowComment());
        } else if (comment.getShowUsername() == null && comment.getShowComment() == null){
            holder.constraintLayoutComment.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayoutComment;
        private TextView tvUsername, tvComment;

        public myViewHolder(final View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.commentUsername);
            tvComment = itemView.findViewById(R.id.tvShowComment);
            constraintLayoutComment = itemView.findViewById(R.id.constraintLayoutComment);
        }
    }
}
