package com.example.sriram.singleimage2;

/**
 * Created by Sriram on 02-01-2018.
 */

public class userLoginDetails
{
    String userId;
    byte[] userImage;
    userLoginDetails()
    {
       
    }
    public void setUserId(String userId)
    {
        this.userId=userId;
    }
    public String getUserId()
    {
        return  userId;
    }
    public void setUserImage(byte[] userImage)
    {
        this.userImage=userImage;
    }
    public  byte[] getUserImage()
    {
        return userImage;
    }
}
