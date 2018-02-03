package com.example.sriram.singleimage2;

/**
 * Created by Sriram on 13-01-2018.
 */

public class NoticePojo {


    private  String regno,name,imageid,imagename,datetime,Msg,ImageId;
    private  int curryear,isImage;
    public NoticePojo (String name,int curryear,String regno,String imageid,String imagename,String datetime,String Msg,int isImage,String ImageId)
    {
        this.name=name;
        this.curryear=curryear;
        this.regno=regno;
        this.imageid= imageid;
        this.imagename= imagename;
        this.datetime= datetime;
        this.Msg=Msg;
        this.isImage=isImage;
        this.ImageId=ImageId;
    }
    public void setImageId(String ImageId){this.ImageId=ImageId;}
    public String getImageId(){return ImageId;}
    public void setIsImage(int isImage){this.isImage=isImage;}
    public int getIsImage(){return isImage;}
    public  void setName(String namee)
    {
        name=namee;
    }
    public  String getName()
    {
        return name;
    }
    public  void setImageid(String imageidd)
    {
        imageid=imageidd;
    }
    public  String getImageid()
    {
        return imageid;
    }
    public  void setRegno(String regnoo)
    {
        regno=regnoo;
    }
    public  String getRegno()
    {
        return regno;
    }
    public  void setCurryear(int curryearr)
    {
        curryear=curryearr;
    }
    public  int getCurryear()
    {
        return curryear;
    }
    public  void setImagename(String imagenamee)
    {
        imagename=imagenamee;
    }
    public  String getImagename()
    {
        return imagename;
    }
    public  void setDatetime(String datetimee)
    {
        datetime=datetimee;
    }
    public  String getDatetime()
    {
        return datetime;
    }
    public void setMsg(String Msg)
    {
        this.Msg=Msg;
    }
    public String getMsg()
    {
        return Msg;
    }
}
