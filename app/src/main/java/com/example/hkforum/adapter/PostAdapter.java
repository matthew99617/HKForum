package com.example.hkforum.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hkforum.R;
import com.example.hkforum.model.Comment;
import com.example.hkforum.model.PostsImage;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.myViewHolder> {

    private DatabaseReference referenceComment = FirebaseDatabase.getInstance().getReference("Comments");
    private ArrayList<PostsImage> list;
    private Context context;
    private String strUsername;

    int i = 0;
    CommentAdapter commentAdapter;
    private ArrayList<Comment> comments = new ArrayList<>();

    public PostAdapter(String username, ArrayList<PostsImage> list, Context context) {
        this.strUsername = username;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_post, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        PostsImage postsImage = list.get(position);
        holder.tvTitle.setText(postsImage.getShowTitle());
        holder.tvUsername.setText(postsImage.getShowUsername());
        holder.tvContent.setText(postsImage.getShowText());

        if (postsImage.getImageUrl() != null) {
            holder.imageView.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(postsImage.getImageUrl())
                    .into(holder.imageView);
        }

        holder.commentSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = holder.edComment.getText().toString();
                if (comment.isEmpty()) {
                    Toast.makeText(context.getApplicationContext(), "Please write something!", Toast.LENGTH_LONG).show();
                } else {
                    String username = strUsername;
                    String title = postsImage.getShowTitle();
                    String strComment = holder.edComment.getText().toString();
                    addComment(username, title, strComment);
                    holder.edComment.setText("");
                }
            }

            private void addComment(String username, String title, String comment) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("Username", username);
                map.put("Title", title);
                map.put("Comment", comment);

                referenceComment.push().setValue(map);
            }
        });

        referenceComment.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("Title").getValue().toString().equals(postsImage.getShowTitle())) {
                        Comment comment = dataSnapshot.getValue(Comment.class);
                        comments.add(comment);
                    }
                }
                holder.recyclerView.setHasFixedSize(true);
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                commentAdapter = new CommentAdapter(postsImage.getShowTitle(), comments, context);
                holder.recyclerView.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        }


//        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        commentOption = new FirebaseRecyclerOptions.Builder<Comment>().setQuery(referenceComment,Comment.class).build();
//        commentAdapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(commentOption) {
//            @Override
//            protected void onBindViewHolder(@NonNull CommentViewHolder commentHolder, int position, @NonNull Comment model) {
//                commentHolder.tvComment.setText(model.getShowComment());
//                commentHolder.tvUsername.setText(model.getShowUsername());
//
//            }
//
//            @NonNull
//            @Override
//            public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(context).inflate(R.layout.row_comment,parent, false);
//                return new CommentViewHolder(view);
//            }
//        };

//        commentAdapter.startListening();
//        holder.recyclerView.setAdapter(commentAdapter);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvUsername, tvContent;
        private ImageView imageView, commentSend;
        private EditText edComment;
        RecyclerView recyclerView;

        public myViewHolder(final View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.showTitle);
            tvUsername = itemView.findViewById(R.id.showUserName);
            tvContent = itemView.findViewById(R.id.showContent);
            imageView = itemView.findViewById(R.id.showImage);
            commentSend = itemView.findViewById(R.id.commentSend);
            edComment = itemView.findViewById(R.id.commentPost);
            recyclerView = itemView.findViewById(R.id.recCommentView);
        }
    }
}
