package com.example.assignment4;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Dipak on 08/11/19.
 */

public class CustomAdapter extends ArrayAdapter<DataModel>{

    private ArrayList<DataModel> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView courseId;
        TextView courseName;
        TextView term;
        TextView prerequsite;
        ImageView imageView;
    }



    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            DataModel dataModel = getItem(position);

            ViewHolder viewHolder;

            final View result;

            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.row_item, parent, false);
                viewHolder.courseId = (TextView) convertView.findViewById(R.id.courseId);
                viewHolder.courseName = (TextView) convertView.findViewById(R.id.courseName);
                viewHolder.term = (TextView) convertView.findViewById(R.id.term);
                viewHolder.prerequsite = (TextView) convertView.findViewById(R.id.prerequsite);
                viewHolder.imageView = (ImageView)convertView.findViewById(R.id.courseImage);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.courseId.setText(dataModel.getCourseId());
            viewHolder.courseName.setText(dataModel.getCourseName());
            viewHolder.term.setText(dataModel.getTerm());
            viewHolder.courseName.setTag(position);
            //viewHolder.prerequsite.setText(dataModel.getPrerequsite());
            if(!TextUtils.isEmpty(dataModel.getImageUrl())) {
                Picasso.get().load(dataModel.getImageUrl()).placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background).resize(100, 80).centerCrop().into(viewHolder.imageView);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }


}
