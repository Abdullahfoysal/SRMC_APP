package com.nishant.mathsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

public class userProfileActivity extends AppCompatActivity {
    private GridLayout gridLayout;
    private String CURRENT_USERNAME="";
    private String USER_TYPE="offline";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        loadAll();
    }
    private void loadAll(){
        loadGridLayout();
        loadCurrentUserInformation();


    }
    private void loadCurrentUserInformation(){
        CURRENT_USERNAME=getIntent().getExtras().getString("userName");
        USER_TYPE=getIntent().getExtras().getString("userType");

        //GET USER solving String
        DbContract.userRankSolvingStringFetching(this,CURRENT_USERNAME);




    }
    private void loadGridLayout(){
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
}
