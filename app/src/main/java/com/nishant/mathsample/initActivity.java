package com.nishant.mathsample;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nishant.mathsample.introApp.introActivity;

public class initActivity extends AppCompatActivity {

    //    private pl.droidsonroids.gif.GifImageView gifImageView;

    private Button initLoginButton;
    public static MyDatabaseHelper myDatabaseHelper;
    public static SQLiteDatabase sqLiteDatabase;
    private initActivity context;
    private String preUserName="",preUserPassword="";
    private int logStatus=DbContract.LOGIN_STATUS_OUT;
    private String CURRENT_USERNAME="";

    private int SPLASH_TIME_OUT=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        context=this;
        //splash screen

        //splash screen
        Initialization();



    }
    private void Initialization(){

        loadAll();//database
        splashScreen();

    }
    private void splashScreen(){
        ((ImageView) findViewById(R.id.imageView2)).startAnimation(AnimationUtils.loadAnimation(this,R.anim.test));
//        ((ImageView) findViewById(R.id.imageView2)).startAnimation(AnimationUtils.loadAnimation(this,R.anim.mytransition));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               /* Intent homeIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homeIntent);
                finish();*/
               loggedUser();
               newUser();
            }
        },SPLASH_TIME_OUT);
    }
    private void loadAll(){

        DbContract.FIRST_IMPRESSION=DbContract.ON;
        initDatabase();
       // findAllButton();
       // updateServerToLocal();

    }
    private void newUser(){
        if(logStatus==DbContract.LOGIN_STATUS_OUT){

            startActivity(new Intent(this, introActivity.class));
            finish();

        }
    }
    private void loggedUser(){

        Cursor problemCountCursor=myDatabaseHelper.showAllData("problemAndSolution");
        DbContract.CURRENT_PROBLEM_NUMBER=problemCountCursor.getCount();

        Cursor cursor =myDatabaseHelper.showAllData("userInformation");

        while (cursor.moveToNext()){
            logStatus=cursor.getInt(11);
            CURRENT_USERNAME=cursor.getString(1);

            if(logStatus==DbContract.LOGIN_STATUS_ON){

                DbContract.CURRENT_USER_NAME=CURRENT_USERNAME;
                DbContract.CURRENT_USER_LOGIN_STATUS=DbContract.LOGIN_STATUS_ON;
                Intent intent = new Intent (this, homeActivity.class);
                intent.putExtra("userName",CURRENT_USERNAME);
                intent.putExtra("userType","offline");
                this.startActivity(intent);
                finish();
            }
        }
    }
    private void updateServerToLocal(){

        //problemAndSolution saveFromServer thread

        new Thread(){
            @Override
            public void run() {
                try {

                   // DbContract.saveFromServer(context);

                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        }.start();

         //DbContract.saveToAppServer(this,DbContract.USER_DATA_UPDATE_URL);//userInformation for signUp information update to server


    }
    private void initDatabase(){

            //database init 1

            myDatabaseHelper=new MyDatabaseHelper(this);
            try {

                 sqLiteDatabase =myDatabaseHelper.getWritableDatabase();

            }catch (Exception e){

            }

    }
    private void findAllButton(){

       /* initLoginButton= this.<Button>findViewById(R.id.initLoginButtonId);
       // guestButton= this.<Button>findViewById(R.id.guestLoginButtonId);
       // gifImageView=findViewById(R.id.initGifId);

        initLoginButton.setOnClickListener(this);
       // guestButton.setOnClickListener(this);
       // gifImageView.setOnClickListener(this);*/
    }

   /* @Override
    public void onClick(View view) {
        long id=view.getId();
        if(id==R.id.initLoginButtonId){
            startActivity(new Intent(this,loginActivity.class));
            finish();
        }
       *//* else if(id==R.id.guestLoginButtonId){
           *//**//* Intent intent=new Intent(this,homeActivity.class);
            intent.putExtra("userName","guest");
            intent.putExtra("userType","offline");
            DbContract.CURRENT_USER_NAME="guest";
            DbContract.CURRENT_USER_LOGIN_STATUS=DbContract.LOGIN_STATUS_OUT;

            startActivity(intent);
            finish();*//**//*
        }*//*

      *//*  if(view.getId()==R.id.initGifId){
            Toast.makeText(this,"touched gif",Toast.LENGTH_SHORT).show();
            int gf=currentGifNo%totalGifNo;
            if(gf==0)
            gifImageView.setImageResource(R.drawable.d2);
            else if(gf==1) gifImageView.setImageResource(R.drawable.d3);
            else if(gf==2) gifImageView.setImageResource(R.drawable.d1);

            currentGifNo++;
        }*//*

    }*/
    public static SQLiteDatabase getDatabase(){

        return sqLiteDatabase;
    }
    public static MyDatabaseHelper getDatabaseHelper(){

        return  myDatabaseHelper;
    }

    @Override
    public void onBackPressed() {

        //Exit application begin code
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        initActivity.this.finish();
                        System.exit(0);
                        //exit end
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
        //Exit application end code
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    /*@Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
       *//* boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
        double myDouble = savedInstanceState.getDouble("myDouble");
        int myInt = savedInstanceState.getInt("MyInt");*//*
         preUserName = savedInstanceState.getString("userName");
         preUserPassword=savedInstanceState.getString("password");
    }*/

}
