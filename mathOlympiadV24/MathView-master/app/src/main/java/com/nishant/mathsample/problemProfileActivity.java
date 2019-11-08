package com.nishant.mathsample;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nishant.math.MathView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nishant.mathsample.initActivity.getDatabaseHelper;


public class problemProfileActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.math_view)
    MathView mathView;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mtoggle;
    private problemProfileActivity context;
    private Toolbar toolbar;

    private MyDatabaseHelper myDatabaseHelper;
    private Cursor cursor;
    private TextView title;
    private Button submitButton;
    private EditText submittedSolutionEditText;
    private int problemId=1;
    private String CURRENT_USER="";
    private Bundle bundle;
    private String KEY="problemId";
    private String realSolution="#!?@";
    private String submittedSolution="";
    private String verdict="No submission";
    private TextView textViewverdict;
    private String activityMessage="submitted problem ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_profile);
        ButterKnife.bind(this);
        mathView.setText("$$a^2$$");
        //load nav_menu
        context=this;

        loadAll();




    }
    private void loadAll(){
        myDatabaseHelper = new MyDatabaseHelper(this);

        findId();
        receiveData();//get problemId
        loadData();
        loadUserData();
        loadNavMenu();
        setNavMenuInfo();
    }
    private void loadUserData(){
        cursor=myDatabaseHelper.query("userInformation",DbContract.CURRENT_USER_NAME);

        String solvingString="";
        if(cursor.moveToNext()){
            solvingString=cursor.getString(8);//user solviing string
        }
        char ch=solvingString.charAt(problemId);
        int result=Character.getNumericValue(ch);
        if(result==DbContract.NOT_TOUCHED){
           verdict="No submission";
        }
        else if(result==DbContract.SOLVED){
            verdict="Accepted";
            textViewverdict.setTextColor(Color.GREEN);
        }
        else if(result==DbContract.NOT_ABLE_SOLVED){
            verdict="Enough tried(-_-),"+DbContract.SOLVED+"Times";
            textViewverdict.setTextColor(Color.BLUE);
            submittedSolutionEditText.setHint("Enough Tried(-_-)");

        }
        else if(result==DbContract.SOLVED-1) {
            verdict="Wrong";
            submittedSolutionEditText.setHint("Last Chance to Solve ");
        }
        else if(result >DbContract.NOT_TOUCHED && result<DbContract.SOLVED){
            verdict="Wrong";
            textViewverdict.setTextColor(Color.RED);
            submittedSolutionEditText.setHint("You have "+Integer.toString(DbContract.SOLVED-result)+" more Chances");

        }




        textViewverdict.setText(verdict);

    }

    public void loadData() {


        cursor = myDatabaseHelper.showAllData("problemAndSolution");

        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "NO data is available in database", Toast.LENGTH_LONG).show();

        } else {

            while (cursor.moveToNext()) {
//                listData.add(cursor.getString(0)+". "+cursor.getString(2));
                if(cursor.getInt(0)==problemId){

                    realSolution=cursor.getString(3);
                    title.setText(cursor.getString(1));
                    mathView.setText(cursor.getString(2));
                    break;

                }

            }
        }




    }

    void findId(){
        title = this.<TextView>findViewById(R.id.problemTitleId);
        submitButton=findViewById(R.id.solutionSubmitButtonId);
        submittedSolutionEditText=findViewById(R.id.submittedSolutionEditTextId);
        textViewverdict= this.<TextView>findViewById(R.id.submissionVerdictId);

        submitButton.setOnClickListener(this);




    }

    void receiveData(){
        bundle = getIntent().getExtras();
         problemId=Integer.parseInt(bundle.getString(KEY));

        // Toast.makeText(this,problemId,Toast.LENGTH_LONG).show();
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

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.solutionSubmitButtonId){
            submittedSolution= submittedSolutionEditText.getText().toString().trim();
            if(submittedSolution.isEmpty())return;
            if(DbContract.CURRENT_USER_NAME.equals("guest")){
                DbContract.Alert(this,"Problem Submission:","You are Guest only\n Login To Try Problems");
                return;
            }



            if(realSolution.equals(submittedSolution)){



                Toast.makeText(this,"Accepted",Toast.LENGTH_LONG).show();

               int verdict= DbContract.changeUserSolvingString(problemId,true);

                if(verdict==DbContract.SOLVED){
                    Toast.makeText(this,"Already Accepted ",Toast.LENGTH_LONG).show();
                    DbContract.Alert(this,"Problem Verdict"," Accepted! (-_-)");
                    submittedSolutionEditText.setHint("Accepted (-_-)");
                }
               else if(verdict==DbContract.NOT_ABLE_SOLVED){
                    Toast.makeText(this,"Already Tried "+DbContract.NOT_ABLE_SOLVED+" Times",Toast.LENGTH_LONG).show();
                    DbContract.Alert(this,"Problem Verdict","You can't submit this any more time");
                    submittedSolutionEditText.setHint("Already Tried "+DbContract.SOLVED+" times ");

                }

               // DbContract.Alert(this,"Problem Verdict","Accepted");
                submittedSolutionEditText.setHint("Accepted (-_-)");

            }
            else{

                Toast.makeText(this,"Wrong",Toast.LENGTH_LONG).show();
                int verdict= DbContract.changeUserSolvingString(problemId,false);
                
                if(verdict==DbContract.SOLVED){
                    Toast.makeText(this,"Already Solved ",Toast.LENGTH_LONG).show();
                    DbContract.Alert(this,"Problem Verdict","Wrong");
                    submittedSolutionEditText.setHint("Wrong -_- ");
                }
                else if(verdict==DbContract.SOLVED-1){
                    Toast.makeText(this,"Again Wrong ",Toast.LENGTH_LONG).show();
                    DbContract.Alert(this,"Problem Verdict","Wrong\nLast Chance to Solve");
                    submittedSolutionEditText.setHint("Wrong -_-\nLast Chance to Solve ");
                }
                else if(verdict==DbContract.NOT_ABLE_SOLVED ){
                    Toast.makeText(this,"Already Tried "+DbContract.SOLVED+" Times",Toast.LENGTH_LONG).show();
                    DbContract.Alert(this,"Problem Verdict","You can't submit this any more time");
                    submittedSolutionEditText.setHint("Wrong -_-\n Already Tried "+DbContract.SOLVED+" times");
                }
                else{


                        String ms=Integer.toString(verdict);
                        Toast.makeText(this,"Wrong Answer for "+ms+" Times",Toast.LENGTH_LONG).show();
                       // DbContract.Alert(this,"Problem Verdict","Wrong for "+ms+" Times\n Try again");
                        submittedSolutionEditText.setHint("Wrong\nYou have "+Integer.toString(DbContract.SOLVED-verdict)+" more Chances");


                    }


                }

            submittedSolutionEditText.setText("");
            DbContract.saveToAppServer(this,DbContract.USER_DATA_UPDATE_URL);//userUpdate data saved to server
            DbContract.allUserRankingDataFetching(this);

            //user current submission verdict load
            loadUserData();
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
