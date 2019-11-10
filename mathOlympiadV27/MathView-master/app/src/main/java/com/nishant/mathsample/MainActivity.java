package com.nishant.mathsample;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nishant.math.MathView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

  private DrawerLayout mDrawerLayout;
  private ActionBarDrawerToggle mtoggle;
  private MainActivity context;
  private Toolbar toolbar;

 private Button addProblemButton;


  @BindView(R.id.activity_main)
  LinearLayout linearLayout;

  @BindView(R.id.math_view)
  MathView mathView;

  @BindView(R.id.input_view)
  EditText inputView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
     ButterKnife.bind(this);
//load nav_menu
    context=this;
    loadNavMenu();


    mathView.setText("$$(a+b)^2$$");

    inputView.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }
      ///data supply here to show on
      @Override
      public void afterTextChanged(Editable s) {
        mathView.setText(inputView.getText().toString());
      }
    });


    ///16/9/2019;;8:51 pm
    addProblemButton = this.<Button>findViewById(R.id.addProblemButtonId);

    addProblemButton.setOnClickListener(this);

  }


  @Override
  public void onClick(View view) {
    if(view.getId()==R.id.addProblemButtonId){
      startActivity(new Intent(getApplicationContext(),menuActivity.class));
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

        int id=item.getItemId();
        if(id==R.id.solveProblemId){
          Toast.makeText(context,"solved problems",Toast.LENGTH_SHORT).show();
          System.out.println("Asse");
        }
        if(id==R.id.attemptedProblems){
          Toast.makeText(context,"attempted problems",Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.notificationSettingId){
          Toast.makeText(context,"Notification",Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.logOutId){
          Toast.makeText(context,"update information",Toast.LENGTH_SHORT).show();
        }
        return false;
      }
    });
  }
  //nav menu function end

}
