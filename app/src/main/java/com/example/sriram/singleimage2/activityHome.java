package com.example.sriram.singleimage2;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class activityHome extends AppCompatActivity {

    private DrawerLayout drl;
    private ActionBarDrawerToggle tog;
    Fragment myFragment=null;
    Class fragmentclass=null;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //invoking main fragment


    try
    {
        fragmentclass=Notice.class;

        myFragment =(Fragment)fragmentclass.newInstance();
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    FragmentManager fragmentManager=getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.flcontent,myFragment).commit();

        drl=(DrawerLayout)findViewById(R.id.drawer);




        tog=new ActionBarDrawerToggle(this,drl,R.string.open,R.string.close);

        drl.addDrawerListener(tog);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navView1);
        tog.syncState();

        setDrawercontent(navigationView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.drawermenu,menu);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(tog.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void selectItemDrawer(MenuItem menuItem)
    {

        switch (menuItem.getItemId())
        {
            case R.id.home:
                /*intent = new Intent(getApplicationContext(), activityHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getApplicationContext().startActivity(intent);*/
                fragmentclass=null;
                fragmentclass=Notice.class;
                break;
            case R.id.info:
                fragmentclass=null;
                fragmentclass=Upload.class;
                break;
            case R.id.psq:
                fragmentclass=null;
                fragmentclass=PreYearQues.class;
                break;
            case R.id.pp:
                fragmentclass=null;
                fragmentclass=Placement.class;
                break;
            case R.id.changepass:
                fragmentclass=null;
                fragmentclass=Changepassword.class;
                break;
            case R.id.event:
                fragmentclass=null;
                fragmentclass=Event.class;
                break;
            case R.id.about:
                fragmentclass=null;
                fragmentclass=About.class;
                break;
            case R.id.logout:

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());
                builder1.setMessage("Do you want to Logout");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                                intent = new Intent(getApplicationContext(), Login.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                getApplicationContext().startActivity(intent);


                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                break;
            default:
                fragmentclass=Upload.class;
        }
        try
        {

            myFragment =(Fragment)fragmentclass.newInstance();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flcontent,myFragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drl.closeDrawers();
    }
    private void setDrawercontent(NavigationView navigationView)
    {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);
                return true;
            }
        });
    }
}
