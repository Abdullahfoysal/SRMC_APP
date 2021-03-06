package com.nishant.mathsample;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

import static com.nishant.mathsample.initActivity.getDatabase;
import static com.nishant.mathsample.initActivity.getDatabaseHelper;
import static com.nishant.mathsample.initActivity.myDatabaseHelper;

public class DbContract {
    public static int ON=1;
    public static int OFF=0;
    public static int FIRST_IMPRESSION=ON;
    public static final int SYNC_STATUS_OK=0;
    public static final int SYNC_STATUS_FAILED=1;
    public static final String SERVER_URL="https://adminsrmcapp.000webhostapp.com/android/sync.php";//no need on 15/10/19 9:54Pm
    public static final String PROBLEM_DATA_SYNC_URL="https://adminsrmcapp.000webhostapp.com/android/dataSync.php";//15/10/19->9:16 pm from phone to mysql add problem on phone
    public static final String USERDATASYNC_URL="https://adminsrmcapp.000webhostapp.com/android/userDataSync.php";//signUp for
    public static final String USER_DATA_UPDATE_URL="https://adminsrmcapp.000webhostapp.com/android/userDataUpdate.php";//user activity Update
    public static final String SERVER_URL3="http://192.168.0.117/syncdemo/dataFetch.php";//for single information such login
    public static final String SERVER_URL4="https://adminsrmcapp.000webhostapp.com/android/allDataFetching.php";//15/10/19->8:05 pm
    public static final String ALL_DATA_FETCHING_URL="https://adminsrmcapp.000webhostapp.com/android/allDataFetching.php";//15/10/19->8:05 pm
    public static final String USER_DATA_FETCHING_URL="https://adminsrmcapp.000webhostapp.com/android/userDataFetching.php";
    public static final String ALL_USER_STATICS_DATA_URL="https://adminsrmcapp.000webhostapp.com/android/userRankingDataFetching.php";//ranking of statics
    public static final String USER_RANK_SOLVING_STRING_DATA_URL="https://adminsrmcapp.000webhostapp.com/android/userRankSolvingStringFetching.php";//ranked user solvingString data
    public static final String LOGIN_DATA_FETCHING_URL="https://adminsrmcapp.000webhostapp.com/android/userLogin.php";//15/10/19->8:05 pm
    public static final String CHECK_SIGNUP_DATA_FETCHING_URL="https://adminsrmcapp.000webhostapp.com/android/checkSignUp.php";
    public static final String UI_UPDATE_BROADCAST="com.nishant.mathsample.uiupdatebroadcast";
    public static final String DATABASE_NAME="contactdb";
    public static final String DATABASE_NAME2="SRMC.db";
    public static final String TABLE_NAME="contactinfo";
    public static final String TABLE_NAME2="problemAndSolution";//work with
    public static final String NAME="name";
    public static final String PROBLEM_ID="problemId";
    public static final String SYNC_STATUS="syncstatus";
    public static final int LOGIN_STATUS_ON=0;
    public static final int LOGIN_STATUS_OUT=1;
    public static String CURRENT_USER_NAME="guest";
    public static int CURRENT_USER_LOGIN_STATUS=DbContract.LOGIN_STATUS_OUT;
    public static String response="Nothing changed";
    public static  ArrayList<userRankStatics>userRankList=new ArrayList<>();
    public static ArrayList<userRankStatics>userSolvingString=new ArrayList<>();
    public static boolean showRankingUserDataUpdated=false;
    public static final int TOTAL_PROBLEM_NUMBER=10001;
    public static int CURRENT_PROBLEM_NUMBER=0;
    //NEW USER SOLVING STRING SIZE=10,001
    public static final String NEW_USER_SOLVING_STRING="00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    public static final int NOT_TOUCHED=0;
    public static final  int SOLVED=4;
    public static final  int NOT_ABLE_SOLVED=5;
    public static String CURRENT_USER_TOTAL_SOLVED="Solved : 0";
    public static String CURRENT_USER_TOTAL_ATTEMPTED="Attempted : 0";


    public static AlertDialog alertDialog;






