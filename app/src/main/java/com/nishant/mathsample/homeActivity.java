package com.nishant.mathsample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.nishant.mathsample.initActivity.getDatabase;
import static com.nishant.mathsample.initActivity.getDatabaseHelper;


public class homeActivity extends AppCompatActivity {

    private Button testSampleProblemButton;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    //nav menu begin
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    private homeActivity context;
    private Toolbar toolbar;
    private String CURRENT_USER="";
    private GridLayout gridLayout;
    private String USER_TYPE="offline";

    //nav menu end


    //button
    private Button showProblemButton,updateProblemButton,addProblemButton,signUpButton,syncButton,refreshButton,attemptedButton,solvedButton;

    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context=this;



            loadAll();//nav,button,database//network backgroundwork


    }



    private void loadAll(){
        //testSampleLoadAll();
        currentUserInformation();
        loadLocalDatabase();
        if(CURRENT_USER.equals("guest")) guestUser();
        findAllButton();
        checkingNetworkAndThread();//problem update to local and user signUp upload to server
        loadNavMenu();
        setNavMenuInfo();
        userAllFunction();


    }
    private void testSampleLoadAll(){
       /* testSampleProblemButton= this.<Button>findViewById(R.id.testSampleButtonId);
        testSampleProblemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(homeActivity.this,menuActivity.class));
            }
        });*/
    }
    private  void currentUserInformation(){
        CURRENT_USER=DbContract.CURRENT_USER_NAME;
        USER_TYPE=getIntent().getExtras().getString("userType");
    }

    private void userAllFunction(){
        if(DbContract.FIRST_IMPRESSION==DbContract.ON){
            DbContract.Alert(this,"User information","Welcome "+CURRENT_USER);
            //all user Rank
            DbContract.allUserRankingDataFetching(this);

            DbContract.FIRST_IMPRESSION=DbContract.OFF;
        }


        gridLayout= this.<GridLayout>findViewById(R.id.gridLayout);
        setSingleEvent(gridLayout);
    }
    private void setSingleEvent(GridLayout gridLayout){
        for(int i=0;i<gridLayout.getChildCount();i++){

            CardView cardView= (CardView) gridLayout.getChildAt(i);
            final int index=i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if(index==0){

                            //new thread for problem update begin
                        new Thread(){
                            @Override
                            public void run() {
                                try {

                                    DbContract.saveFromServer(context);

                                }catch (Exception e){
                                    e.printStackTrace();
                                }



                            }
                        }.start();

                        //new thread for problem update end

                        //Toast.makeText(homeActivity.this,"All Problems",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(homeActivity.this,problemActivity.class);
                        intent.putExtra("method","allProblem");
                        intent.putExtra("userType",USER_TYPE);
                        startActivity(intent);

                    }
                    else if(index==1){

                        if(checkNetworkConnection()==false){
                            DbContract.Alert(homeActivity.this,"Network Connectivity:","Connect to internet");
                            return;
                        }
                        //statics
                        DbContract.allUserRankingDataFetching(context);
                        //new thread for rank update end


                        //Toast.makeText(homeActivity.this,"Ranking",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(homeActivity.this,statics.class);
                        intent.putExtra("method","Statics");
                        intent.putExtra("userType",USER_TYPE);
                        startActivity(intent);

                    }
                    else if(index==2){
                       // Toast.makeText(homeActivity.this,"Attempted Problems",Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(homeActivity.this,problemActivity.class);
                        intent.putExtra("method","attempted");
                        intent.putExtra("userType",USER_TYPE);
                        startActivity(intent);
                    }
                    else if(index==3){
                       // Toast.makeText(homeActivity.this,"Solved Problems",Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(homeActivity.this,problemActivity.class);
                        intent.putExtra("method","solved");
                        intent.putExtra("userType",USER_TYPE);
                        startActivity(intent);
                    }
                    else if(index==4){
                      //  Toast.makeText(homeActivity.this,"See shotcuts on Navigation Menu",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(homeActivity.this,myProfileActivity.class);
                        intent.putExtra("userName",CURRENT_USER);
                        intent.putExtra("userType","offline");
                        startActivity(intent);
                    }
                    else if(index==5){

                       // Toast.makeText(homeActivity.this,"Activity Monitoring",Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(homeActivity.this,dataSyncActivity.class));
                    }

                }
            });
        }
    }
    private void guestUser(){

        Cursor cursor =myDatabaseHelper.query("userInformation","guest");

        if(cursor.moveToNext()){
            myDatabaseHelper.updateLocalDatabase(DbContract.LOGIN_STATUS_OUT);
        }
        else
        myDatabaseHelper.insertData(CURRENT_USER, CURRENT_USER, CURRENT_USER, CURRENT_USER, CURRENT_USER, CURRENT_USER, CURRENT_USER, CURRENT_USER, DbContract.NEW_USER_SOLVING_STRING, "0", DbContract.SYNC_STATUS_OK,DbContract.LOGIN_STATUS_OUT);

    }
    private void loadLocalDatabase(){
        this.sqLiteDatabase=getDatabase();
        this.myDatabaseHelper=getDatabaseHelper();
    }

    private void checkingNetworkAndThread(){



        DbContract.saveToAppServer(this,DbContract.USER_DATA_UPDATE_URL);

        DbContract.userInformationUpdateFromServer(this);//backgroundTask method="userDataFetching"

        setNavMenuInfo();

        //problemAndSolution saveFromServer thread

        new Thread(){
            @Override
            public void run() {
                try {

                    DbContract.saveFromServer(context);

                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        }.start();


        //NetWorkMonitoring method saveToServer
        broadcastReceiver=new BroadcastReceiver() {
            //userData saveToServer thread
            @Override
            public void onReceive( Context context, Intent intent) {

            }
        };


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
      //  loginNavmenu= headerView.<Button>findViewById(R.id.loginButtonNavMenuId);


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




    private void findAllButton(){



    }






    @Override
    protected void onStart() {
        super.onStart();
       // System.out.println("broadcastReceiver onStart");
        registerReceiver(broadcastReceiver,new IntentFilter(DbContract.UI_UPDATE_BROADCAST));



    }

    @Override
    protected void onPause() {
        super.onPause();
       // unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //unregisterReceiver(broadcastReceiver);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
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

            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?\nYou are logged in!")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {

                            if(CURRENT_USER.equals("guest")){
                                Intent intent = new Intent (homeActivity.this, loginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{

                                homeActivity.this.finish();
                                System.exit(0);
                                //exit end
                            }
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
                    Toast.makeText(context,"Refresh",Toast.LENGTH_SHORT).show();
                    setNavMenuInfo();

                    //problemAndSolution saveFromServer thread

                    new Thread(){
                        @Override
                        public void run() {
                            try {

                                DbContract.saveFromServer(context);

                            }catch (Exception e){
                                e.printStackTrace();
                            }



                        }
                    }.start();
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
                                    Intent intent = new Intent (homeActivity.this, loginActivity.class);
                                    startActivity(intent);
                                    finish();

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
    //nav menu function end

    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();

        return (networkInfo!=null && networkInfo.isConnected());

    }

 /*   @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        *//*savedInstanceState.putBoolean("MyBoolean", true);
        savedInstanceState.putDouble("myDouble", 1.9);
        savedInstanceState.putInt("MyInt", 1);*//*
        savedInstanceState.putString("userName", "abdullah");
        savedInstanceState.putString("password", "123");
        // etc.
    }*/





}
