package com.example.sriram.singleimage2;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



public class Changepassword extends Fragment implements View.OnClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    EditText opass,npass,cpass;
    String npwd="",opwd="",cpwd="",reg="",password="";
    InputStream is;
    Button btnchange;
    String line = null;

    private OnFragmentInteractionListener mListener;

    public Changepassword()
    {
        // Required empty public constructor
    }

    public static Changepassword newInstance(String param1, String param2)
    {
        Changepassword fragment = new Changepassword();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {

        View view =inflater.inflate(R.layout.fragment_changepassword, container, false);
        opass = (EditText) view.findViewById(R.id.oldpass);
        npass = (EditText) view.findViewById(R.id.newpass);
        cpass = (EditText) view.findViewById(R.id.confirmpass);
        btnchange = (Button) view.findViewById(R.id.btnchangepass);
        btnchange.setOnClickListener(this);
        reg=retriveRegNo.RegNo;
        password=retriveRegNo.pass;
        return view;
    }

    public void onClick(View view)
    {
        opwd = opass.getText().toString().trim();
        npwd = npass.getText().toString().trim();
        cpwd = cpass.getText().toString().trim();
        FetchDataToDatabase fetchDataToDatabase = new FetchDataToDatabase();
        if(opwd.isEmpty() || npwd.isEmpty() || cpwd.isEmpty()){
            Toast.makeText(getContext(), "you can't leave blank any box", Toast.LENGTH_LONG).show();
        }
        else if(!(opwd.equals(password)))
        {
            Toast.makeText(getContext(), "Please fill old password properly", Toast.LENGTH_LONG).show();
        }
        else
        {
            fetchDataToDatabase.execute(reg, opwd, npwd, cpwd);
        }

    }

    class FetchDataToDatabase extends AsyncTask<String, Void, String> {
        String imgInfo;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getContext(), "Please Wait...", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {


            String ServerURL = "http://srpsolutions.co.in/update.php";
            String Response = "";


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("reg", params[0]));
            nameValuePairs.add(new BasicNameValuePair("opwd", params[1]));
            nameValuePairs.add(new BasicNameValuePair("npwd", params[2]));
            nameValuePairs.add(new BasicNameValuePair("cpwd", params[3]));

            try
            {
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(ServerURL);

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                Response = sb.toString();
                br.close();
            } catch (ClientProtocolException e) {
                Log.e("ClientProtocolException", e.getMessage());
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            } catch (Exception e) {
                Log.e("Main Exception ", e.getMessage());
            }
            return Response;
        }

        @Override
        protected void onPostExecute(String result)
        {

            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result.contains("Not Updated")) {
                Toast.makeText(getContext(), "Password Not Changed", Toast.LENGTH_LONG).show();

                opass.getText().clear();
                npass.getText().clear();
                cpass.getText().clear();

            } else if (result.contains("Updated Successfully")) {
                Toast.makeText(getContext(), "Password Changed Successfully", Toast.LENGTH_LONG).show();
                opass.getText().clear();
                npass.getText().clear();
                cpass.getText().clear();
            } else if (result.contains("Password and Confirm Password are not matched")) {
                Toast.makeText(getContext(), "New Password and Confirm Password are not matched", Toast.LENGTH_LONG).show();
                opass.getText().clear();
                npass.getText().clear();
                cpass.getText().clear();
            } else {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                opass.getText().clear();
                npass.getText().clear();
                cpass.getText().clear();
            }
        }
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
