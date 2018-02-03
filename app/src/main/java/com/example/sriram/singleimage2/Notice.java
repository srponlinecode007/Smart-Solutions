package com.example.sriram.singleimage2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Notice extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    HashMap<String,String>arrayImageNameHasMap;
    int totalStudent=1;
    ArrayList<NoticePojo> noticePojos;
    NoticeAdapter adapter;
    ListView listView;
    private static final int RequestPermissionCode=123;



    private OnFragmentInteractionListener mListener;

    public Notice() {
        // Required empty public constructor
    }
    public static Notice newInstance(String param1, String param2) {
        Notice fragment = new Notice();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    class FetchDataToDatabase extends AsyncTask<String, Void, ArrayList<NoticePojo>> {


        ProgressDialog progressDialog;
        String result = null;
        InputStream is = null;

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getContext(), "Please Wait Loading Data", null, true, true);
        }

        @Override
        public ArrayList<NoticePojo> doInBackground(String... param)
        {
            arrayImageNameHasMap=new HashMap<>();
            try
            {
                noticePojos = new ArrayList<>();

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://srpsolutions.co.in/shownotice.php");
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

                String lname= Notice_Details_Pojo.getLname();
                result = sb.toString();
                String ImageId="image";
                int id=1;
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i< jsonArray.length(); i++)
                {
                   JSONObject json_data = jsonArray.getJSONObject(i);

                    if(json_data.getInt("isimage")==1)
                    {
                        ImageId=ImageId+id+String.valueOf(1);
                        arrayImageNameHasMap.put(ImageId,json_data.getString("imagename")+","+json_data.getString("imageid"));
                        id++;
                    }
                    noticePojos.add(
                            new NoticePojo
                                    (
                                            json_data.getString("name"),
                                            json_data.getInt("year"),
                                            json_data.getString("regno"),
                                            json_data.getString("imageid"),
                                            json_data.getString("imagename"),
                                            json_data.getString("Dattime"),
                                            json_data.getString("text"),
                                            json_data.getInt("isimage"),
                                            ImageId
                                    )
                    );
                }
            }
            catch (Exception e)
            {
                Log.e("log_tag", "Error converting result " + e.toString());
               // Toast.makeText(getContext(), " Input reading fail", Toast.LENGTH_SHORT).show();

            }
            return noticePojos;
        }

        @Override
        protected void onPostExecute(ArrayList<NoticePojo> result) {

            super.onPostExecute(result);
            progressDialog.dismiss();
            adapter=new NoticeAdapter(getContext(),result,arrayImageNameHasMap);
            listView.setAdapter(adapter);

            Log.e("Result",result.toString());

           // Toast.makeText(getContext(), result.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    void Initialize(View view)
    {
        if(Build.VERSION.SDK_INT>22) {
            if (checkPermission())
                Toast.makeText (getContext(), "You Have Permission to Wrtie And Read From", Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(getContext(), "You Have no Permission to Wrtie And Read From,Requestion", Toast.LENGTH_LONG).show();
                requestPermission();
            }
        }



        listView=(ListView)view.findViewById(R.id.lvnotice);
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]
                {
                        android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.INTERNET
                }, RequestPermissionCode);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadInternetCapability=grantResults[2]==PackageManager.PERMISSION_GRANTED;
                    if (CameraPermission && ReadContactsPermission && ReadInternetCapability) {

                        Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(getContext(),"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }
    public boolean checkPermission()
    {


        int FirstPermissionResult = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult=ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.INTERNET);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult==PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        Initialize(view);
        context=view.getContext();
        FetchDataToDatabase fetchDataToDatabase = new FetchDataToDatabase();
        fetchDataToDatabase.execute();



        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
