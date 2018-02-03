package com.example.sriram.singleimage2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.SearchView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class PreYearQues extends Fragment {

    private static final int RequestPermissionCode=123;
    ListView listView;
    ArrayList<list_pre_year_paper> listpreyearpapers;
     //ArrayAdapter<String> adapter;
    list_pre_paper_adapter adapter;
    int totalStudent=0;
    TextView studentno;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PreYearQues() {
    }

    public static PreYearQues newInstance(String param1, String param2) {
        PreYearQues fragment = new PreYearQues();
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

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_pre_year_ques, container, false);
        Initialize(view);
        context=getContext();
        studentno = (TextView)view.findViewById(R.id.txtstudentno) ;
        FetchDataToDatabase fetchDataToDatabase = new FetchDataToDatabase();
        fetchDataToDatabase.execute();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();

        if (Build.VERSION.SDK_INT > 22) {
            if (checkPermission())
                Toast.makeText(context, "You Have Permission to Wrtie And Read From", Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(context, "You Have no Permission to Wrtie And Read From,Requestion", Toast.LENGTH_LONG).show();
                requestPermission();
            }



            inflater.inflate(R.menu.menu_add_image, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView)menu.findItem(R.id.action_search).getActionView();
        SearchView searchView = (SearchView) item.getActionView();
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //adapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(adapter!=null) {
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }
        );
    }}
    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]
                {
                        Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET
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

                        Toast.makeText(context, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(context,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult=ContextCompat.checkSelfPermission(context,Manifest.permission.INTERNET);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult==PackageManager.PERMISSION_GRANTED;
    }

    class FetchDataToDatabase extends AsyncTask<String, Void, ArrayList<list_pre_year_paper>> {


        ProgressDialog progressDialog;
        String result = null;
        InputStream is = null;

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getContext(),"Please Wait Loading Data", null, true, true);
        }
        @Override
        public ArrayList<list_pre_year_paper> doInBackground(String... param)
        {
            try
            {
                listpreyearpapers=new ArrayList<>();

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://srpsolutions.co.in/showquestions.php");
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                    //  Toast.makeText(getApplicationContext(), "Input Reading pass", Toast.LENGTH_SHORT).show();
                }
                is.close();

                result = sb.toString();

                JSONArray jsonArray=new JSONArray(result);
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject json_data = jsonArray.getJSONObject(i);
                    listpreyearpapers.add(new list_pre_year_paper(json_data.getString("subname"),json_data.getString("sem"),json_data.getString("semtype"),json_data.getString("year"),json_data.getString("qid")));
                    totalStudent++;
                }
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
                Toast.makeText(getContext(), " Input reading fail", Toast.LENGTH_SHORT).show();

            }

            return listpreyearpapers;
        }
        @Override
        protected void onPostExecute(ArrayList<list_pre_year_paper> result) {

            super.onPostExecute(result);
            progressDialog.dismiss();
            adapter=new list_pre_paper_adapter(getContext(),result);
            listView.setAdapter(adapter);
            studentno.setText(String.valueOf(--totalStudent));

           // Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
}
    }


    void Initialize(View view)
    {
        listView=(ListView)view.findViewById(R.id.lstPrePapDetails);
        listView.setTextFilterEnabled(true);
    }


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

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }

}
