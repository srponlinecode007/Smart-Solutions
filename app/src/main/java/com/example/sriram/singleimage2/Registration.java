package com.example.sriram.singleimage2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Calendar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import java.security.Permission;
import  android.Manifest;

import id.zelory.compressor.Compressor;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    ImageView ivImage;
    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    Button btn, btnDate;
    int flag = 0;
    EditText etfname, etlname, etregno, etpassword, etcpassword, etemail, etcontact;
    TextView etimage;
    Spinner etsession, etcourse, etyear, etdept;
    String filePath = null;
    String line = null;
    private static final int RequestPermissionCode=123;

    String[] month = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public String getmonth(String abc) {
        int a = 0;
        for (int i = 0; i < month.length; i++) {
            if (abc.toLowerCase().matches(month[i].toLowerCase())) {
                a = i + 1;
                break;
            }

        }
        String ab = "";
        ab = String.valueOf(a);
        if (a < 10)
            ab = "0" + ab;
        return ab;
    }


    int tyear;
    DateFormat fmtDateAndTime = DateFormat.getDateTimeInstance();
    TextView lblDateAndTime;
    DatePickerDialog.OnDateSetListener d;
    String course, dept, dob, image, fname, lname, regno, pass, cpass, email, contact, session;
    InputStream is;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        //Checking and Settting  Permission

        if(Build.VERSION.SDK_INT>22) {
            if (checkPermission())
                Toast.makeText(this, "You Have Permission to Wrtie And Read From", Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(this, "You Have no Permission to Wrtie And Read From,Requestion", Toast.LENGTH_LONG).show();
                requestPermission();
            }
        }


        ivImage = (ImageView) findViewById(R.id.image);

        Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectImage();
            }
        });


        d = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        //Initialization of objects
        etfname = (EditText) findViewById(R.id.fname);
        etlname = (EditText) findViewById(R.id.lname);
        etregno = (EditText) findViewById(R.id.Regno);
        etpassword = (EditText) findViewById(R.id.password);
        etcpassword = (EditText) findViewById(R.id.cpassword);
        etemail = (EditText) findViewById(R.id.email);
        etcontact = (EditText) findViewById(R.id.contact);
        etsession = (Spinner) findViewById(R.id.spinsession);
        etcourse = (Spinner) findViewById(R.id.spincourse);
        etyear = (Spinner) findViewById(R.id.cyearspin);
        etdept = (Spinner) findViewById(R.id.spindept);
        etimage = (TextView) findViewById(R.id.txtimage);
        btn = (Button) findViewById(R.id.buttonreg);
        btnDate = (Button) findViewById(R.id.btnDate);


        //adding listener
        btn.setOnClickListener(this);
        btnDate.setOnClickListener(this);


        ArrayAdapter myAdapterdept = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.Department));

        ArrayAdapter myAdapteryear = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.year));

        ArrayAdapter myAdaptercourse = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.course));

        ArrayAdapter myAdaptersession = new ArrayAdapter<String>(Registration.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.session));


        // myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner.setAdapter(myAdapter);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void requestPermission() {

        ActivityCompat.requestPermissions(Registration.this, new String[]
                {
                        Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.INTERNET
                }, RequestPermissionCode);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] ==PackageManager.PERMISSION_GRANTED;
                    boolean ReadContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadInternetCapability=grantResults[2]==PackageManager.PERMISSION_GRANTED;
                    if (CameraPermission && ReadContactsPermission && ReadInternetCapability) {

                        Toast.makeText(Registration.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(Registration.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }
    public boolean checkPermission() {


        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ThirdPermissionResult=ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.INTERNET);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult==PackageManager.PERMISSION_GRANTED;
    }


    private void SelectImage() {
        final CharSequence[] items = {/*"From Camera",*/ "From Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Registration.this);
        builder.setTitle("Select Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               /* if (items[i].equals("From Camera")) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else*/ if (items[i].equals("From Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            /*if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                ivImage.setImageBitmap(bmp);
                String uri = bmp.toString();
                filePath = uri;
                //Toast.makeText(this,uri,Toast.LENGTH_LONG).show();
                TextView textView = (TextView) findViewById(R.id.image);
                textView.setText(uri);

            } else*/ if (requestCode == SELECT_FILE) {

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                filePath = picturePath;

                Log.e("Registration",picturePath);



                ivImage.setImageURI(selectedImage);
               // Bitmap compressedImageBitmap = new Compressor(this).compressToBitmap();
                cursor.close();
                //Toast.makeText(this,uri,Toast.LENGTH_LONG).show();
                TextView textView = (TextView) findViewById(R.id.txtimage);
                textView.setText(filePath);
            }
        }
    }

    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_add_image, menu);
            return true;
        }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateLabel() {

        lblDateAndTime.setText(fmtDateAndTime.format(myCalendar.getTime()));
        lblDateAndTime.setText(lblDateAndTime.getText().subSequence(0, 11));
        lblDateAndTime.setText(mydate(lblDateAndTime.getText().toString()));

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.buttonreg: {
                setData();
                FetchDataToDatabase fetchDataToDatabase = new FetchDataToDatabase();
                fetchDataToDatabase.execute(fname, lname, regno, pass, cpass, contact, dob, email, dept, course, String.valueOf(tyear), session, image);
            }

            break;
            case R.id.btnDate:
                lblDateAndTime = (TextView) findViewById(R.id.lblDateAndTime);
                Button btnDate = (Button) findViewById(R.id.btnDate);
                btnDate.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        new DatePickerDialog(Registration.this, d, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),

                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                //
                flag++;
                updateLabel();


                break;
        }

    }

    public String mydate(String db) {

        String[] arrOfStr = db.split(" ");

        return arrOfStr[2]+"-"+getmonth(arrOfStr[1])+"-"+arrOfStr[0];
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Registration Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    class FetchDataToDatabase extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Registration.this, "Please Wait Loading Data", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {


            String ServerURL="http://srpsolutions.co.in/registration.php";
            String Response="";
            String fnme=params[0];
            String lnme=params[1];
            String rgno=params[2];
            String pas=params[3];
            String cpas=params[4];
            String contct=params[5];
            String db=params[6];
            String emal=params[7];
            String dpt=params[8];
            String corse=params[9];
            int tyer=Integer.parseInt(params[10]);
            String ses=params[11];
            String img=params[12];




            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("fName", fnme));
            nameValuePairs.add(new BasicNameValuePair("lName", lnme));
            nameValuePairs.add(new BasicNameValuePair("reg", rgno));
            nameValuePairs.add(new BasicNameValuePair("pass", pas));
            nameValuePairs.add(new BasicNameValuePair("cpass", cpas));
            nameValuePairs.add(new BasicNameValuePair("contact", contct));
            nameValuePairs.add(new BasicNameValuePair("dob", db));
            nameValuePairs.add(new BasicNameValuePair("email", emal));
            nameValuePairs.add(new BasicNameValuePair("dept", dpt));
            nameValuePairs.add(new BasicNameValuePair("course", corse));
            nameValuePairs.add(new BasicNameValuePair("cyear", String.valueOf(tyer)));
            nameValuePairs.add(new BasicNameValuePair("session", ses));
            nameValuePairs.add(new BasicNameValuePair("image", img));

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
            String imMsg=sendImageFTP();

            return Response+" "+imMsg;
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

                ftpclient.changeWorkingDirectory("/httpdocs/UserImage/");

                File file = new File(filePath);
                String testName = regno + ".jpg";
                fis = new FileInputStream(file);
Log.e("File Registration",filePath);
                // Upload file to the ftp server
                result = ftpclient.storeFile(testName, fis);

                if (result == true) {
                    msg = "Image Uploaded";
                } else {
                    msg = "Image Uploadation Failed";
                }
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


        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result.contains("successfully"))
            {
                Toast.makeText(Registration.this, "Registration Completed Successfully", Toast.LENGTH_LONG).show();

                Intent intentgologin=new Intent(getApplicationContext(),Login.class);
                Bundle bndl=new Bundle();
                bndl.putString("userName",etregno.getText().toString());

                intentgologin.putExtras(bndl);
                setResult(RESULT_OK,intentgologin);

                etfname.getText().clear();
                etpassword.getText().clear();
                etlname.getText().clear();
                etregno.getText().clear();
                etemail.getText().clear();
                etcpassword.getText().clear();
                etcontact.getText().clear();

            } else if (result.contains("Duplicate entry")) {
                Toast.makeText(Registration.this, "You Have Already Registered", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Registration.this, "Error While Saving Data" + result, Toast.LENGTH_LONG).show();
            }
        }

    }

    void setData() {

       fname = etfname.getText().toString().trim();
        lname = etlname.getText().toString().trim();
        regno = etregno.getText().toString().trim();
        pass = etpassword.getText().toString().trim();
        cpass = etcpassword.getText().toString().trim();
        email = etemail.getText().toString().trim();
        contact =etcontact.getText().toString().trim();
        session =etsession.getSelectedItem().toString().trim();
        tyear =Integer.parseInt(etyear.getSelectedItem().toString());
        image =etimage.getText().toString().trim();
        dob = lblDateAndTime.getText().toString().trim();
        dept = etdept.getSelectedItem().toString().trim();
        course =etcourse.getSelectedItem().toString().trim();



        /*fname = "Ram";//etfname.getText().toString().trim();
        lname = "Kumar";//etlname.getText().toString().trim();
        regno = "2017dha1";//etregno.getText().toString().trim();
        pass = "12";//etpassword.getText().toString().trim();
        cpass ="12";// etcpassword.getText().toString().trim();
        email = "asd@fmail.com";//etemail.getText().toString().trim();
        contact ="123"; //etcontact.getText().toString().trim();
        session ="2014-205";//etsession.getPrompt().toString().trim();
        tyear = 1;//Integer.parseInt(etyear.getText.toString().trim());
        image =etimage.getText().toString().trim();
        dob = "2018-5-21";//"2018-02-12";//dob.get.toString().trim();
        dept = "Na";//etdept.getPrompt().toString().trim();
        course ="MCA"; //etcourse.getSelectedItem().toString().trim();*/

    }

}
