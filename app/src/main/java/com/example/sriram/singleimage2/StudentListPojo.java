package com.example.sriram.singleimage2;

import android.graphics.Bitmap;

/**
 * Created by Sriram on 05-01-2018.
 */

public class StudentListPojo {

    private String fname, whatsappimage,regno;
    private Bitmap studentimage;
    private int year;
    public StudentListPojo (String fname,int year,String regno,Bitmap studentimage)
    {
        this.studentimage=studentimage;
        this.fname=fname;
        this.year=year;
        this.regno=regno;
    }
    public void setStudentimage(Bitmap studentimage)
    {
        this.studentimage=studentimage;
    }
    public Bitmap getStudentimage()
    {
        return studentimage;
    }
    public void setFname(String fname)
    {
        this.fname=fname;
    }
    public String getFname()
    {
        return fname;
    }
    public void setyear(int year)
    {
        this.year=year;
    }
    public int getyear()
    {
        return year;
    }
    public void setRegno(String regno)
    {
        this.regno=regno;
    }
    public String getRegno()
    {
        return  regno;
    }
}
