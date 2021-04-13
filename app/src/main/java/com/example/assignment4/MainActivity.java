package com.example.assignment4;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;


import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity{
    private CourseRegHelper courseRegHelper;
    private PageAdapter pageAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    String response;
    private static ArrayList<DataModel> dataModels;
    private static ArrayList<DataModel> preRegisterCourse;
    private static RecyclerView recyclerView;
    private static RecycleViewAdapter recycleViewAdapter;
    AsyncHttpClient asyncHttpClient;
    String courseGetUrl ="http://35.203.95.76:8809/course-details/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_main);
            courseRegHelper = new CourseRegHelper(this);
            asyncHttpClient = new AsyncHttpClient();
            dataModels = new ArrayList<>();
            preRegisterCourse = new ArrayList<>();
            tabLayout = (TabLayout)findViewById(R.id.tabLayout);
            tabLayout.addTab(tabLayout.newTab().setText("Available Course"));
            tabLayout.addTab(tabLayout.newTab().setText("Registered Course"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            viewPager = (ViewPager)findViewById(R.id.viewPager);
            pageAdapter = new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
            viewPager.setAdapter(pageAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            asyncHttpClient.get(courseGetUrl, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        for (int i=0;i<response.length();i++){
                            JSONObject course = (JSONObject)response.getJSONObject(i);
                            int term =1;
                            if (course.getString("term").contains("2nd")){
                                term =2;
                            }
                            String preRequisite ="";
                            String condition ="";
                            if(course.getString("condition").equals("1")){
                                condition =" or ";
                            }else if (course.getString("condition").equals("2")){
                                condition =" and ";
                            }
                            if(!TextUtils.isEmpty(course.getString("prerequitiesOne"))){
                                preRequisite = preRequisite+course.getString("prerequitiesOne").toUpperCase();
                            }
                            if(!TextUtils.isEmpty(course.getString("prerequitiesTwo"))){
                                preRequisite = preRequisite+condition+course.getString("prerequitiesTwo").toUpperCase();
                            }
                            int isReg =0;
                            if (course.getBoolean("seletionStatus")){
                                isReg = 1;
                            }
                            if (course.getString("courseNumber").toUpperCase().equals("CS161") || course.getString("courseNumber").toUpperCase().equals("CS162") || course.getString("courseNumber").toUpperCase().equals("CS255") || course.getString("courseNumber").toUpperCase().equals("CS263")){
                                preRegisterCourse.add(new DataModel(i,course.getString("courseNumber"), course.getString("coursename"), String.valueOf(term), preRequisite,isReg,course.getString("imageUrl")));
                            }
                            dataModels.add(new DataModel(i,course.getString("courseNumber"), course.getString("coursename"), String.valueOf(term), preRequisite,isReg,course.getString("imageUrl")));
                            courseRegHelper.insertData(i,course.getString("courseNumber"), course.getString("coursename"), term, preRequisite,isReg,course.getString("imageUrl"));
                        }

                        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
                        recycleViewAdapter = new RecycleViewAdapter(MainActivity.this,dataModels);
                        recyclerView.setAdapter(recycleViewAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        System.out.println(preRegisterCourse.size());
                        for (int m=0;m<preRegisterCourse.size();m++){
                            int courseId= dataModels.size()+m+1;
                            courseRegHelper.insertData(courseId,preRegisterCourse.get(m).getCourseId(), preRegisterCourse.get(m).getCourseName(), Integer.parseInt(preRegisterCourse.get(m).getTerm()), preRegisterCourse.get(m).getPrerequsite(),1,preRegisterCourse.get(m).getImageUrl());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    Toast.makeText(MainActivity.this,"Failed to retrieve data from server.",Toast.LENGTH_LONG).show();
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject response) {
                    super.onFailure(statusCode, headers,throwable,response);
                    Toast.makeText(MainActivity.this,"Failed to retrieve data from server.",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onRetry(int retryNo) {
                    System.out.println("retryNo::"+retryNo);
                }
            });

            System.out.println(dataModels.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
