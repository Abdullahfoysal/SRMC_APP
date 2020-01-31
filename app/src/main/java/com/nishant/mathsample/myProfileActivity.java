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
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.nishant.mathsample.initActivity.getDatabaseHelper;


public class myProfileActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    private myProfileActivity context;
    private Toolbar toolbar;

    private GridLayout gridLayout;
    private String CURRENT_USERNAME;
    private String USER_TYPE;
    private MyDatabaseHelper myDatabaseHelper;
    private TextView name,institution,email,rank;
    private String MY_CURRENT_RANK="Not in First 30";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        context=this;
        loadAll();
    }
    private  void loadAll(){
        myDatabaseHelper=getDatabaseHelper();

        loadCurrentUserInformation();
        loadGridLayout();
        updateUserProfileInformation();

        loadNavMenu();
        setNavMenuInfo();

    }
    private void loadCurrentUserInformation(){
        CURRENT_USERNAME=getIntent().getExtras().getString("userName");
        USER_TYPE=getIntent().getExtras().getString("userType");

        //GET USER solving String
        DbContract.userRankSolvingStringFetching(this,CURRENT_USERNAME);




    }
    private void loadGridLayout(){
        myDatabaseHelper=getDatabaseHelper();

        gridLayout= this.<GridLayout>findViewById(R.id.myProfilegridLayout);
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

                        Toast.makeText(myProfileActivity.this,"Join Contest coming soon",Toast.LENGTH_SHORT).show();


                    }
                    else if(index==1){
                        //statics
                        Toast.makeText(myProfileActivity.this,"Add problem",Toast.LENGTH_SHORT).show();


                    }

                }
            });
        }
    }
    private void updateUserProfileInformation(){

        name= this.<TextView>findViewById(R.id.myProfileNameId);
        institution= this.<TextView>findViewById(R.id.myProfileInstitutionId);
        email= this.<TextView>findViewById(R.id.myProfileEmailId);
        rank= this.<TextView>findViewById(R.id.myProfileRankId);


        if(USER_TYPE.equals("offline")){


            Cursor cursor=myDatabaseHelper.query("userInformation",DbContract.CURRENT_USER_NAME);
            if(cursor.moveToNext()){
                name.setText(cursor.getString(0));
                institution.setText(cursor.getString(7));
                email.setText(cursor.getString(5));
                rank.setText(MY_CURRENT_RANK);

            }

            for(int i=0;i<DbContract.userRankList.size();i++){

                userRankStatics person=DbContract.userRankList.get(i);
                if(person.getUSERNAME().equals(DbContract.CURRENT_USER_NAME)){
                    rank.setText(person.getRANK());
                    break;
                }

            }




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
                    setNavMenuInfo();
                    Toast.makeText(context,"Refresh All",Toast.LENGTH_SHORT).show();
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
       // loginNavmenu= headerView.<Button>findViewById(R.id.loginButtonNavMenuId);


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
}
