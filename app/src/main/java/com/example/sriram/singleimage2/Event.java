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
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Event extends Fragment {


    ArrayList<EventPojo> eventListPojos;
    EventAdapter adapter;
    ListView listView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Event() {
        // Required empty public constructor
    }
    public static Event newInstance(String param1, String param2) {
        Event fragment = new Event();
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

        View view= inflater.inflate(R.layout.fragment_event, container, false);
        context=getContext();
        Initialize(view);
        FetchDataToDatabase fetchDataToDatabase = new FetchDataToDatabase();
        fetchDataToDatabase.execute();
        return view;
    }
    class FetchDataToDatabase extends AsyncTask<String, Void, ArrayList<EventPojo>> {


        ProgressDialog progressDialog;
        String result = null;
        InputStream is = null;

        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(getContext(), "Please Wait Loading Data", null, true, true);
        }

        @Override
        public ArrayList<EventPojo> doInBackground(String... param) {
            try {
                eventListPojos = new ArrayList<>();

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://srpsolutions.co.in/showevent.php");
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

                result = sb.toString();
                String regno;
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json_data = jsonArray.getJSONObject(i);
                    eventListPojos.add(new EventPojo(json_data.getString("date"), json_data.getString("text")));

                }
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
                Toast.makeText(getContext(), " Input reading fail", Toast.LENGTH_SHORT).show();

            }
            return eventListPojos;
        }

        @Override
        protected void onPostExecute(ArrayList<EventPojo> result) {

            super.onPostExecute(result);
            progressDialog.dismiss();
           /* for (int i=0;i<result.size();i++) {
                Log.e("Data ", String.valueOf(result.get(i).getyear()));
            }*/
            adapter = new EventAdapter(context, result);
            listView.setAdapter(adapter);

            // Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
        }
    }
    void Initialize(View view)
    {
        listView=(ListView)view.findViewById(R.id.listevent);
    }

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
