package com.example.assignment4;
import java.io.Serializable;

/**
 * Created by Dipak on 08/11/19.
 */
public class DataModel implements Serializable {
    private int id;
    private String courseId;
    private String courseName;
    private String term;
    private String prerequsite;
    private int isRegister;
    private String imageUrl;

    public DataModel(int id, String courseId, String courseName, String term, String prerequsite, int isRegister,String img) {
        this.id = id;
        this.courseId = courseId;
        this.courseName = courseName;
        this.term = term;
        this.prerequsite = prerequsite;
        this.isRegister = isRegister;
        this.imageUrl = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getPrerequsite() {
        return prerequsite;
    }

    public void setPrerequsite(String prerequsite) {
        this.prerequsite = prerequsite;
    }

    public int getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(int isRegister) {
        this.isRegister = isRegister;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
