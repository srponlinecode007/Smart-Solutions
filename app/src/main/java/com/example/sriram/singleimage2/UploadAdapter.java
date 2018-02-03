package com.example.sriram.singleimage2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sriram on 10-01-2018.
 */

public class UploadAdapter extends BaseAdapter{

    Context context;


    ArrayList<Bitmap> list_image;
    public UploadAdapter(Context context, ArrayList<Bitmap> list_image)
    {
        this.context = context;
        this.list_image=list_image;
    }
    private class ViewHolder
    {

       ImageView img;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.gridiewupload, null);
            holder = new ViewHolder();

            holder.img = (ImageView) convertView.findViewById(R.id.imageUploadView);


            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        //decodeUri(list_image);

        //UploadPojo rowItem = (UploadPojo) getItem(position);


        holder.img.setImageBitmap(list_image.get(position));
        //holder.txtfirstname.setText(rowItem.getFname());

        return convertView;
    }


    @Override
    public int getCount() {
        return list_image.size();
    }

    @Override
    public Object getItem(int position) {
        return list_image.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list_image.indexOf(getItem(position));
    }


}
