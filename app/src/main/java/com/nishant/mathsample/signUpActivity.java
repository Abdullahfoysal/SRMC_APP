package com.nishant.mathsample;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class signUpActivity extends AppCompatActivity implements View.OnClickListener {

    private MyDatabaseHelper myDatabaseHelper;
    private TextInputEditText name,userName,password,gender,birthDate,email,phonNumber,institution;
    private Button signUp;
    private TextView loginLink;
    private String NAME,USERNAME,PASSWORD,GENDER,BIRTHDATE,EMAIL,PHONENUMBER,INSTITUTION;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private String dateBirth="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        loadAll();
        myDatabaseHelper=new MyDatabaseHelper(this);

    }

    private  void loadAll(){

        findAllField();
        DateOfBirthPicker();


    }
    private void findAllField(){

        name= this.<TextInputEditText>findViewById(R.id.input_name);
        userName= this.<TextInputEditText>findViewById(R.id.user_name);
        password= this.<TextInputEditText>findViewById(R.id.passwordId);
        gender= this.<TextInputEditText>findViewById(R.id.selectGenderId);
        birthDate= this.<TextInputEditText>findViewById(R.id.selectBirthDateId);
        email= this.<TextInputEditText>findViewById(R.id.your_email_address);
        phonNumber= this.<TextInputEditText>findViewById(R.id.phoneNumber);
        institution= this.<TextInputEditText>findViewById(R.id.selectInstitutionId);

        signUp= this.<Button>findViewById(R.id.signUpButtonId);
        loginLink=findViewById(R.id.link_login);

        signUp.setOnClickListener(this);
        loginLink.setOnClickListener(this);
       // birthDate.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(getApplicationContext(),"DATA SAVED TO LOCAL",Toast.LENGTH_SHORT).show();
        long id=view.getId();
        if(id==R.id.signUpButtonId){
            getAllFieldText();


                if(!NAME.isEmpty() && !USERNAME.isEmpty() && !PASSWORD.isEmpty() && !GENDER.isEmpty() && !BIRTHDATE.isEmpty() && !EMAIL.isEmpty() && !PHONENUMBER.isEmpty() && !INSTITUTION.isEmpty() ) {

                    if (checkNetworkConnection()) {

                        String method = "checkSignUp";
                        BackgroundTask backgroundTask = new BackgroundTask(this);
                        backgroundTask.execute(method,NAME,USERNAME,PASSWORD,GENDER,BIRTHDATE,EMAIL,PHONENUMBER,INSTITUTION);



                        }

                   else DbContract.Alert(this, "Network Connectivity info:", "Connect to Internet");
                   }

                else DbContract.Alert(this,"Fill up Error","Enter All information correctly");

        }
        else if(id==R.id.link_login){

            startActivity(new Intent(this,loginActivity.class));
            finish();
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
    private void getAllFieldText(){

        NAME=name.getText().toString().trim();
        USERNAME=userName.getText().toString().trim();
        PASSWORD=password.getText().toString().trim();
        GENDER=gender.getText().toString().trim();
        BIRTHDATE=birthDate.getText().toString().trim();
        EMAIL=email.getText().toString().trim();
        PHONENUMBER=phonNumber.getText().toString().trim();
        INSTITUTION=institution.getText().toString().trim();

    }
    public  boolean checkNetworkConnection(){

        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();

        return (networkInfo!=null && networkInfo.isConnected());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void getCalenderdata(){
       // final String dateBirth="";
        calendar=Calendar.getInstance();
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int month=calendar.get(Calendar.MONTH);
        int year=calendar.get(Calendar.YEAR);

        dateBirth=year+"-"+month+"-"+day;

        datePickerDialog=new DatePickerDialog(signUpActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int myear, int mMonth, int mDay) {
               dateBirth=myear+"-"+mMonth+"-"+mDay;
            }
        },day,month,year);

        datePickerDialog.show();

    }

    private void DateOfBirthPicker(){


        calendar=Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        birthDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(signUpActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }

    private void updateLabel() {
        String myFormat = "yy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        birthDate.setText(sdf.format(calendar.getTime()));
    }




















}
