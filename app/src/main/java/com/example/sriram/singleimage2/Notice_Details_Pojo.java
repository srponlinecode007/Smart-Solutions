package com.example.sriram.singleimage2;

/**
 * Created by Sriram on 12-01-2018.
 */

public class Notice_Details_Pojo {

    private static String regno,fname,lname,pwd;
    private static int curryear;
    public Notice_Details_Pojo (String fname,String lname,int curryear,String pwd,String regno)
    {
        this.fname=fname;
        this.curryear=curryear;
        this.lname=lname;
        this.pwd=pwd;
        this.regno=regno;
    }
    public static void setFname(String fnamee)
    {
        fname=fnamee;
    }
    public static String getFname()
    {
        return fname;
    }
    public static void setLname(String lnamee)
    {
       lname=lnamee;
    }
    public static String getLname()
    {
        return lname;
    }
    public static void setPwd(String pwdd)
    {
        pwd=pwdd;
    }
    public static String getPwd()
    {
        return pwd;
    }
    public static void setRegno(String regnoo)
    {
        regno=regnoo;
    }
    public static String getRegno()
    {
        return regno;
    }
    public static void setCurryear(int curryearr)
    {
        curryear=curryearr;
    }
    public static int getCurryear()
    {
        return curryear;
    }
}
