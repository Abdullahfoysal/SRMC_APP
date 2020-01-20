package com.nishant.mathsample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextInputEditText userName,password;
    private Button loginButton;
    private TextView signUpLink;
    private String USERNAME="",PASSWORD="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadAll();


    }

    private  void loadAll(){

        findAllField();
    }

    private void findAllField(){

        userName= this.<TextInputEditText>findViewById(R.id.input_userNameId);

        password= this.<TextInputEditText>findViewById(R.id.input_passwordId);


        loginButton= this.<Button>findViewById(R.id.loginButtonId);
        signUpLink= this.<TextView>findViewById(R.id.link_signUp);

        loginButton.setOnClickListener(this);
        signUpLink.setOnClickListener(this);

    }
    public void getAllFieldText(){

        USERNAME=userName.getText().toString().trim();
        PASSWORD=password.getText().toString().trim();

    }


    @Override
    public void onClick(View view) {

        long id=view.getId();
        if(id==R.id.loginButtonId){
            getAllFieldText();


            if(!USERNAME.isEmpty() && !PASSWORD.isEmpty()){
                if(checkNetworkConnection()){

                    //DbContract.Alert(this,"Network is On","Process to login");
                    String method="login";
                    BackgroundTask backgroundTask=new BackgroundTask(this);
                    backgroundTask.execute(method,USERNAME,PASSWORD);

                }
                else {

                    DbContract.Alert(this,"Network Connectivity info:","Connect to Internet");
                }

            }
            else DbContract.Alert(this,"Fill up Error","Enter All information correctly");

        }
        else if(id==R.id.link_signUp){
            startActivity(new Intent(this,signUpActivity.class));

        }

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    public  boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();

        return (networkInfo!=null && networkInfo.isConnected());

    }
    public void onBackPressed() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Do you want to EXIT?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {

                       //startActivity(new Intent(loginActivity.this,initActivity.class));
                        loginActivity.this.finish();

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
