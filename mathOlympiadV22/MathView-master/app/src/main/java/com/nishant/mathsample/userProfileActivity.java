package com.nishant.mathsample;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.nishant.mathsample.initActivity.getDatabaseHelper;

public class userProfileActivity extends AppCompatActivity {
    //nav menu begin
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    private userProfileActivity context;
    private Toolbar toolbar;
    //nav end
    private MyDatabaseHelper myDatabaseHelper;
    //profile date
    private TextView name,institution,email,rank;
    private GridLayout gridLayout;
    private String CURRENT_USERNAME="";
    private String USER_TYPE="offline";
    private int USER_INDEX=0;
    private String MY_CURRENT_RANK="Not in First 30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        context=this;

        loadAll();
    }
    private void loadAll(){
        myDatabaseHelper=getDatabaseHelper();

        loadGridLayout();
        loadCurrentUserInformation();
        updateUserProfileInformation();
        loadNavMenu();
        setNavMenuInfo();


    }
    private void updateUserProfileInformation(){

        name= this.<TextView>findViewById(R.id.userProfileNameId);
        institution= this.<TextView>findViewById(R.id.userProfileInstitutionId);
        email= this.<TextView>findViewById(R.id.userProfileEmailId);
        rank= this.<TextView>findViewById(R.id.userProfileRankId);

        if(USER_TYPE.equals("online")){

            userRankStatics person=DbContract.userRankList.get(USER_INDEX);
            name.setText(person.getNAME());
            institution.setText(person.getINSTITUTION());
            email.setText(person.getEMAIL());
            rank.setText(person.getRANK());
        }
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
    private void loadCurrentUserInformation(){
        CURRENT_USERNAME=getIntent().getExtras().getString("userName");
        USER_TYPE=getIntent().getExtras().getString("userType");
        USER_INDEX=getIntent().getExtras().getInt("userIndex");



    }
    private void loadGridLayout(){
        myDatabaseHelper=getDatabaseHelper();

        gridLayout= this.<GridLayout>findViewById(R.id.userProfilegridLayout);
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
                        Toast.makeText(userProfileActivity.this,"Attempted problems",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(userProfileActivity.this,problemActivity.class);
                        intent.putExtra("method","attempted");
                        intent.putExtra("userType",USER_TYPE);
                        startActivity(intent);

                    }
                    else if(index==1){
                        //statics
                        Toast.makeText(userProfileActivity.this,"Solved problems",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(userProfileActivity.this,problemActivity.class);
                        intent.putExtra("method","solved");
                        intent.putExtra("userType",USER_TYPE);
                        startActivity(intent);

                    }

                }
            });
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
        else {
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

    //nav info update
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
}
