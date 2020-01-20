package com.nishant.mathsample.introApp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nishant.mathsample.DbContract;
import com.nishant.mathsample.R;
import com.nishant.mathsample.homeActivity;
import com.nishant.mathsample.initActivity;
import com.nishant.mathsample.introApp.IntroViewPagerAdapter;
import com.nishant.mathsample.introApp.screenItem;
import com.nishant.mathsample.loginActivity;

import java.util.ArrayList;
import java.util.List;

public class introActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView skipButton;
    private TextView takeATour;

    private ViewPager screenPager;
    private IntroViewPagerAdapter introViewPagerAdapter;

    private int dotsCount;
    private ImageView[] dots;
    private LinearLayout sliderDotspanel;

    private int TAKE_A_TOUR_POSITION=-1;
    private double POSITION_OFF_SET=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        loadAll();
    }

    private void loadAll(){

        findAll();
        loadIntroPagerData();


    }

    private void findAll(){
        skipButton=findViewById(R.id.introSkipButtionId);
        skipButton.setOnClickListener(this);

        takeATour=(TextView)findViewById(R.id.introTakeATourTexviewId);
        takeATour.setOnClickListener(this);



    }
    private void loadIntroPagerData(){
        List<screenItem>mList=new ArrayList<>();

        mList.add(new screenItem("Problems","You can see All problems here",R.drawable.allproblem));
        mList.add(new screenItem("Attempted","You can see All your attempted problems here",R.drawable.attempted));
        mList.add(new screenItem("Solved","You can see All your solved problems here",R.drawable.solvedproblem));
        mList.add(new screenItem("Statics","You can see your ranking here",R.drawable.logo_topranks));
        mList.add(new screenItem("Your profile","You can see your own profile here",R.drawable.profile4));
        mList.add(new screenItem("Friends profile","You can see your friends here",R.drawable.friendsprofile));
        mList.add(new screenItem("Monitor","See your Online and offline synchronization status",R.drawable.activity));

        screenPager=findViewById(R.id.intoViewPagerId);
        introViewPagerAdapter=new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        sliderDotspanel=(LinearLayout) findViewById(R.id.slideDotsId);
        dotsCount=introViewPagerAdapter.getCount();
        dots=new ImageView[dotsCount];

        for(int i=0;i<dotsCount;i++){
            dots[i]=new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8,0,8,0);
            sliderDotspanel.addView(dots[i],params);
        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dot));

        screenPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


                if((position+1-dotsCount==0) && position+positionOffset>POSITION_OFF_SET){

                    //swift <<<<
                    startActivity(new Intent(introActivity.this, loginActivity.class));
                    finish();

                }else{
                    //swift >>>>
                }

                POSITION_OFF_SET=position+positionOffset;
            }

            @Override
            public void onPageSelected(int position) {

                for(int i=0;i<dotsCount;i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.nonactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dot));

                TAKE_A_TOUR_POSITION=position;

                if(position+1-dotsCount==0){

                    takeATour.setText(DbContract.TAKEATOUR);

                    //System.out.println("Take a TOur");
                }
                else{
                    takeATour.setText("");
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
    }

    @Override
    public void onClick(View view) {
        long id=view.getId();
        if(id==R.id.introSkipButtionId){
            Toast.makeText(this,"skip click",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this, loginActivity.class));
            finish();
        }
        else if(id==R.id.introTakeATourTexviewId && (TAKE_A_TOUR_POSITION+1-dotsCount==0)){
            Intent intent=new Intent(this, homeActivity.class);
            intent.putExtra("userName","guest");
            intent.putExtra("userType","offline");
            DbContract.CURRENT_USER_NAME="guest";
            DbContract.CURRENT_USER_LOGIN_STATUS=DbContract.LOGIN_STATUS_OUT;

            startActivity(intent);
            finish();
        }
    }
}
