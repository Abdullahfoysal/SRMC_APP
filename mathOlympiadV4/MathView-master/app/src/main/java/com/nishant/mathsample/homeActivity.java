package com.nishant.mathsample;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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


public class homeActivity extends AppCompatActivity implements View.OnClickListener {

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

    private BroadcastReceiver broadcastReceiver,broadcastReceiverUpdateProblem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context=this;

        CURRENT_USER=DbContract.CURRENT_USER_NAME;

        loadAll();//nav,button,database//network backgroundwork
        System.out.println(DbContract.CURRENT_USER_NAME+" ON PROFILE IS ACTIVE");

    }



    private void loadAll(){

        loadLocalDatabase();
         guestUser();
        findAllButton();
        checkingNetworkAndThread();//problem update to local and user signUp upload to server
        loadNavMenu();
        setNavMenuInfo();
        userAllFunction();


    }
    private void userAllFunction(){
        DbContract.Alert(this,"User information","Welcome "+CURRENT_USER);
        //all user Rank
        DbContract.allUserRankingDataFetching(this);

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

                        Toast.makeText(homeActivity.this,"All Problems",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(homeActivity.this,problemActivity.class);
                        intent.putExtra("method","allProblem");
                        intent.putExtra("userType",USER_TYPE);
                        startActivity(intent);

                    }
                    else if(index==1){
                        //statics
                        //new thread for Rank update begin
                        new Thread(){
                            @Override
                            public void run() {
                                try {

                                    DbContract.allUserRankingDataFetching(context);

                                }catch (Exception e){
                                    e.printStackTrace();
                                }



                            }
                        }.start();

                        //new thread for rank update end


                        Toast.makeText(homeActivity.this,"Ranking",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(homeActivity.this,statics.class);
                        intent.putExtra("method","Statics");
                        intent.putExtra("userType",USER_TYPE);
                        startActivity(intent);

                    }
                    else if(index==2){
                        Toast.makeText(homeActivity.this,"Attempted Problems",Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(homeActivity.this,problemActivity.class);
                        intent.putExtra("method","attempted");
                        intent.putExtra("userType",USER_TYPE);
                        startActivity(intent);
                    }
                    else if(index==3){
                        Toast.makeText(homeActivity.this,"Solved Problems",Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(homeActivity.this,problemActivity.class);
                        intent.putExtra("method","solved");
                        intent.putExtra("userType",USER_TYPE);
                        startActivity(intent);
                    }
                    else if(index==4){
                        Toast.makeText(homeActivity.this,"See shotcuts on Navigation Menu",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(homeActivity.this,myProfileActivity.class);
                        intent.putExtra("userName",CURRENT_USER);
                        intent.putExtra("userType","offline");
                        startActivity(intent);
                    }
                    else if(index==5){

                        Toast.makeText(homeActivity.this,"Activity Monitoring",Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(homeActivity.this,dataSyncActivity.class));
                    }

                }
            });
        }
    }
    private void guestUser(){

        Cursor cursor=myDatabaseHelper.query("userInformation",CURRENT_USER);
        if(cursor.moveToNext()){
            myDatabaseHelper.updateLocalDatabase(CURRENT_USER,DbContract.SYNC_STATUS_OK,DbContract.NEW_USER_SOLVING_STRING,"0");
        }
        else
        myDatabaseHelper.insertData(CURRENT_USER, CURRENT_USER, CURRENT_USER, CURRENT_USER, CURRENT_USER, CURRENT_USER, CURRENT_USER, CURRENT_USER, DbContract.NEW_USER_SOLVING_STRING, "0", DbContract.SYNC_STATUS_OK);

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
        broadcastReceiverUpdateProblem=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };

    }

    private void setNavMenuInfo(){

        TextView USERNAME,NAME,INSTITUTION,EMAIL,PHONE;
        String _USERNAME="",_NAME="",_INSTITUTION="",_EMAIL="",_PHONE="";

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        // get menu from navigationView
       // Menu menu = navigationView.getMenu();

        navigationView= this.<NavigationView>findViewById(R.id.nav_view);

        //MenuItem menuItem=navigationView.getMenu().findItem(R.id.)
        View headerView =navigationView.getHeaderView(0);
        USERNAME= headerView.<TextView>findViewById(R.id.userNameId);
        NAME= headerView.<TextView>findViewById(R.id.fullUserNameId);
        INSTITUTION= headerView.<TextView>findViewById(R.id.institutionTextId);
        EMAIL= headerView.<TextView>findViewById(R.id.userEmailProfileId);
        PHONE= headerView.<TextView>findViewById(R.id.userPhoneProfileId);


        Cursor cursor=myDatabaseHelper.query("userInformation",DbContract.CURRENT_USER_NAME);

        if(cursor.moveToNext()) {
            _NAME = cursor.getString(0);
            _USERNAME = cursor.getString(1);
            _INSTITUTION = cursor.getString(7);
            _EMAIL = cursor.getString(5);
            _PHONE = cursor.getString(6);
        }

        USERNAME.setText(_USERNAME);
        NAME.setText(_NAME);
        INSTITUTION.setText(_INSTITUTION);
        EMAIL.setText(_EMAIL);
        PHONE.setText(_PHONE);

    }




    private void findAllButton(){


        showProblemButton= this.<Button>findViewById(R.id.showButtonId);
        updateProblemButton= this.<Button>findViewById(R.id.updateButtonId);
        addProblemButton= this.<Button>findViewById(R.id.addProblemButtonId);
        signUpButton= this.<Button>findViewById(R.id.signUpButtonId);
        syncButton=findViewById(R.id.uploadUserInformationId);
        refreshButton= this.<Button>findViewById(R.id.refreshButtonId);
        attemptedButton= this.<Button>findViewById(R.id.attemptedProblemButtonId);
        solvedButton= this.<Button>findViewById(R.id.solvedProblemButtonId);



        showProblemButton.setOnClickListener(this);
        updateProblemButton.setOnClickListener(this);
        addProblemButton.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        syncButton.setOnClickListener(this);
        refreshButton.setOnClickListener(this);
        attemptedButton.setOnClickListener(this);
        solvedButton.setOnClickListener(this);
    }






    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("broadcastReceiver onStart");
        registerReceiver(broadcastReceiver,new IntentFilter(DbContract.UI_UPDATE_BROADCAST));
        registerReceiver(broadcastReceiverUpdateProblem,new IntentFilter(DbContract.UI_UPDATE_BROADCAST));


    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(broadcastReceiverUpdateProblem);
    }


    @Override
    public void onClick(View view) {
        long id=view.getId();
        if(id==R.id.showButtonId){

            Intent intent=new Intent(this,problemActivity.class);
            intent.putExtra("method","allProblem");
            startActivity(intent);

        }
        else if(id==R.id.updateButtonId){

           /* String method="saveFromServer";
            BackgroundTask backgroundTask=new BackgroundTask(this);
            backgroundTask.execute(method);*/

        }
        else if(id==R.id.addProblemButtonId){
//            startActivity(new Intent(this,menuActivity.class));

        }
        else if(id==R.id.signUpButtonId){

//            startActivity(new Intent(this,signUpActivity.class));
        }
        else if(id==R.id.uploadUserInformationId){

//            startActivity(new Intent(this,dataSyncActivity.class));

        }
        else if(id==R.id.refreshButtonId){
//            DbContract.saveToAppServer(this,DbContract.USER_DATA_UPDATE_URL);
        }
        else if(id==R.id.attemptedProblemButtonId){
           /* Intent intent=new Intent(this,problemActivity.class);
            intent.putExtra("method","attempted");
            startActivity(intent);*/
        }
        else if(id==R.id.solvedProblemButtonId){
           /* Intent intent=new Intent(this,problemActivity.class);
            intent.putExtra("method","solved");
            startActivity(intent);*/

        }
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
            //exit app from this activity
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            //exit end
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
                if(id==R.id.updateInformationId){
                    Toast.makeText(context,"update information",Toast.LENGTH_SHORT).show();
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





}
