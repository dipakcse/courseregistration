package com.example.assignment4;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder>{
    private static ArrayList<DataModel> dataModels;
    private Context mContext;

    public RecycleViewAdapter(Context context,ArrayList<DataModel> dataModel) {
        mContext = context;
        dataModels = dataModel;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);

        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.courseId.setText(dataModels.get(position).getCourseId());
        holder.courseName.setText(dataModels.get(position).getCourseName());
        holder.term.setText(dataModels.get(position).getTerm());
        //holder.prerequsite.setText(dataModels.get(position).getPrerequsite());
        if(!TextUtils.isEmpty(dataModels.get(position).getImageUrl())) {
            Picasso.get().load(dataModels.get(position).getImageUrl()).placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background).resize(100, 80).centerCrop().into(holder.imageView);
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Object object = dataModels.get(position);
                    DataModel dataModel = (DataModel)object;
                    Intent intent = new Intent(mContext, CourseDetailsActivity.class);
                    intent.putExtra("dataModel", dataModel);
                    mContext.startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout relativeLayout;
        TextView courseId;
        TextView courseName;
        TextView term;
        TextView prerequsite;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseId = (TextView) itemView.findViewById(R.id.courseId);
            courseName = (TextView) itemView.findViewById(R.id.courseName);
            term = (TextView) itemView.findViewById(R.id.term);
            //prerequsite = (TextView) itemView.findViewById(R.id.prerequsite);
            imageView = (ImageView) itemView.findViewById(R.id.courseImage);
            relativeLayout=(RelativeLayout) itemView.findViewById(R.id.relative_view);
        }
    }

}