    public static void Alert(Context ctx,String title,String text){

        alertDialog=new AlertDialog.Builder(ctx).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();

    }

    public static String replaceChar(String str, char ch, int index) {
        StringBuilder myString = new StringBuilder(str);
        myString.setCharAt(index, ch);
        return myString.toString();
    }
    
    public static String changeTotalSolved(String totalSolved){
        int total=Integer.parseInt(totalSolved);
        total++;

        return Integer.toString(total);

    }

    public static int changeUserSolvingString(int problemId,boolean verdict){

        String solvingString="";
        String totaSolved="";


        final MyDatabaseHelper myDatabaseHelper=getDatabaseHelper();
        String USER_NAME=DbContract.CURRENT_USER_NAME;
        int syncstatus=DbContract.SYNC_STATUS_FAILED;



        Cursor USER_CURSOR=myDatabaseHelper.query("userInformation",USER_NAME);


        if(USER_CURSOR.moveToNext()){
            solvingString=USER_CURSOR.getString(8);
            totaSolved=USER_CURSOR.getString(9);
        }
            if(verdict==true){
                int r=Character.getNumericValue(solvingString.charAt(problemId));

                if(r==NOT_ABLE_SOLVED || r==SOLVED){

                    return r;
                }

                solvingString=replaceChar(solvingString,'4',problemId);

                totaSolved=changeTotalSolved(totaSolved);

                myDatabaseHelper.updateLocalDatabase(USER_NAME,syncstatus,solvingString,totaSolved);
                return 0;
            }
            else if(verdict==false){

                char ch=solvingString.charAt(problemId);
                int r=Character.getNumericValue(ch);

                if(r==NOT_ABLE_SOLVED || r==SOLVED){

                    return r;
                }

                if(r==SOLVED-1){
                    solvingString=replaceChar(solvingString,'5',problemId);//not able to solve

                    myDatabaseHelper.updateLocalDatabase(USER_NAME,syncstatus,solvingString,totaSolved);
                    return NOT_ABLE_SOLVED;

                }else {
                    r++;
                    ch=(char)(r+'0');

                    solvingString=replaceChar(solvingString,ch,problemId);

                    myDatabaseHelper.updateLocalDatabase(USER_NAME,syncstatus,solvingString,totaSolved);


                }
                return r;
            }



        return  0;


    }
    public static boolean userSolvingString(String str,int position,String method){


        int result=10;

        if(position<str.length())
         result= Character.getNumericValue(str.charAt(position));

        if(method.equals("solved")){

               if(result==SOLVED) return true;
               else return false;


        }
        else if(method.equals("attempted")){

                if(((result >NOT_TOUCHED) && (result<SOLVED)) || result==NOT_ABLE_SOLVED) return true;
                else return false;


        }
        else if(method.equals("allProblem")){

            if(result==NOT_TOUCHED ) return true;
            else return false;

        }

        return false;


    }

