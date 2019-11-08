package com.nishant.mathsample;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class initActivity extends AppCompatActivity implements View.OnClickListener {
    private Button initLoginButton,guestButton;
    public static MyDatabaseHelper myDatabaseHelper;
    public static SQLiteDatabase sqLiteDatabase;
    private initActivity context;
    private String preUserName="",preUserPassword="";
    private int logStatus=DbContract.LOGIN_STATUS_OUT;
    private String CURRENT_USERNAME="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
        context=this;
        //System.out.println("on initialization "+preUserName+" "+preUserPassword);

        loadAll();
    }
    private void loadAll(){
        initDatabase();
        findAllButton();
        updateServerToLocal();
        loggedUser();


    }
    private void loggedUser(){
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

        initLoginButton= this.<Button>findViewById(R.id.initLoginButtonId);
        guestButton= this.<Button>findViewById(R.id.guestLoginButtonId);

        initLoginButton.setOnClickListener(this);
        guestButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        long id=view.getId();
        if(id==R.id.initLoginButtonId){
            startActivity(new Intent(this,loginActivity.class));
            finish();
        }
        else if(id==R.id.guestLoginButtonId){
            Intent intent=new Intent(this,homeActivity.class);
            intent.putExtra("userName","guest");
            intent.putExtra("userType","offline");
            DbContract.CURRENT_USER_NAME="guest";
            DbContract.CURRENT_USER_LOGIN_STATUS=DbContract.LOGIN_STATUS_OUT;

            startActivity(intent);
            finish();
        }

    }
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
                        //exit app from this activity
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
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
