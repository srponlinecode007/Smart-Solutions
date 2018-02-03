package com.example.sriram.singleimage2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class StdentListAdapter extends BaseAdapter  {

    Context context;

    List<StudentListPojo> list_student;
    public StdentListAdapter(Context context, List<StudentListPojo> list_student)
    {
        this.context = context;
        this.list_student=list_student;
    }
    private class ViewHolder
    {
        ImageView imagestudent;
        TextView txtfirstname;
        //TextView txtlname;
        TextView txtyear;
        //ImageView imagewhatsapp;
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.listviewstudentdetails, null);
            holder = new ViewHolder();

            holder.txtfirstname = (TextView)convertView.findViewById(R.id.txtfname);
            holder.txtyear=(TextView)convertView.findViewById(R.id.txtyear) ;
            holder.imagestudent=(ImageView)convertView.findViewById(R.id.download);

           /* ImageView imageView = (ImageView) findViewById(R.id.imageView);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.welcome);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            roundedBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(roundedBitmapDrawable);*/


            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        StudentListPojo rowItem = (StudentListPojo) getItem(position);


        holder.txtfirstname.setText(rowItem.getFname());
        //holder.txtlname.setText(rowItem.getSubname());
        holder.txtyear.setText(String.valueOf(rowItem.getyear()));
        holder.imagestudent.setImageBitmap(rowItem.getStudentimage());

        return convertView;
    }


    @Override
    public int getCount() {
        return list_student.size();
    }

    @Override
    public Object getItem(int position) {
        return list_student.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list_student.indexOf(getItem(position));
    }
}