    public  static boolean checkNetworkConnection(Context ctx){

        ConnectivityManager connectivityManager= (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();

        return (networkInfo!=null && networkInfo.isConnected());

    }
    public static synchronized void saveToAppServer(final Context ctx,String url){

            //update userInformation to server
        Cursor cursor=null;

        if(checkNetworkConnection(ctx)) {

          //  Toast.makeText(ctx,"Network is ON",Toast.LENGTH_SHORT).show();

            final MyDatabaseHelper myDatabaseHelper=getDatabaseHelper();

            try {

                 cursor = myDatabaseHelper.showAllData("userInformation");

            }catch (Exception e){
                e.printStackTrace();
                return;
            }

            while (cursor.moveToNext()) {

                //USER INFORMATION UPDATE TO ONLINE
                final String NAME = cursor.getString(0);//
                final String USERNAME = cursor.getString(1);//Title
                final String PASSWORD= cursor.getString(2);//problemStatement
                final String GENDER = cursor.getString(3);//solution
                final String BIRTHDATE= cursor.getString(4);//Tags
                final String EMAIL = cursor.getString(5);// problem setter
                final String PHONE = cursor.getString(6);//
                final String INSTITUTION = cursor.getString(7);//
                final String SOLVINGSTRING = cursor.getString(8);//
                final String TOTALSOLVED = cursor.getString(9);//
                final int sync_status = cursor.getInt(10);

                if(sync_status==DbContract.SYNC_STATUS_FAILED){


                    StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        String Response = jsonObject.getString("response");

                                        if (Response.equals("OK")) {
                                            //Toast.makeText(ctx, USERNAME+" is saved on server", Toast.LENGTH_SHORT).show();

                                            myDatabaseHelper.updateLocalDatabase(DbContract.SYNC_STATUS_OK,USERNAME);
                                           // Toast.makeText(ctx,"Server update Successfully",Toast.LENGTH_SHORT).show();


                                        } else {
                                            //Toast.makeText(ctx, "Not saved on server", Toast.LENGTH_SHORT).show();


                                        }

                                    } catch (JSONException e) {

                                        e.printStackTrace();
                                        //Toast.makeText(ctx,"Exception occur 404 on Dbcontract",Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<>();


                            params.put("name", NAME);
                            params.put("userName",USERNAME);
                            params.put("password", PASSWORD);
                            params.put("gender", GENDER);
                            params.put("dateBirth", BIRTHDATE);
                            params.put("email", EMAIL);
                            params.put("phone", PHONE);
                            params.put("institution",INSTITUTION);
                            params.put("solvingString",SOLVINGSTRING);
                            params.put("totalSolved",TOTALSOLVED);

                            return params;
                        }
                    }
                            ;

                    MySingleton.getInstance(ctx).addToRequestQue(stringRequest);

                }




            }
            cursor.close();

        }




    }


    public static synchronized void saveFromServer(final Context ctx){ //not work for initActivity

        if(checkNetworkConnection(ctx)) {
           // System.out.println("aisse from server");
            //retrive data from json object begin
          //  final SQLiteDatabase database = myDatabaseHelper.getReadableDatabase();
            final MyDatabaseHelper myDatabaseHelper=getDatabaseHelper();

           Cursor cursor=myDatabaseHelper.showAllData("problemAndSolution");
           CURRENT_PROBLEM_NUMBER=cursor.getCount();


            String result=null;
            InputStream is=null;

            try {
                URL url = new URL(ALL_DATA_FETCHING_URL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("GET");
                is = new BufferedInputStream(con.getInputStream());
            }catch(Exception e){
                e.printStackTrace();
            }
            //read is content into a string
            try{
                BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
                StringBuilder sb=new StringBuilder();
                String line=null;
                while((line=br.readLine())!=null){

                    sb.append(line+"\n");

                }
                is.close();
                result=sb.toString();

            }catch(Exception e){
                e.printStackTrace();
            }
            //parse json data
            try{
                JSONArray ja=new JSONArray(result);
                JSONObject jo=null,joLastDateTime=null;

                for(int i=0;i<ja.length();i++){

                    //joLastDateTime=ja.getJSONObject(0);//first row of index will have last update date and time
                  //  final int lastUpdateDate=Integer.parseInt(joLastDateTime.getString("lastUpdateDate"));
                   // final int lastUpdateTime=Integer.parseInt(joLastDateTime.getString("lastUpdateTime"));

                    jo=ja.getJSONObject(i);

                    String ProblemId=jo.getString("problemId");
                    final String Title=jo.getString("title");//Title
                    final String Problem=jo.getString("problem");//problemStatement
                    final String Solution=jo.getString("solution");//solution
                    final String Tag=jo.getString("tag");//Tags
                    final String Setter=jo.getString("setter");// problem setter
                    final String updateDate=jo.getString("updateDate");//year_month_date
                   // final int updateTime=Integer.parseInt(jo.getString("updateTime"));//internation 24 formate hour only


                    final int sync_status=DbContract.SYNC_STATUS_OK;

                    if (cursor.moveToNext()){

                       // final int lastUpdateDate2=cursor.getInt(9);//lastupdateDate on local
                      //  final int lastUpdateTime2=cursor.getInt(10);//lastUpdateTime on local

                        //if((lastUpdateDate==lastUpdateDate2) && (lastUpdateTime==lastUpdateTime2) ){
                            //return "Local Data up to Date";
                         //   return;
                        //}


                        final String ProblemId2=cursor.getString(0);//problemId on local
                        final String updateDate2=cursor.getString(7);//updateDate on local
                        //final int updateTime2=cursor.getInt(8);//UpdateTime on local

                        if(updateDate.compareTo(updateDate2)>0) //updateDate>updateDate2
                            myDatabaseHelper.UpdateFromOnline(ProblemId2,Title,Problem,Solution,Tag,Setter,sync_status,updateDate);

                    }
                    else myDatabaseHelper.insertData(Title,Problem,Solution,Tag,Setter,sync_status,updateDate);



                }
            } catch(Exception e){
                e.printStackTrace();
                //Toast.makeText(ctx,"Exception Error on problem update",Toast.LENGTH_SHORT).show();
                //return "No connection is available";
            }
            cursor=myDatabaseHelper.showAllData("problemAndSolution");
            DbContract.CURRENT_PROBLEM_NUMBER=cursor.getCount();
            // return "updated Local Database";
            //Toast.makeText(ctx,"All Problem updated successfully",Toast.LENGTH_SHORT).show();

        }
//retrive data from json object end

        }

        public static synchronized void userInformationUpdateFromServer(Context ctx) {

            if (checkNetworkConnection(ctx)) {


                String method = "userDataFetching";
                BackgroundTask backgroundTask = new BackgroundTask(ctx);
                backgroundTask.execute(method);


            }
        }

        public static synchronized void allUserRankingDataFetching(Context ctx){

            if (checkNetworkConnection(ctx)) {
                String method = "allUserRankingDataFetching";
                BackgroundTask backgroundTask = new BackgroundTask(ctx);
                backgroundTask.execute(method);

            }
            else DbContract.Alert(ctx,"Network Connection","Connect to Internet");


        }

        public static synchronized void userRankSolvingStringFetching(Context ctx,String USERNAME){

                if(checkNetworkConnection(ctx)){

                    String method = "userRankSolvingStringFetching";
                    BackgroundTask backgroundTask = new BackgroundTask(ctx);
                    backgroundTask.execute(method,USERNAME);

                }
                else DbContract.Alert(ctx,"Network Connection","Connect to Internet");

        }

        public static void userSolvingResult(String solvingString){
            int solved=0,attempted=0;
            for(int i=1;i<solvingString.length();i++){

                int r=Character.getNumericValue(solvingString.charAt(i));

                if(DbContract.SOLVED==r)solved++;
                else if((DbContract.SOLVED> r && DbContract.NOT_TOUCHED <r ) || DbContract.NOT_ABLE_SOLVED==r){
                    attempted++;
                }
            }

           CURRENT_USER_TOTAL_SOLVED="Solved : "+Integer.toString(solved);
            CURRENT_USER_TOTAL_ATTEMPTED="Attempted : "+Integer.toString(attempted);
        }

        public static int nextProblemId(String method,String solvingString,int currentProblemId){
            int nextproblemNo=currentProblemId;

            if(method.equals("allProblem")){

                for(int i=currentProblemId+1;i<=CURRENT_PROBLEM_NUMBER;i++){
                    int r=Character.getNumericValue(solvingString.charAt(i));
                    if(r==NOT_TOUCHED){
                        return i;
                    }
                }
            }
            else if(method.equals("attempted")){
                for(int i=currentProblemId+1;i<=CURRENT_PROBLEM_NUMBER;i++){
                    int r=Character.getNumericValue(solvingString.charAt(i));
                    if(r==NOT_ABLE_SOLVED ||(r>NOT_TOUCHED && r<SOLVED)){
                        return i;
                    }
                }

            }
            else  if(method.equals("solved")){
                for(int i=currentProblemId+1;i<=CURRENT_PROBLEM_NUMBER;i++){
                    int r=Character.getNumericValue(solvingString.charAt(i));
                    if(r==SOLVED){
                        return i;
                    }
                }
            }




          return  nextproblemNo;
        }



        public  static String TAKEATOUR="Take A Tour";




    }


