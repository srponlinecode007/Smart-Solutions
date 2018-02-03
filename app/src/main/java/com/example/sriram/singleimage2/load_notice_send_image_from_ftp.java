package com.example.sriram.singleimage2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Sriram on 15-01-2018.
 */

public class load_notice_send_image_from_ftp implements View.OnClickListener
{
    int p=1;
    static int visit=1;
    ArrayList<Bitmap> arrBitImage;
    String Loadimage = "";
    ImageView dialogImageContent;
    ArrayList list;
    Button btnNext,btnPrev,btnDown;
    private ProgressDialog dialog;
    String ImageName;

    public Context context;




    public void loadData(View view, final String ImageLoadDetails)
    {
        ImageName=ImageLoadDetails;
context=view.getContext();



        final Dialog openDialog = new Dialog(view.getContext());
        openDialog.setContentView(R.layout.custom_dialog_show_uploaded_images);
        openDialog.setTitle("Custom Dialog Box");
        dialogImageContent = (ImageView) openDialog.findViewById(R.id.customDialogImage);
        Button dialogCloseButton = (Button) openDialog.findViewById(R.id.customDialogButtonOk);
        btnNext=(Button)openDialog.findViewById(R.id.customDialogButtonNext);
        btnPrev=(Button)openDialog.findViewById(R.id.customDialogPre);
        btnDown=(Button)openDialog.findViewById(R.id.customDialogButtonDwn);

        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnDown.setOnClickListener(this);
        list = new ArrayList();
        String strImagename = ImageLoadDetails;
        String temp = strImagename.substring(0, strImagename.indexOf(","));
        String[] imgno = strImagename.split(",");
        for (int i = 1; i < imgno.length; i++) {
            list.add(temp + "_" + imgno[i]);
        }

        loaddatafromftpasync load = new loaddatafromftpasync();
        load.execute();

        dialogCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Custom Dialog", ImageLoadDetails);
                openDialog.dismiss();
            }
        });
        openDialog.show();
    }



    @Override
    public void onClick(View view)
    {
        int t;
        if(view.getId()==R.id.customDialogPre)
        {
            if(visit==-1)
                btnNext.setVisibility(View.VISIBLE);
            else
            {
                for (t=visit;t>=0;t--) {
                    dialogImageContent.setImageBitmap(arrBitImage.get(t));

                }
                //if(t==-1)
                   // btnPrev.setVisibility(View.INVISIBLE);
            }
        }
        if(view.getId()==R.id.customDialogButtonNext)
        {
            for(int i=0;i<arrBitImage.size();i++)
            {
                if(i==arrBitImage.size()-1)
                    btnNext.setVisibility(View.VISIBLE);
                else
                {
                    dialogImageContent.setImageBitmap(arrBitImage.get(i + 1));
                    visit=i;
                }
            }
        }
        if(view.getId()==R.id.customDialogButtonDwn)
        {

            dialog = new ProgressDialog(context);
            dialog.setTitle("Status");
            dialog.setMessage("Downloading...");
            dialog.setCancelable(false);

            dialog.show();

            try
            {
                String strImagename = ImageName;
                String temp = strImagename.substring(0, strImagename.indexOf(","));
                File fileinfo = new File(Environment.getExternalStorageDirectory(), strImagename+"_"+String.valueOf(p)+".png");
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileinfo));

                Bitmap img=arrBitImage.get(visit);
                img.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                p++;
                //Toast.makeText(context,"Image Downloaded...!!!",Toast.LENGTH_LONG).show();


                /*for(int i=0;i<fileinfo.length();i++)
                {

                    if(filesList[i].getName().equals(dwnFile))
                    {
                        resOut=true;
                        f=filesList[i].getName();
                        resOut=ftpclient.retrieveFile(filesList[i].getName(),outputStream);
                        break;
                    }
                }*/


            }
            catch (Exception e)
            {
                Log.e("Exception ",e.getMessage());
            }

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    class loaddatafromftpasync extends AsyncTask<String, Void, String>
    {

        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        public String doInBackground(String... arg)
        {
            String msg = "";
            FTPClient ftpclient = new FTPClient();
            String ftpServerAddress = "ftp.srpsolutions.co.in";
            String userName = "shriramftp";
            String password = "Srijan!123";

            boolean result = true;
            try
            {
                ftpclient.connect(ftpServerAddress);
                result = ftpclient.login(userName, password);
                ftpclient.changeWorkingDirectory("/httpdocs/NoticeImage/");
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
                ByteArrayOutputStream stream = null;
                arrBitImage = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {

                    stream = new ByteArrayOutputStream();
                    ftpclient.retrieveFile(list.get(i).toString()+".png", stream);
                    byte[] tempimage = stream.toByteArray();
                    Log.e("Image Name",list.get(i).toString());
                    arrBitImage.add(BitmapFactory.decodeByteArray(tempimage, 0, tempimage.length));
                }

                stream.close();

                ftpclient.logout();
            }
            catch (FTPConnectionClosedException e)
            {

                msg = e.getMessage();
            }
            catch (IOException e)
            {
                msg = e.getMessage();
            }
            finally
            {
                try {
                    ftpclient.disconnect();
                } catch (FTPConnectionClosedException e) {
                    msg = "FTP Close : " + e.getMessage();
                } catch (IOException e) {
                    msg = "FTP IO : " + e.getMessage();
                }
            }
            return msg;
        }
        @Override
        public void onPostExecute(String result)
        {
            super.onPostExecute(result);
            dialogImageContent.setImageBitmap(arrBitImage.get(0));

        }
    }
}