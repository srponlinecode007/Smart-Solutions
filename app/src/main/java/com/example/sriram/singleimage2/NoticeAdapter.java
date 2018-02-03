package com.example.sriram.singleimage2;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sriram on 13-01-2018.
 */

public class NoticeAdapter extends BaseAdapter {


    Context context;
    NoticeAdapter.ViewHolder holder = null;
    ArrayList<NoticePojo> list_notice;
    HashMap<String,String> imageListName;
    public NoticeAdapter(Context context, ArrayList<NoticePojo> list_notice, HashMap<String,String>imageListName)
    {
        this.context = context;
        this.list_notice=list_notice;
        this.imageListName=imageListName;
    }
    private class ViewHolder
    {

        TextView txtname;
        TextView txtyear;
        TextView txtdatetime;
        TextView txtMsg;
        TextView txtMore;
        int isImage;
        String ImageId;

    }
    public View getView(int position, View convertView, ViewGroup parent)
    {


        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.listviewnoticedetails, null);
            holder = new NoticeAdapter.ViewHolder();


            holder.txtname = (TextView)convertView.findViewById(R.id.txtfname);
            holder.txtyear = (TextView)convertView.findViewById(R.id.txtyear);
            holder.txtdatetime = (TextView)convertView.findViewById(R.id.txtdatetime);
            holder.txtMsg=(TextView)convertView.findViewById(R.id.txtMsg);
            holder.txtMore=(TextView)convertView.findViewById(R.id.txtviewmore);
            holder.txtMore.setVisibility(View.INVISIBLE);
            convertView.setTag(holder);
        }
        else
        {
            holder = (NoticeAdapter.ViewHolder) convertView.getTag();
        }

        NoticePojo rowItem = (NoticePojo) getItem(position);

       // int Year=Notice_Details_Pojo.getCurryear();
        //String [] imgno=holder.ImageId.split("_");
        //System.out.println(imgno[3]);
        //String sYeear=String.valueOf(Year);
        //if(imgno[3].contains(sYeear)) {

            holder.txtname.setText(rowItem.getName());
            holder.txtyear.setText(String.valueOf(rowItem.getCurryear()));
            holder.txtdatetime.setText(rowItem.getDatetime());
            holder.txtMsg.setText(rowItem.getMsg());

            holder.isImage = rowItem.getIsImage();
            holder.ImageId = rowItem.getImageId();
            final String tempImageId = holder.ImageId;
            if (holder.isImage == 1) {
                holder.txtMore.setVisibility(View.VISIBLE);
            }
        else {
                holder.txtMore.setVisibility(View.INVISIBLE);
            }

            holder.txtMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    load_notice_send_image_from_ftp loadNoticeSendImageFromFtp = new load_notice_send_image_from_ftp();
                    loadNoticeSendImageFromFtp.loadData(view, imageListName.get(tempImageId));
                }
            });
       /* }
        else
        {
            Toast.makeText(context,"No Data for this Year's Student",Toast.LENGTH_LONG).show();
        }*/
        return convertView;
    }


    @Override
    public int getCount() {
        return list_notice.size();
    }

    @Override
    public Object getItem(int position) {
        return list_notice.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list_notice.indexOf(getItem(position));
    }

}
