//package com.example.hkforum.adapter;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.hkforum.R;
//import com.example.hkforum.ToForum;
//import com.example.hkforum.model.District;
//import com.example.hkforum.model.DistrictSingleton;
//
//import java.util.ArrayList;
//
//public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.myViewHolder> {
//    private ArrayList<District> districts;
//
//    public LocationAdapter(ArrayList<District> districts){
//        this.districts = districts;
//    }
//
//    @NonNull
//    @Override
//    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_forum,parent, false);
//        return new myViewHolder(view);
//    }
//
//    @SuppressLint("SetTextI18n")
//    @Override
//    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
//        String location = districts.get(position).getTvDistrict();
//        holder.tvDistrict.setText(location+" Forum");
//
//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick (View v){
//
//                Intent intent = new Intent(v.getContext(), ToForum.class);
//                intent.putExtra("Location",location);
//                v.getContext().startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return districts.size();
//    }
//
//    public class myViewHolder extends RecyclerView.ViewHolder{
//        private TextView tvDistrict;
//
//        public myViewHolder(final View itemView){
//            super(itemView);
//            tvDistrict = itemView.findViewById(R.id.tvDistrictLocation);
//        }
//    }
//}
