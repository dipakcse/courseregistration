package com.example.assignment4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CourseDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        try {
            setContentView(R.layout.activity_course_details);
            Intent intent = getIntent();
            DataModel dataModel= (DataModel)intent.getSerializableExtra("dataModel");
            TextView courseId = (TextView)findViewById(R.id.courseId);
            TextView courseName = (TextView)findViewById(R.id.courseName);
            TextView term = (TextView)findViewById(R.id.term);
            TextView prerequsite = (TextView)findViewById(R.id.prerequsite);
            courseId.setText(dataModel.getCourseId());
            courseName.setText(dataModel.getCourseName());
            term.setText(dataModel.getTerm());
            prerequsite.setText(dataModel.getPrerequsite());
        }catch (Exception e){
            e.printStackTrace();

        }
    }
}
