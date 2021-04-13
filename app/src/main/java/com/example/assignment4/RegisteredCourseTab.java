package com.example.assignment4;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class RegisteredCourseTab extends Fragment {

    private static ArrayList<DataModel> availableCourse;
    private static ArrayList<DataModel> registeredCourse;
    private static ArrayList<DataModel> allCourses;
    private static ListView listView;
    private static Button registerBtn;
    private static CourseRegHelper courseRegHelper;
    private static CustomAdapter adapter;
    private String postApiUrl = "http://35.203.95.76:8809/register/";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.registered_course_fragment, container, false);
        try {
            courseRegHelper = new CourseRegHelper(getContext());
            availableCourse = new ArrayList<>();
            registeredCourse = new ArrayList<>();
            listView = (ListView)view.findViewById(R.id.list);
            registerBtn = (Button) view.findViewById(R.id.registerBtn);
            availableCourse = courseRegHelper.getAvailableCourses();
            registeredCourse = courseRegHelper.getRegisteredCourses();

            adapter = new CustomAdapter(registeredCourse,getActivity());

            listView.setAdapter(adapter);
            addItemsOnSpinner2(view);
        }catch (Exception e){
            e.printStackTrace();
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DataModel deletedCourse = registeredCourse.get(position);
                try{
                    showDialog(registeredCourse,deletedCourse,position);
                } catch (Exception e){
                    e.printStackTrace();
                }

                return false;
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner1);
                    DataModel selectedCourse = availableCourse.get(spinner1.getSelectedItemPosition());
                    int totalTermCourses = getPerTermCourses(registeredCourse, selectedCourse);
                    String prerequisiteCourse = ifPrerequisite(registeredCourse, selectedCourse);

                    if (!isRegistered(registeredCourse, selectedCourse)) {
                        if (totalTermCourses < 3) {
                            if (TextUtils.isEmpty(prerequisiteCourse)) {
                                allCourses = courseRegHelper.getAllCourses();
                                int id = allCourses.size();
                                id++;
                                courseRegHelper.insertData(id,selectedCourse.getCourseId(), selectedCourse.getCourseName(), Integer.parseInt(selectedCourse.getTerm()),selectedCourse.getPrerequsite(),1,selectedCourse.getImageUrl());
                                registeredCourse = courseRegHelper.getRegisteredCourses();
                                adapter= new CustomAdapter(registeredCourse,getActivity());
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), R.string.register_success, Toast.LENGTH_LONG).show();
                                JSONObject jsonParams = new JSONObject();
                                jsonParams.put("student_id","201905075");
                                jsonParams.put("name","Dipak Kumar Mondal");
                                jsonParams.put("email","x2019eyg@stfx.ca");
                                jsonParams.put("course",selectedCourse.getCourseName());
                                StringEntity entity = new StringEntity(jsonParams.toString());

                                asyncHttpClient.post(getContext(),postApiUrl,entity,"application/json", new JsonHttpResponseHandler(){
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                        super.onSuccess(statusCode, headers, response);


                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        System.out.println("post response message:"+response);
                                        System.out.println("post response code:::"+statusCode);
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        super.onFailure(statusCode, headers, responseString, throwable);
                                    }
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                                        super.onFailure(statusCode, headers,throwable,response);
                                        System.out.println("post response message:"+response);
                                        System.out.println("post response code:::"+statusCode);
                                    }

                                    @Override
                                    public void onRetry(int retryNo) {
                                        System.out.println("retryNo::"+retryNo);
                                    }
                                });

                            }else {
                                Toast.makeText(getContext(), "You could not register this course because you have following "+prerequisiteCourse+ " prerequisite Course.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "You could not add more than 3 courses in a term.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Course already is registered.", Toast.LENGTH_LONG).show();
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
            getFragmentManager().beginTransaction().detach(RegisteredCourseTab.this).attach(RegisteredCourseTab.this).commit();
        }
    }

    public void showDialog(final ArrayList<DataModel> registeredCourse,final DataModel deletedCourse,final int position) throws Exception {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to delete this course? If there any other courses have used it as a prerequisite course then it will also be deleted.");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                courseRegHelper.deleteData(deletedCourse.getId());
                adapter.remove(registeredCourse.get(position));
                Toast.makeText(getContext(),R.string.delete_success,Toast.LENGTH_SHORT).show();
                if(confirmDetele(registeredCourse,deletedCourse)){
                    Toast.makeText(getContext(),"Course and also Prerequsite course are deleted successfully.",Toast.LENGTH_LONG).show();
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void addItemsOnSpinner2(View view) {
        List<String> courseList = new ArrayList<String>();
        if (availableCourse != null && availableCourse.size()>0) {
            for (int i = 0; i < availableCourse.size(); i++) {
                String value = availableCourse.get(i).getCourseId()+"(Term "+availableCourse.get(i).getTerm()+")";
                courseList.add(value);
            }
        }
        Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner1);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, courseList);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner1.setAdapter(dataAdapter);
    }

    public  boolean confirmDetele(ArrayList<DataModel> dataModels, DataModel selectedCourse){
        if (dataModels != null && dataModels.size()>0) {
            for (int i = 0; i < dataModels.size(); i++) {
                DataModel dataModel =dataModels.get(i);
                if (dataModels.get(i).getPrerequsite().contains(selectedCourse.getCourseId()) || dataModels.get(i).getPrerequsite().contains("or")) {
                    courseRegHelper.deleteData(dataModels.get(i).getId());
                    dataModels.remove(dataModels.get(i));
                    confirmDetele(dataModels,dataModel);
                    return true;
                }else if(dataModels.get(i).getPrerequsite().contains(selectedCourse.getCourseId()) && dataModels.get(i).getPrerequsite().contains("and")){
                    courseRegHelper.deleteData(dataModels.get(i).getId());
                    dataModels.remove(dataModels.get(i));
                    confirmDetele(dataModels,dataModel);
                    return true;
                }
            }
        }
        return false;
    }

    public  String ifPrerequisite(ArrayList<DataModel> dataModels, DataModel selectedCourse){
        boolean isPre = false;

        if (selectedCourse != null) {
            if (!TextUtils.isEmpty(selectedCourse.getPrerequsite())) {
                if (dataModels != null && dataModels.size() > 0) {
                    boolean a = false;
                    boolean b = false;
                    for (int i = 0; i < dataModels.size(); i++) {
                        if (selectedCourse.getPrerequsite().contains("and")) {
                            if (!a && selectedCourse.getPrerequsite().contains(dataModels.get(i).getCourseId())) {
                                a = true;
                            } else if (selectedCourse.getPrerequsite().contains(dataModels.get(i).getCourseId())) {
                                b = true;
                            }
                            if (a && b) {
                                isPre = true;
                            }
                        } else {
                            if (selectedCourse.getPrerequsite().contains(dataModels.get(i).getCourseId())) {
                                isPre = true;
                            } else if (selectedCourse.getPrerequsite().contains(dataModels.get(i).getCourseId()) && selectedCourse.getPrerequsite().contains("or")) {
                                isPre = true;
                            }
                        }
                    }
                }
            } else {
                isPre = true;
            }
        }
        if (!isPre) {
            return selectedCourse.getPrerequsite();
        } else {
            return "";
        }
    }

    public  boolean isRegistered(ArrayList<DataModel> dataModels, DataModel selectedCourse){
        boolean alreadyRegistered = false;
        if (dataModels != null && dataModels.size()>0) {
            for (int i = 0; i < dataModels.size(); i++) {
                if (dataModels.get(i).getCourseId().equals(selectedCourse.getCourseId())) {
                    alreadyRegistered = true;
                    break;
                }
            }
        }
        return alreadyRegistered;
    }

    public int getPerTermCourses(ArrayList<DataModel> dataModels, DataModel selectedCourse){
        List<String> courseList = new ArrayList<String>();
        if (dataModels != null && dataModels.size()>0) {
            for (int i = 0; i < dataModels.size(); i++) {
                if (dataModels.get(i).getTerm().equalsIgnoreCase(selectedCourse.getTerm())) {
                    courseList.add(dataModels.get(i).getTerm());
                }
            }
        }
        return courseList.size();
    }
}
