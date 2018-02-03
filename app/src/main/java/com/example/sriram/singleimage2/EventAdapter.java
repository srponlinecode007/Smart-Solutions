package com.example.sriram.singleimage2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends BaseAdapter {

    Context context;

    List<EventPojo> list_event;
    public ArrayList<list_pre_year_paper> preArrayList;
    public ArrayList<list_pre_year_paper> orig;
    public EventAdapter(Context context, List<EventPojo> list_event)
    {
        this.context = context;
        this.list_event=list_event;
    }
    private class ViewHolder
    {
        ImageView imagestudent;
        TextView txtevent;
        //TextView txtlname;
        TextView txtdate;
        //ImageView imagewhatsapp;
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.eventlist, null);
            holder = new ViewHolder();

            holder.txtdate=(TextView)convertView.findViewById(R.id.txtdate) ;
            holder.txtevent = (TextView)convertView.findViewById(R.id.txtevent);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        EventPojo rowItem = (EventPojo) getItem(position);

        holder.txtdate.setText(rowItem.getDate());
        holder.txtevent.setText(rowItem.getEvent());

        return convertView;
    }


    @Override
    public int getCount() {
        return list_event.size();
    }

    @Override
    public Object getItem(int position) {
        return list_event.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list_event.indexOf(getItem(position));
    }

}
