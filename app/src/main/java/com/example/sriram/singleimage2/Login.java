package com.example.sriram.singleimage2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class Login extends AppCompatActivity implements View.OnClickListener
{

        Button btnRegister,btnLogin;
    userLoginDetails loginDetails;
        EditText etuname ;
        EditText etpassword ;
        TextView textView ;
        InputStream is;
        ImageView imageView;
    Notice_Details_Pojo noticeDetailsPojo;
        String line = null;
        int n=0;
    public String uname="",pwd="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(!isNetworkAvailable()){
            Toast.makeText(Login.this, "Internet Connection is OFF", Toast.LENGTH_LONG).show();
        }


        etuname = (EditText) findViewById(R.id.uname);
        etpassword = (EditText) findViewById(R.id.pass);
        textView = (TextView) findViewById(R.id.forgotpass);

        imageView=(ImageView)findViewById(R.id.ivImage);
         loginDetails=new userLoginDetails();

            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.welcome);
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            roundedBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(roundedBitmapDrawable);

            btnRegister=(Button)findViewById(R.id.createaccountbutton);
            btnLogin=(Button)findViewById(R.id.signbutton);

            btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        textView.setOnClickListener(this);




    }
   /* public class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, final Intent intent) {

            String status = NetworkUtil.getConnectivityStatusString(context);

            Toast.makeText(context, status, Toast.LENGTH_LONG).show();
        }
    }*/
    private boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.createaccountbutton:
                startActivity(new Intent(this,Registration.class));
                break;
            case R.id.signbutton:
                 uname = etuname.getText().toString().trim();
                 pwd = etpassword.getText().toString().trim();
                retriveRegNo.RegNo=uname;
                retriveRegNo.pass=pwd;
                FetchDataToDatabase fetchDataToDatabase = new FetchDataToDatabase();
                fetchDataToDatabase.execute(uname, pwd);

                break;
            case R.id.forgotpass:
                startActivity(new Intent(this,ForgotPassword.class));
        }

    }

    class FetchDataToDatabase extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Login.this, "Please Wait Loading Data", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {

            String responseStatus=null;

            String ServerURL="http://srpsolutions.co.in/login.php";
            String Response="";

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("uname", params[0]));
            nameValuePairs.add(new BasicNameValuePair("password", params[1]));

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

                JSONArray jsonArray = new JSONArray(Response);
                for (int i = 0; i< jsonArray.length(); i++)
                {
                    JSONObject json_data = jsonArray.getJSONObject(i);
                    if (json_data.getString("status").contains("success"))
                    {
                        responseStatus="success";
                        noticeDetailsPojo = new Notice_Details_Pojo(json_data.getString("fname"), json_data.getString("lname"), json_data.getInt("curryear"), json_data.getString("pwd"), (json_data.getString("regno")));
                    }
                    else
                    {
                        responseStatus="failed";
                    }
                }


            }
            catch (ClientProtocolException e)
            {
                Log.e("ClientProtocolException",e.getMessage());
            } catch (IOException e) {
                Log.e("IOException",e.getMessage());
            }
            catch(Exception e)
            {
                Log.e("Json Exception ",e.getMessage());
            }
            return responseStatus;
        }


        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            progressDialog.dismiss();
            if(result.contains("success"))
            {
                startActivity(new Intent(getApplicationContext(),activityHome.class));
                etuname.getText().clear();
                etpassword.getText().clear();
            }
            else
            {
                Toast.makeText(getApplicationContext(), " Invalid User", Toast.LENGTH_SHORT).show();
                etuname.getText().clear();
                etpassword.getText().clear();
            }

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==1 && resultCode==RESULT_OK)
        {


                 Toast.makeText(this,data.getStringExtra("userName"),Toast.LENGTH_LONG).show();
                Log.e("User Name",data.getStringExtra("userName"));
                etuname.setText("asasas");

        }
    }
}
