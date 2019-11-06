package com.nishant.mathsample;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.nishant.mathsample.initActivity.getDatabaseHelper;


public class myProfileActivity extends AppCompatActivity {
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

        loadAll();
    }
    private  void loadAll(){
        myDatabaseHelper=getDatabaseHelper();

        loadCurrentUserInformation();
        loadGridLayout();
        updateUserProfileInformation();

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
}
