package com.example.assignment4;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Dipak on 08/11/19.
 */

public class CourseRegHelper extends SQLiteOpenHelper {
    private ArrayList<DataModel> dataModels;
    private DataModel dataModel;
    private final static String DB_NAME = "course_reg.db";
    private final static String TABLE_NAME_COURSE = "course";
    private final static String ID = "id";
    private final static String COURSE_ID = "course_id";
    private final static String COURSE_NAME = "course_name";
    private final static String COURSE_TERM = "term";
    private final static String PREREQUSITE = "prerequsite";
    private final static String IS_REGISTERED = "is_register";
    private final static String IMG_URL = "img_url";

    public CourseRegHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 7);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String createtable = "create table  " + TABLE_NAME_COURSE+
                    "(" + ID + " integer primary key, " +
                    COURSE_ID + " text, " +
                    COURSE_NAME + " text, " +
                    COURSE_TERM + " integer, " +
                    PREREQUSITE + " text, " +
                    IS_REGISTERED + " integer default 0, "+
                    IMG_URL + " text)";
            db.execSQL(createtable);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_COURSE);
            onCreate(db);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void insertData(int id,String courseId, String courseName, int term, String prerequsite, int isReg,String img){
        try {
            if(!isCourseExist(id)) {
                SQLiteDatabase db = this.getWritableDatabase();
                String insertq = "INSERT INTO " + TABLE_NAME_COURSE + " (id,course_id, course_name, term,prerequsite,is_register,img_url)" + " VALUES " +
                        " (" + id + ",'" + courseId + "','" + courseName + "', " + term + ", '" + prerequsite + "', " + isReg + ", '" + img + "')";
                db.execSQL(insertq);
                db.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void deleteData(int id){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM " + TABLE_NAME_COURSE + " where id =?";
            String[] whereArgs = new String[]{String.valueOf(id)};
            Cursor result = db.rawQuery(query, whereArgs);
            result.moveToFirst();
            result.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public boolean isCourseExist(int id) {
        boolean isExist = false;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME_COURSE + " where id = ?";
            String[] whereArgs = new String[]{String.valueOf(id)};
            Cursor result = db.rawQuery(query, whereArgs);
            if(result.moveToFirst()){
                isExist = true;
            }
            result.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return isExist;
    }

    public DataModel getCourseDetails(String id) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME_COURSE + " where course_id = ?";
            String[] whereArgs = new String[]{id};
            Cursor result = db.rawQuery(query, whereArgs);
            result.moveToFirst();
            do{
                dataModel = new DataModel(result.getInt(0),result.getString(1), result.getString(2), String.valueOf(result.getInt(3)), result.getString(4),result.getInt(5),result.getString(6));
            }while (result.moveToNext());
            result.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return dataModel;
    }

    public ArrayList<DataModel> getAvailableCourses(){
        dataModels= new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME_COURSE + " where is_register = 0";
            Cursor result = db.rawQuery(query, null);
            result.moveToFirst();
            if (result!=null && result.getCount()>0) {
                do {
                    dataModels.add(new DataModel(result.getInt(0), result.getString(1), result.getString(2), String.valueOf(result.getInt(3)), result.getString(4),result.getInt(5),result.getString(6)));
                } while (result.moveToNext());
            }
            result.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return dataModels;
    }

    public ArrayList<DataModel> getRegisteredCourses(){
        dataModels= new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME_COURSE + " where is_register = 1";
            Cursor result = db.rawQuery(query, null);
            result.moveToFirst();
            if(result!=null && result.getCount()>0) {
                do {
                    dataModels.add(new DataModel(result.getInt(0), result.getString(1), result.getString(2), String.valueOf(result.getInt(3)), result.getString(4),result.getInt(5),result.getString(6)));
                } while (result.moveToNext());
            }
            result.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return dataModels;
    }

    public ArrayList<DataModel> getAllCourses(){
        dataModels= new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_NAME_COURSE;
            Cursor result = db.rawQuery(query, null);
            result.moveToFirst();
            do{
                dataModels.add(new DataModel(result.getInt(0),result.getString(1), result.getString(2), String.valueOf(result.getInt(3)), result.getString(4),result.getInt(5),result.getString(6)));
            }while (result.moveToNext());
            result.close();
            db.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return dataModels;
    }

}
