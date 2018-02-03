package com.example.sriram.singleimage2;

/**
 * Created by Sriram on 09-01-2018.
 */

public class EventPojo {

    private String date,event;


    public EventPojo (String date,String event)
    {
        this.date=date;
        this.event=event;
    }
    public void setDate(String date){
        this.date=date;
    }
    public String getDate(){
        return date;
    }
    public void setEvent(String event){
        this.event=event;
    }
    public String getEvent(){
        return event;
    }
}
