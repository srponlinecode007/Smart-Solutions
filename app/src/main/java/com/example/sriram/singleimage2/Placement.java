package com.example.sriram.singleimage2;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPConnectionClosedException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;


public class Placement extends Fragment
{
    int totalStudent=1;
    ArrayList<StudentListPojo> studentListPojos;
    StdentListAdapter adapter;
    ListView listView;
    TextView studentno;
    ArrayList<Bitmap>arrBitImage;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Placement() {
        // Required empty public constructor
    }
    public static Placement newInstance(String param1, String param2) {
        Placement fragment = new Placement();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    Context context;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_placement, container, false);
        Initialize(view);
        context=getContext();
        studentno = (TextView)view.findViewById(R.id.txtstudentno) ;
        FetchDataToDatabase fetchDataToDatabase = new FetchDataToDatabase();
        fetchDataToDatabase.execute();
        return view;
    }

    class FetchDataToDatabase extends AsyncTask<String, Void, ArrayList<StudentListPojo>> {


        ProgressDialog progressDialog;
        String result = null;
        InputStream is = null;

        protected void onPreExecute() {
            super.onPreExecute();

           progressDialog = ProgressDialog.show(getContext(), "Please Wait Loading Data", null, true, true);
        }

        @Override
        public ArrayList<StudentListPojo> doInBackground(String... param) {
            try {
                studentListPojos = new ArrayList<>();

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://srpsolutions.co.in/showstudent.php");
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                    //  Toast.makeText(getApplicationContext(), "Input Reading pass", Toast.LENGTH_SHORT).show();
                }
                is.close();

                loadImageFromFTP();

                result = sb.toString();
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i< jsonArray.length(); i++)
                {
                    JSONObject json_data = jsonArray.getJSONObject(i );
                    studentListPojos.add(new StudentListPojo(json_data.getString("fname")+" "+json_data.getString("lname"), json_data.getInt("curryear"),json_data.getString("regno"),arrBitImage.get(i)));
                    totalStudent++;

                    Log.e("Image ",String.valueOf(arrBitImage.get(i)));
                }
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
                Toast.makeText(getContext(), " Input reading fail", Toast.LENGTH_SHORT).show();

            }
            return studentListPojos;
        }



        @Override
        protected void onPostExecute(ArrayList<StudentListPojo> result) {

            super.onPostExecute(result);
            progressDialog.dismiss();
           /* for (int i=0;i<result.size();i++) {
                Log.e("Data ", String.valueOf(result.get(i).getyear()));
            }*/
            adapter=new StdentListAdapter(getContext(),result);
            listView.setAdapter(adapter);
            studentno.setText(String.valueOf(--totalStudent));

            // Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
        }

        public void loadImageFromFTP()
        {
            String msg = "";
            FTPClient ftpclient = new FTPClient();
            String ftpServerAddress = "ftp.srpsolutions.co.in";
            String userName = "shriramftp";
            String password = "Srijan!123";

            boolean result=true;
            try
            {
                ftpclient.connect(ftpServerAddress);
                result = ftpclient.login(userName, password);
                ftpclient.changeWorkingDirectory("/httpdocs/UserImage/");
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

                FTPFile[] filesList = ftpclient.listFiles();
                ByteArrayOutputStream stream=null;
                arrBitImage=new ArrayList<>();
                for(int ii=0;ii<filesList.length;ii++)
                {
                    stream = new ByteArrayOutputStream();
                    ftpclient.retrieveFile(filesList[ii].getName(),stream);
                    byte[] tempimage=stream.toByteArray();
                    arrBitImage.add(BitmapFactory.decodeByteArray(tempimage,0,tempimage.length));

                    Log.e("Image Name",filesList[ii].toString());
                }
                stream.close();

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
        }
    }
    void Initialize(View view)
    {
        listView=(ListView)view.findViewById(R.id.lvstudent);
    }



   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_placement, container, false);
    }*/

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}