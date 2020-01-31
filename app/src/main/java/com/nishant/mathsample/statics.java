package com.nishant.mathsample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.nishant.mathsample.initActivity.getDatabaseHelper;

public class statics extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private MyDatabaseHelper myDatabaseHelper;
    private ListView listView;
    //nav menu begin
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    private statics context;
    private Toolbar toolbar;
    //nav menu end


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statics);

        context=this;

        loadAll();




    }
    private void loadAll(){
        loadNavMenu();
        setNavMenuInfo();
        findAll();
        rankingLoad();
    }
    private void findAll(){
        //DbContract.allUserRankingDataFetching(this);
        listView= this.<ListView>findViewById(R.id.userRankListViewId);
        listView.setOnItemClickListener(this);

    }
    private void rankingLoad(){
         //DbContract.allUserRankingDataFetching(this);
        customAdapter adapter=new customAdapter(this);
        listView.setAdapter(adapter);
    }
    //load nav menu begin
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mtoggle.onOptionsItemSelected(item)){

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else{

            super.onBackPressed();

        }
    }
    public void loadNavMenu(){

        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mDrawerLayout =findViewById(R.id.drawer_layout);
        mtoggle =new ActionBarDrawerToggle(context,mDrawerLayout,toolbar,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mtoggle);
        mtoggle.syncState();


        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                setNavMenuInfo();

                int id=item.getItemId();
                if(id==R.id.solveProblemId){
                    Toast.makeText(context,"solved problems",Toast.LENGTH_SHORT).show();

                }
                if(id==R.id.attemptedProblems){
                    Toast.makeText(context,"attempted problems",Toast.LENGTH_SHORT).show();
                }
                if(id==R.id.notificationSettingId){
                    Toast.makeText(context,"Notification",Toast.LENGTH_SHORT).show();
                }
                if(id==R.id.logOutId){

                    Toast.makeText(context,"logout",Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to Log out?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int id) {
                                    myDatabaseHelper.updateLocalDatabase(DbContract.LOGIN_STATUS_OUT);
                                    Intent intent = new Intent(context, initActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int id) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alertDialog=builder.create();
                    alertDialog.show();
                }
                return false;
            }
        });
    }


    private void setNavMenuInfo(){

        TextView USERNAME,NAME,INSTITUTION,EMAIL,PHONE;
        String _USERNAME="",_NAME="",_INSTITUTION="",_EMAIL="",_PHONE="";
       // Button loginNavmenu;
        int logingStatus=DbContract.LOGIN_STATUS_OUT;
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);




        navigationView= this.<NavigationView>findViewById(R.id.nav_view);


        View headerView =navigationView.getHeaderView(0);
        USERNAME= headerView.<TextView>findViewById(R.id.userNameId);
        NAME= headerView.<TextView>findViewById(R.id.fullUserNameId);
        INSTITUTION= headerView.<TextView>findViewById(R.id.institutionTextId);
        EMAIL= headerView.<TextView>findViewById(R.id.userEmailProfileId);
        PHONE= headerView.<TextView>findViewById(R.id.userPhoneProfileId);
        //loginNavmenu= headerView.<Button>findViewById(R.id.loginButtonNavMenuId);


        myDatabaseHelper=getDatabaseHelper();
        Cursor cursor=myDatabaseHelper.query("userInformation",DbContract.CURRENT_USER_NAME);


        String SOLVING_STRING="";

        if(cursor.moveToNext()) {
            _NAME = cursor.getString(0);
            _USERNAME = cursor.getString(1);
            _INSTITUTION = cursor.getString(7);
            _EMAIL = cursor.getString(5);
            _PHONE = cursor.getString(6);
            SOLVING_STRING=cursor.getString(8);
            logingStatus=cursor.getInt(10);
        }
        DbContract.CURRENT_USER_LOGIN_STATUS=logingStatus;
        DbContract.userSolvingResult(SOLVING_STRING);

        USERNAME.setText(_USERNAME);
        NAME.setText(_NAME);
        INSTITUTION.setText(_INSTITUTION);
        EMAIL.setText(_EMAIL);
        PHONE.setText(_PHONE);

        // get menu from navigationView
        Menu menu = navigationView.getMenu();
        MenuItem solvedNumber=navigationView.getMenu().findItem(R.id.solveProblemId);
        MenuItem attemptedNumber=navigationView.getMenu().findItem(R.id.attemptedProblems);

        solvedNumber.setTitle(DbContract.CURRENT_USER_TOTAL_SOLVED);
        attemptedNumber.setTitle(DbContract.CURRENT_USER_TOTAL_ATTEMPTED);


        /*loginNavmenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.loginButtonNavMenuId){

                    if(DbContract.CURRENT_USER_LOGIN_STATUS==DbContract.LOGIN_STATUS_ON){
                        DbContract.Alert(context,"Are not you "+DbContract.CURRENT_USER_NAME+" ?","Log Out first");
                        return;
                    }
                    else{

                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setMessage("Hello Guest(-_-)\nAre you sure you want to login?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int id) {

                                        Intent intent = new Intent(context, loginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int id) {
                                        dialogInterface.cancel();
                                    }
                                });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();


                    }

                }
            }
        });*/
    }


    //nav menu function end

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        Object obj= getListView().getItemAtPosition(position);

        Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_SHORT).show();

        if(checkNetworkConnection()==false) {
            DbContract.Alert(statics.this,"Network Connectivity:","Connect to Internet");
            return;
        }


        if(DbContract.userRankList.get(position)!=null){

           userRankStatics person=DbContract.userRankList.get(position);

            String USERNAME=person.getUSERNAME();


            // 8/11/2019 10:29AM working now
            DbContract.showRankingUserDataUpdated=false;
            DbContract.userRankSolvingStringFetching(this,USERNAME);

            Intent intent=new Intent(this,userProfileActivity.class);
            intent.putExtra("userType","online");
            intent.putExtra("userName",USERNAME);
            intent.putExtra("userIndex",position);

            startActivity(intent);
        }


    }
    public ListView getListView() {

        return listView;
    }
    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();

        return (networkInfo!=null && networkInfo.isConnected());

    }
}
