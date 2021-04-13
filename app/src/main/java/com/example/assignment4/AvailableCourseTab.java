package com.example.assignment4;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AvailableCourseTab extends Fragment {

    private static ArrayList<DataModel> availableCourse;
    private static ArrayList<DataModel> allCourses;
    private static CourseRegHelper courseRegHelper;
    private static RecyclerView recyclerView;
    private static RecycleViewAdapter recycleViewAdapter;
    Button addButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.available_course_fragment, container, false);
        try {
            courseRegHelper = new CourseRegHelper(getContext());
            if (this.getArguments() !=null) {
                availableCourse = (ArrayList<DataModel>) getArguments().getSerializable("availableCourse");
            }
            addButton = (Button)view.findViewById(R.id.addCourseBtn);
            availableCourse = courseRegHelper.getAvailableCourses();
            recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
            recycleViewAdapter = new RecycleViewAdapter(getContext(),availableCourse);
            recyclerView.setAdapter(recycleViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        }catch (Exception e){
            e.printStackTrace();
        }


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditText courseId = (EditText)view.findViewById(R.id.courseId);
                    EditText courseName = (EditText)view.findViewById(R.id.courseName);
                    EditText term = (EditText)view.findViewById(R.id.term);
                    EditText prerequsite = (EditText)view.findViewById(R.id.prerequsite);

                    if(!TextUtils.isEmpty(courseId.getText()) && !TextUtils.isEmpty(courseName.getText()) && !TextUtils.isEmpty(term.getText())) {
                        courseRegHelper = new CourseRegHelper(getContext());
                        allCourses = courseRegHelper.getAllCourses();
                        int id = allCourses.size();
                        id++;
                        courseRegHelper.insertData(id,courseId.getText().toString(), courseName.getText().toString(), Integer.parseInt(term.getText().toString()),prerequsite.getText().toString(),0,"");
                        availableCourse = courseRegHelper.getAvailableCourses();
                        recycleViewAdapter = new RecycleViewAdapter(getContext(),availableCourse);
                        recyclerView.setAdapter(recycleViewAdapter);
                        recycleViewAdapter.notifyItemInserted(id);
                        Toast.makeText(getActivity(),R.string.added_success,Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getActivity(), R.string.not_empty, Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(AvailableCourseTab.this).attach(AvailableCourseTab.this).commit();
        }
    }

}
