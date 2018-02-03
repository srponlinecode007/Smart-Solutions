package com.example.sriram.singleimage2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ForgotPassword extends AppCompatActivity implements View.OnClickListener{


    String responseData = "";
    EditText etforgetreg;
    Button btnforget;
    String r="",forgetreg="";
    InputStream is;
    String line = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        btnforget = (Button) findViewById(R.id.btnforget);
        etforgetreg = (EditText) findViewById(R.id.forgetreg);

        btnforget.setOnClickListener(this);
    }
    public void onClick(View view){
        forgetreg = etforgetreg.getText().toString().trim();
        FetchDataToDatabase fetchDataToDatabase = new FetchDataToDatabase();
        fetchDataToDatabase.execute(forgetreg);

    }
    class FetchDataToDatabase extends AsyncTask<String, Void, String> {
        String imgInfo;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ForgotPassword.this, "Please Wait...", null, true, true);

        }

        @Override
        protected String doInBackground(String... params) {


            String ServerURL="http://srpsolutions.co.in/getpassword.php";
            String Response="";


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("reg", params[0]));

            try {
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(ServerURL);

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();
                is=httpEntity.getContent();

                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                StringBuilder sb=new StringBuilder();

                while((line=br.readLine())!=null)
                {
                    sb.append(line+"\n");
                }
                Response=sb.toString();
                br.close();
            } catch (ClientProtocolException e)
            {
                Log.e("ClientProtocolException",e.getMessage());
            } catch (IOException e) {
                Log.e("IOException",e.getMessage());
            }
            catch (Exception e)
            {
                Log.e("Main Exception ",e.getMessage());
            }
            return Response;
        }
        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            progressDialog.dismiss();
            if(result.contains("Your Password sent on your Email")){
                Toast.makeText(getApplicationContext(), "Your Password sent on your Email", Toast.LENGTH_SHORT).show();
                etforgetreg.getText().clear();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Invalid Registration No.", Toast.LENGTH_SHORT).show();
            }
    }

    }

}
