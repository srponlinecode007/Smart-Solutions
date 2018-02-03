package com.example.sriram.singleimage2;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.button;
import static android.R.attr.fragment;
import static android.R.attr.logo;
import static android.R.attr.theme;
import static android.R.attr.value;
import static android.app.Activity.RESULT_OK;


public class Upload extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList<Bitmap>sendToAdapter;
    GridView imagegrid;
    ArrayList<String> imagesUriList;
    GridView gv;
    int PICK_IMAGE_MULTIPLE = 1;
    UploadAdapter uploadAdapter;
    EditText etmessage;
    String imageEncoded;
    String reg="",txtmessage="";
    String sendToyear="";
    String imageName="";
    ArrayList<Uri>tempList;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    final String selectedOption = "";
    Button mOrder;
    TextView mItemSelected;
    String[] listItems;
    boolean[] checkedItems;
    InputStream is;
    String line = null;
    ArrayList<Integer> mUserItems = new ArrayList<>();

    public Upload() {

    }


    public static Upload newInstance(String param1, String param2) {
        Upload fragment = new Upload();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        // fragment.setArguments(args);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootView = inflater.inflate(R.layout.fragment_upload, container, false);

        //mItemSelected=(TextView)rootView.findViewById(R.id.)


        imagegrid = (GridView) rootView.findViewById(R.id.gv);
        Button btnPhotofile = (Button) rootView.findViewById(R.id.btnphotofile);
        Button btnUpload = (Button) rootView.findViewById(R.id.btnupload);
        etmessage=(EditText)rootView.findViewById(R.id.editText);
        final Dialog dialog = new Dialog(getContext());
        // dialog.setContentView(R.layout.cu);

        //dialog.setTitle("Title...");

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                selectYearDialog();
            }
        });


        btnPhotofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
               // intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imagesUriList=new ArrayList<>();

       // final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
      //  final String orderBy = MediaStore.Images.Media._ID;
        tempList=new ArrayList<>();
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data)
        {
            try {
                if (data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(selectedImageUri,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);

                    tempList.add(selectedImageUri);
                    cursor.close();
                    Log.e("Picture", picturePath);
                    decodeUri(tempList);
                }
                if (data.getClipData() != null) {
                    ClipData selectedClicpData = data.getClipData();

                    for (int i = 0; i < selectedClicpData.getItemCount(); i++) {

                        ClipData.Item item = selectedClicpData.getItemAt(i);
                        Uri imgUri = item.getUri();
                        tempList.add(imgUri);
                    }
                    decodeUri(tempList);
                }




            }
            catch (Exception e)
            {
                Log.e("Exception ",e.getMessage());
            }

        }
    }


    public void decodeUri(ArrayList<Uri> uri)
    {
        ParcelFileDescriptor parcelFD = null;
        try
        {
            sendToAdapter=new ArrayList<>();
            for (int k = 0; k < uri.size(); k++)
            {
                parcelFD = getContext().getContentResolver().openFileDescriptor(uri.get(k), "r");
                FileDescriptor imageSource = parcelFD.getFileDescriptor();

                // Decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(imageSource, null, o);

                // the new size we want to scale to
                final int REQUIRED_SIZE = 1024;

                // Find the correct scale value. It should be the power of 2.
                int width_tmp = o.outWidth, height_tmp = o.outHeight;
                int scale = 1;
                while (true)
                {
                    if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                    {
                        break;
                    }
                    width_tmp /= 2;
                    height_tmp /= 2;
                    scale *= 2;
                }

                // decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize = scale;
                Bitmap bitmap = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);

                sendToAdapter.add(bitmap);

                //imageview.setImageBitmap(bitmap);

            }

            uploadAdapter=new UploadAdapter(getContext(),sendToAdapter);
            imagegrid.setAdapter(uploadAdapter);

            for (int i = 0; i < sendToAdapter.size(); i++) {
                Log.e("List Name After ", sendToAdapter.get(i).toString());
            }
        }
        catch(FileNotFoundException e)
        {
            Log.e("Flenotfound from Decode", e.getMessage());
        }
        catch(IOException e)
        {
            Log.e("IOfound from Decode", e.getMessage());
        }
        finally
        {
            if (parcelFD != null)
                try {
                    parcelFD.close();
                } catch (IOException e) {
                    Log.e("IOfound from Decode", e.getMessage());
                    // ignored
                }
        }

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void selectYearDialog()
    {


        final String[] listItems;
        final ArrayList<Integer> mUserItems = new ArrayList<>();

        listItems = getResources().getStringArray(R.array.mcayear);

        checkedItems = new boolean[listItems.length];


        AlertDialog.Builder mbuilder = new AlertDialog.Builder(getContext());
        mbuilder.setTitle("want to sent");
        mbuilder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {

                if (isChecked) {
                    if (!mUserItems.contains(position)) {
                        mUserItems.add(position);
                    }
                } else {
                    mUserItems.remove(position);
                }
            }
        });
        mbuilder.setCancelable(false);
        mbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                for (int i = 0; i < mUserItems.size(); i++) {
                    sendToyear = sendToyear + listItems[mUserItems.get(i)];
                    if (i != mUserItems.size() - 1) {
                        sendToyear = sendToyear + ", ";
                    }
                }
                //mItemSelected.setText(item);
                txtmessage = etmessage.getText().toString().trim();



                Date date=new Date();
                String dat=date.toLocaleString();
                String imageDateTime=dat.replaceAll(" ","").replaceAll("IST","").replaceAll(":","");


                Pattern p = Pattern.compile("\\d+");
                String sendToyr="";
                Matcher m = p.matcher(sendToyear);
                while (m.find())
                {
                    sendToyr=sendToyr+m.group();
                }
                imageName=Notice_Details_Pojo.getRegno()+"_"+Notice_Details_Pojo.getFname()+"_"+Notice_Details_Pojo.getCurryear()+"_"+sendToyr+"_"+imageDateTime;

                Log.e("Image Name ",imageName);

                String imId="";
                if(sendToAdapter!=null )
                {
                    for (int a = 0; a < sendToAdapter.size(); a++) {
                        imId = imId + String.valueOf(a + 1) + ",";
                        Log.e("List Of Sel Image", sendToAdapter.get(a).toString());
                    }
                }
                Log.e("Image Id",imId);


                String regno=Notice_Details_Pojo.getRegno();
                String name=Notice_Details_Pojo.getFname()+Notice_Details_Pojo.getLname();
                int year=Notice_Details_Pojo.getCurryear();
                String dattime=dat;
                String text=txtmessage;
                String imagenam=imageName;
                String imageid=imId;


               // Toast.makeText(getContext(),"Date is "+dattime,Toast.LENGTH_LONG).show();

                FetchDataToDatabase fetchDataToDatabase = new FetchDataToDatabase();
                fetchDataToDatabase.execute(regno,name,String.valueOf(year),dattime,text,imagenam,imageid);


                Toast.makeText(getContext(),sendToyear,Toast.LENGTH_LONG).show();

            }
        });



        mbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

       /* mbuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                for (int i = 0; i < checkedItems.length; i++) {
                    checkedItems[i] = false;
                    mUserItems.clear();
                   mItemSelected.setText("");
                }
            }
        });*/
        AlertDialog mDialog = mbuilder.create();
        mDialog.show();
    }

    class FetchDataToDatabase extends AsyncTask<String, Void, String> {
        String imgInfo;
        ProgressDialog progressDialog;
        String imMsg;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           progressDialog = ProgressDialog.show(getContext(), "Please Wait...", null, true, true);

        }

        @Override
        protected String doInBackground(String... params) {


            String ServerURL="http://srpsolutions.co.in/notice.php";
            String Response="";


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            String reg=params[0];
            String name=params[1];
            String year=params[2];
            String date=params[3];
            String text=params[4];
            String imagename="";
            String imageid=params[6];

            Log.e("Date Fom Notice",reg);
            int isImage=1;
            if(sendToAdapter==null || sendToAdapter.isEmpty() )
            {
                isImage=0;
                imagename="no Image";
            }
            else
            {
                imagename=params[5];
            }

            nameValuePairs.add(new BasicNameValuePair("regno", params[0]));
            nameValuePairs.add(new BasicNameValuePair("name", params[1]));
            nameValuePairs.add(new BasicNameValuePair("year", params[2]));
            nameValuePairs.add(new BasicNameValuePair("dattime", params[3]));
            nameValuePairs.add(new BasicNameValuePair("text", params[4]));
            nameValuePairs.add(new BasicNameValuePair("imagename", imagename));
            nameValuePairs.add(new BasicNameValuePair("imageid", params[6]));
            nameValuePairs.add(new BasicNameValuePair("isimage",String.valueOf(isImage)));
            try
            {
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
                if(sendToAdapter==null || sendToAdapter.isEmpty()) {

                    imMsg="Only Text";
                }
                else
                {
                    imMsg = sendImageFTP();
                }
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

            return Response+" "+imMsg;
        }
        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            progressDialog.dismiss();
            if(result.contains("Inserted Successfully")){
                Toast.makeText(getContext(), "Data Uploaded Successfully", Toast.LENGTH_SHORT).show();
                etmessage.getText().clear();
                imagegrid.setAdapter(null);
                sendToyear = "";


                sendOutBroadcast();

            }
            else if(result.contains("Value Not Inserted"))
            {
                Toast.makeText(getContext(), "Data is not Uploaded", Toast.LENGTH_SHORT).show();
            }


            Log.e("Final Message : ",result);
        }

    }
    public  void sendOutBroadcast(){

        Intent i = new Intent();
        i.setAction("com.example.sriram.singleimage2");
        i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        getContext().sendBroadcast(i);
        //ThisActivity.this.sendBroadcast(intent)
    }
    String sendImageFTP()
    {

        String msg = null;
        FTPClient ftpclient = new FTPClient();
        FileInputStream fis = null;
        boolean result;
        String ftpServerAddress = "ftp.srpsolutions.co.in";
        String userName = "shriramftp";
        String password = "Srijan!123";

        try {
            ftpclient.connect(ftpServerAddress);
            result = ftpclient.login(userName, password);

            if (result == true) {
                msg = "Logged in Successfully !";
            } else {
                msg = "Login Fail!";
            }
            ftpclient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpclient.enterLocalPassiveMode();
            ftpclient.changeWorkingDirectory("/httpdocs/NoticeImage/");

            int imId=1;
            int noOfImage=1;
            for(int a=0;a<sendToAdapter.size();a++)
            {
                Bitmap bitmapimage=sendToAdapter.get(a);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmapimage.compress(Bitmap.CompressFormat.PNG,50,stream);
                byte[] byteArray = stream.toByteArray();

                ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(byteArray);
                String testName = imageName+"_"+String.valueOf(imId)+".png";
                result=ftpclient.storeFile(testName,byteArrayInputStream);

                imId++;
                stream.flush();
                stream.close();
            }

             msg = "Total ["+String.valueOf(noOfImage)+")Images have been Uploaded";

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

        return msg;
    }
}
