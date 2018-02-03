package com.example.sriram.singleimage2;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sriram on 03-01-2018.
 */

public class list_pre_paper_adapter extends BaseAdapter implements Filterable {


    Context context;
    private ArrayList<list_pre_year_paper> filteredData = null;
    ArrayList<list_pre_year_paper> lisprePapaer = null;
    ValueFilter valueFilter=null;
    String filePath=null;
    boolean resOut=false;


    public list_pre_paper_adapter(Context context, ArrayList<list_pre_year_paper> lisprePapaer) {
        this.context = context;
        this.lisprePapaer = lisprePapaer;
        filteredData=lisprePapaer;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtSubName;
        TextView txtSem;
        TextView txtSemType;
        TextView txtYear;
        ImageView txtqid;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listview_pre_year_papers, null);
            holder = new ViewHolder();

            holder.txtSubName = (TextView) convertView.findViewById(R.id.txtSName);
            holder.txtSem = (TextView) convertView.findViewById(R.id.txtSemester);
            holder.txtSemType = (TextView) convertView.findViewById(R.id.txtSemtype);
            holder.txtYear = (TextView) convertView.findViewById(R.id.txtyear);
            holder.txtqid = (ImageView) convertView.findViewById(R.id.download);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        list_pre_year_paper rowItem = (list_pre_year_paper) getItem(position);


        holder.txtSubName.setText(rowItem.getSubname());
        holder.txtSem.setText(rowItem.getSem());
        holder.txtSemType.setText(rowItem.getSemtype());
        holder.txtYear.setText(rowItem.getYear());
       // filePath =holder.txtqid.getTag().toString();
        holder.txtqid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, lisprePapaer.get(position).getQid(), Toast.LENGTH_SHORT).show();
                    StartDownload strtdwnload = new StartDownload();
                    strtdwnload.execute(lisprePapaer.get(position).getQid());

                //Toast.makeText(context,filePath,Toast.LENGTH_LONG).show();

                //Log.e("Data " , filePath);
            }

        });
        return convertView;
    }

    class StartDownload extends AsyncTask<String,Void,String>
    {

        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(context);
            dialog.setTitle("Status");
            dialog.setMessage("Downloading...");
            dialog.setCancelable(false);

            dialog.show();

        }

        @Override
        public String doInBackground(String...param)
        {
            String f=null;

            String msg = null;
            FTPClient ftpclient = new FTPClient();

            boolean result;
            String ftpServerAddress = "ftp.srpsolutions.co.in";
            String userName = "shriramftp";
            String password = "Srijan!123";


            try
            {
                ftpclient.connect(ftpServerAddress);
                result = ftpclient.login(userName, password);
                ftpclient.changeWorkingDirectory("/httpdocs/OtherImage/");
                ftpclient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpclient.enterLocalPassiveMode();
                if (result == true)
                {
                    msg = "Logged in Successfully !";
                }
                else
                {
                    msg = "Login Fail!";
                }
                final String dwnFile=param[0]+".pdf";
                File fileinfo=new File(Environment.getExternalStorageDirectory(),dwnFile);
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileinfo));


                FTPFile[] filesList = ftpclient.listFiles();
                for(int i=0;i<filesList.length;i++)
                {

                    if(filesList[i].getName().equals(dwnFile))
                    {
                        resOut=true;
                        f=filesList[i].getName();
                        resOut=ftpclient.retrieveFile(filesList[i].getName(),outputStream);
                        break;
                    }
                }
                if(resOut==true)
                    msg=msg+"Downloaded";
                else
                    msg=msg+"Not Downloaded";
                outputStream.close();
                ftpclient.logout();
            } catch (FTPConnectionClosedException e) {

                msg = e.getMessage();
            } catch (IOException e) {
                msg = e.getMessage();
            } finally {
                try {
                    ftpclient.disconnect();
                } catch (FTPConnectionClosedException e) {
                    msg = "FTP Close : " + e.getMessage();
                } catch (IOException e) {
                    msg = "FTP IO : " + e.getMessage();
                }
            }
            return "Downloading Complete"+msg+" "+f;
        }
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            Toast.makeText(context,result,Toast.LENGTH_LONG).show();
            Log.e("Daaya " ,result);
        }
    }
    @Override
    public int getCount() {
        return lisprePapaer.size();
    }

    @Override
    public Object getItem(int position) {
        return lisprePapaer.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lisprePapaer.indexOf(getItem(position));
    }
    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }
    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<list_pre_year_paper> filterList = new ArrayList<list_pre_year_paper>();
                for (int i = 0; i < filteredData.size(); i++) {
                    if ((filteredData.get(i).getSubname().toUpperCase()).contains(constraint.toString().toUpperCase())) {

                        list_pre_year_paper country = new list_pre_year_paper(filteredData.get(i).getSubname(), filteredData.get(i).getSem(), filteredData.get(i).getSemtype(), filteredData.get(i).getYear(), filteredData.get(i).getQid());

                        filterList.add(country);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = filteredData.size();
                results.values = filteredData;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            lisprePapaer = (ArrayList<list_pre_year_paper>) results.values;
            notifyDataSetChanged();
        }
    }
}




