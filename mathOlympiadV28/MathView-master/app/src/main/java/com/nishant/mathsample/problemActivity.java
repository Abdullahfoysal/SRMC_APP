package com.nishant.mathsample;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.nishant.mathsample.initActivity.getDatabaseHelper;

public class problemActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private TextView activityTitle;
    private ListView listView;
    private MyDatabaseHelper myDatabaseHelper;
    private Cursor cursor;
    private String CURRENT_USER="";

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    private problemActivity context;
    private Toolbar toolbar;
    private String method="";
    private String USER_TYPE="offline";
    private String SOLVING_STRING="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        //load nav_menu
        context=this;
        loadAll();



    }
    private void loadAll(){
        loadListView();//list view of problems
        loadListViewData();

        loadNavMenu();
        setNavMenuInfo();



    }
    private void loadListView(){
        listView = this.<ListView>findViewById(R.id.problemActivityListViewId);
        //Data base work
        myDatabaseHelper = new MyDatabaseHelper(this);
        listView.setOnItemClickListener(this);
    }
    private void loadListViewData(){

        method=getIntent().getExtras().getString("method");
        USER_TYPE=getIntent().getExtras().getString("userType");

        if(USER_TYPE.equals("online")){

            userRankStatics person=DbContract.userSolvingString.get(0);
            SOLVING_STRING=person.getSOLVING_STRING();
        }

        loadData();

    }

    public void loadData() {
        activityTitle= this.<TextView>findViewById(R.id.activityTitleId);
        activityTitle.setText(method.toUpperCase());
        if(USER_TYPE.equals("offline")){

            Cursor USER_CURSOR=myDatabaseHelper.query("userInformation",DbContract.CURRENT_USER_NAME);
            String solvingString="";

            if(USER_CURSOR.moveToNext()){
                SOLVING_STRING=USER_CURSOR.getString(8);

            }
        }




        ArrayList<String> listData = new ArrayList<>();

         cursor = myDatabaseHelper.showAllData("problemAndSolution");


        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "Connect to internet to update problems", Toast.LENGTH_LONG).show();

        } else {
            while (cursor.moveToNext()) {
                int PROBLEM_ID=cursor.getInt(0);
                //Toast.makeText(this,Integer.toString(PROBLEM_ID),Toast.LENGTH_SHORT).show();

                boolean show=DbContract.userSolvingString(SOLVING_STRING,PROBLEM_ID,method);
                if(show)
                listData.add(cursor.getString(0)+".     "+cursor.getString(1));



            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.problem_title_list_view, R.id.problemTitleTextListViewId, listData);

        listView.setAdapter(adapter);



    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        Object obj= getListView().getItemAtPosition(i);




       StringTokenizer st=new StringTokenizer(obj.toString(),".");
        String problemId="";

        problemId=st.nextToken();

        //Toast.makeText(getApplicationContext(),problemId,Toast.LENGTH_SHORT).show();

        Intent intent=new Intent(this,problemProfileActivity.class);
        intent.putExtra("problemId",problemId);
        intent.putExtra("method",method);
        intent.putExtra("solvingString",SOLVING_STRING);

        startActivity(intent);

    }

    public ListView getListView() {

        return listView;
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
        Button loginNavmenu;
        int logingStatus=DbContract.LOGIN_STATUS_OUT;
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);




        navigationView= this.<NavigationView>findViewById(R.id.nav_view);


        View headerView =navigationView.getHeaderView(0);
        USERNAME= headerView.<TextView>findViewById(R.id.userNameId);
        NAME= headerView.<TextView>findViewById(R.id.fullUserNameId);
        INSTITUTION= headerView.<TextView>findViewById(R.id.institutionTextId);
        EMAIL= headerView.<TextView>findViewById(R.id.userEmailProfileId);
        PHONE= headerView.<TextView>findViewById(R.id.userPhoneProfileId);
        loginNavmenu= headerView.<Button>findViewById(R.id.loginButtonNavMenuId);


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


        loginNavmenu.setOnClickListener(new View.OnClickListener() {

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
        });
    }


    //nav menu function end



}
