package com.nishant.mathsample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.nishant.mathsample.initActivity.getDatabaseHelper;

public class BackgroundTask extends AsyncTask<String,Void,String> {

    TextView problem;
    private ProgressDialog dialog;

    Context ctx;
    MyDatabaseHelper myDatabaseHelper;

    Cursor cursor=null;



    BackgroundTask(Context ctx){
        this.ctx=ctx;
        myDatabaseHelper=getDatabaseHelper();
        dialog = new ProgressDialog(ctx);

    }

    @Override
    protected synchronized void onPreExecute() {
        dialog.setMessage("Processing..wait please");
        if(ctx!=null)
        dialog.show();
    }

    @Override
    protected synchronized String doInBackground(String ... params) {
        String PROBLEM_SYNC_URL=DbContract.PROBLEM_DATA_SYNC_URL;
        String login_url=DbContract.LOGIN_DATA_FETCHING_URL;//userLogin.php
        String allDataFetchingUrl=DbContract.ALL_DATA_FETCHING_URL;
        String userDataFetching_URL=DbContract.USER_DATA_FETCHING_URL;



        String method=params[0];

        if(method.equals("saveOnline")){//not using this 19/10/19 11:12 PM
            String Title=params[1];
            String Problem=params[2];
            String Solution=params[3];
            String Tag=params[4];
            String Setter=params[5];

            try {
                URL url=new URL(PROBLEM_SYNC_URL);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data= URLEncoder.encode("title","UTF-8")+"="+URLEncoder.encode(Title,"UTF-8")+"&"+
                        URLEncoder.encode("problem","UTF-8")+"="+URLEncoder.encode(Problem,"UTF-8")+"&"+
                        URLEncoder.encode("solution","UTF-8")+"="+URLEncoder.encode(Solution,"UTF-8")+"&"+
                        URLEncoder.encode("tag","UTF-8")+"="+URLEncoder.encode(Tag,"UTF-8")+"&"+
                        URLEncoder.encode("setter","UTF-8")+"="+URLEncoder.encode(Setter,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                InputStream IS=httpURLConnection.getInputStream();
                IS.close();

                return "Data Saved to server";
            }catch (MalformedURLException e){
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(method.equals("login")){




            //login only check from server
            String USERNAME=params[1];
            String PASSWORD=params[2];




            try {

                URL url=new URL(DbContract.LOGIN_DATA_FETCHING_URL);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data=
                        URLEncoder.encode("userName","UTF-8")+"="+URLEncoder.encode(USERNAME,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(PASSWORD,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

                String response="";
                String line="";

                if((line=bufferedReader.readLine())!=null){
                    response=line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                //change activity after login
                if(response.equals("OK")){
                    if(dialog.isShowing())dialog.dismiss();

                    DbContract.CURRENT_USER_NAME=USERNAME;
                    DbContract.CURRENT_USER_LOGIN_STATUS=DbContract.LOGIN_STATUS_ON;

                    Intent intent = new Intent (ctx, homeActivity.class);
                    intent.putExtra("user",USERNAME);
                    intent.putExtra("userType","offline");
                    ctx.startActivity(intent);

                    ((Activity)ctx).finish();
                    return "Login successfully";
                }
                else if(response.equals("FAILED")){
                    return "LoginFailed";
                }
               // else DbContract.Alert(ctx,"Login information","Enter correct username & password");

                return "something wrong in login";

            }catch (MalformedURLException e){
                e.printStackTrace();
                return "Error";
            }
            catch (IOException e){
                e.printStackTrace();
                return "Error";
            }


        }
        else if(method.equals("checkSignUp")){

            String NAME=params[1];
            String USERNAME=params[2];
            String PASSWORD=params[3];

            String GENDER=params[4];
            String DATEBIRTH=params[5];
            String EMAIL=params[6];
            String PHONE=params[7];
            String INSTITUTION=params[8];
            String SOLVINGSTRING=DbContract.NEW_USER_SOLVING_STRING;
            String TOTALSOLVED="0";







            try {

                URL url=new URL(DbContract.CHECK_SIGNUP_DATA_FETCHING_URL);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data=
                        URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(NAME,"UTF-8")+"&"+
                        URLEncoder.encode("userName","UTF-8")+"="+URLEncoder.encode(USERNAME,"UTF-8")+"&"+
                                URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(PASSWORD,"UTF-8")+"&"+
                                URLEncoder.encode("gender","UTF-8")+"="+URLEncoder.encode(GENDER,"UTF-8")+"&"+
                                URLEncoder.encode("dateBirth","UTF-8")+"="+URLEncoder.encode(DATEBIRTH,"UTF-8")+"&"+
                                URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(EMAIL,"UTF-8")+"&"+
                                URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(PHONE,"UTF-8")+"&"+
                                URLEncoder.encode("institution","UTF-8")+"="+URLEncoder.encode(INSTITUTION,"UTF-8")+"&"+
                                URLEncoder.encode("solvingString","UTF-8")+"="+URLEncoder.encode(SOLVINGSTRING,"UTF-8")+"&"+
                                URLEncoder.encode("totalSolved","UTF-8")+"="+URLEncoder.encode(TOTALSOLVED,"UTF-8");


                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

                String response="";
                String line="";

                if((line=bufferedReader.readLine())!=null){
                    response=line;

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                //change activity after login
                if(response.equals("OK")){
                    if(dialog.isShowing())dialog.dismiss();

                    Intent intent = new Intent (ctx, loginActivity.class);
                    intent.putExtra("userName",USERNAME);
                    intent.putExtra("userType","offline");
                     ctx.startActivity(intent);

                    ((Activity)ctx).finish();

                    return "SIGNUP successfully";
                }
                else if(response.equals("MATCHED")){

                    return "signUpAlready";
                }
                else if(response.equals("FAILED")){
                    return "signUpFailed";
                }

                // else DbContract.Alert(ctx,"Login information","Enter correct username & password");

                return "something wrong in SIGNUP";

            }catch (MalformedURLException e){
                e.printStackTrace();
                return "Error";
            }
            catch (IOException e){
                e.printStackTrace();
                return "Error";
            }





              //  return  "something wrong in signUp ";


        }
        else if(method.equals("saveFromServer")){

            cursor=myDatabaseHelper.showAllData("problemAndSolution");
            //retrive data from json object begin



                String result=null;
                InputStream is=null;

                try {
                    URL url = new URL(allDataFetchingUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setDoOutput(true);
                    con.connect();
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

                        joLastDateTime=ja.getJSONObject(0);//first row of index will have last update date and time
                        final int lastUpdateDate=Integer.parseInt(joLastDateTime.getString("lastUpdateDate"));
                        final int lastUpdateTime=Integer.parseInt(joLastDateTime.getString("lastUpdateTime"));

                        jo=ja.getJSONObject(i);

                        String ProblemId=jo.getString("problemId");
                        final String Title=jo.getString("title");//Title
                        final String Problem=jo.getString("problem");//problemStatement
                        final String Solution=jo.getString("solution");//solution
                        final String Tag=jo.getString("tag");//Tags
                        final String Setter=jo.getString("setter");// problem setter
                        final int updateDate=Integer.parseInt(jo.getString("updateDate"));//year_month_date
                        final int updateTime=Integer.parseInt(jo.getString("updateTime"));//internation 24 formate hour only


                        final int sync_status=DbContract.SYNC_STATUS_OK;

                       if (cursor.moveToNext()){

                           final int lastUpdateDate2=cursor.getInt(9);//lastupdateDate on local
                           final int lastUpdateTime2=cursor.getInt(10);//lastUpdateTime on local

                           if((lastUpdateDate==lastUpdateDate2) && (lastUpdateTime==lastUpdateTime2) ){
                               return "All problem up to Date";
                           }


                           final String ProblemId2=cursor.getString(0);//problemId on local
                           final int updateDate2=cursor.getInt(7);//updateDate on local
                           final int updateTime2=cursor.getInt(8);//UpdateTime on local

                               if((updateDate>updateDate2) || ((updateDate==updateDate2) && (updateTime>updateTime2)) )
                               myDatabaseHelper.UpdateFromOnline(ProblemId2,Title,Problem,Solution,Tag,Setter,sync_status,updateDate,updateTime,lastUpdateDate,lastUpdateTime);

                        }
                       else myDatabaseHelper.insertData(Title,Problem,Solution,Tag,Setter,sync_status,updateDate,updateTime,lastUpdateDate,lastUpdateTime);



                    }
                } catch(Exception e){
                    e.printStackTrace();
                    return "No connection is available";
                }

                return "updated Local Database";

            //retrive data from json object end
        }
        else if(method.equals("userDataFetching")){
            //retrive data from json object begin


            cursor =myDatabaseHelper.query("userInformation",DbContract.CURRENT_USER_NAME);

            String currentUserName=DbContract.CURRENT_USER_NAME;

            String result=null;
            InputStream is=null;

            try {

                URL url = new URL(userDataFetching_URL);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data=URLEncoder.encode("userName","UTF-8")+"="+URLEncoder.encode(currentUserName,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                is = new BufferedInputStream(httpURLConnection.getInputStream());


            }catch(Exception e){
                e.printStackTrace();
            }
            //read is content into a string
            try{
                BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
                StringBuilder sb=new StringBuilder();
                String line=null;
                while((line=br.readLine())!=null){

                    if(line.equals("notFound")){
                        return "You are not Registered user";
                    }

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

                    jo=ja.getJSONObject(i);

                    final String NAME=jo.getString("name");
                    final String USERNAME=jo.getString("userName");//Title
                    final String PASSWORD=jo.getString("password");//problemStatement
                    final String GENDER=jo.getString("gender");//solution
                    final String DATEBIRTH=jo.getString("dateBirth");//Tags
                    final String EMAIL=jo.getString("email");// problem setter
                    final String PHONE=jo.getString("phone");
                    final String INSTITUTION=jo.getString("institution");
                    final String SOLVINGSTRING=jo.getString("solvingString");
                    final String TOTALSOLVED=jo.getString("totalSolved");
                    int syncstatus=DbContract.SYNC_STATUS_OK;

                    if (cursor.moveToNext())
                    myDatabaseHelper.UpdateFromOnline(NAME,USERNAME,PASSWORD,GENDER,DATEBIRTH,EMAIL,PHONE,INSTITUTION,SOLVINGSTRING,TOTALSOLVED,syncstatus,DbContract.LOGIN_STATUS_ON);
                    else {
                        long rowId=myDatabaseHelper.insertData(NAME,USERNAME,PASSWORD,GENDER,DATEBIRTH,EMAIL,PHONE,INSTITUTION,SOLVINGSTRING,TOTALSOLVED,syncstatus,DbContract.LOGIN_STATUS_ON);

                        if(rowId==-1)return "notSaved userInformation";
                    }




                }
            } catch(Exception e){
                e.printStackTrace();
                return "No connection is available";
            }

            return "updated userInformation on Local";

            //retrive data from json object end

        }


        else if(method.equals("allUserRankingDataFetching")){//for statics of Ranking

            ArrayList<userRankStatics> arrayList=new ArrayList<>();


            String result=null;
            InputStream is=null;

            try {
                URL url = new URL(DbContract.ALL_USER_STATICS_DATA_URL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("GET");
                is = new BufferedInputStream(con.getInputStream());
            }catch(Exception e){
                e.printStackTrace();
                return " connection Exception,No connection is available";
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
                return "buffer reader exception,No connection is available";
            }
            //parse json data
            try {
                JSONArray ja = new JSONArray(result);
                JSONObject jo = null;

                for (int i = 0; i < ja.length(); i++){

                    jo = ja.getJSONObject(i);

                    final String NAME = jo.getString("name");
                    final String USERNAME=jo.getString("userName");
                    final String EMAIL=jo.getString("email");
                    final String INSTITUTION=jo.getString("institution");
                    final String TOTAL_SOLVED=jo.getString("totalSolved");

                   userRankStatics person=new userRankStatics();

                        person.setRANK(Integer.toString(i+1));
                        person.setNAME(NAME);
                        person.setUSERNAME(USERNAME);
                        person.setINSTITUTION(INSTITUTION);
                        person.setEMAIL(EMAIL);
                        person.setSOLVED(TOTAL_SOLVED);

                        arrayList.add(person);



                }

                DbContract.userRankList=arrayList;



            } catch(Exception e){
            e.printStackTrace();
            return " json exception,No connection is available";
        }

            return "SUCESSFULLY GET RANKING";

        }
        else if(method.equals("userRankSolvingStringFetching")){

            ArrayList<userRankStatics> arrayList=new ArrayList<>();

            String USERNAME=params[1];
            String result=null;
            InputStream is=null;

            try {

                URL url = new URL(DbContract.USER_RANK_SOLVING_STRING_DATA_URL);
                HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data=URLEncoder.encode("userName","UTF-8")+"="+URLEncoder.encode(USERNAME,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                is = new BufferedInputStream(httpURLConnection.getInputStream());


            }catch(Exception e){
                e.printStackTrace();
            }
            //read is content into a string
            try{
                BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
                StringBuilder sb=new StringBuilder();
                String line=null;
                while((line=br.readLine())!=null){

                    if(line.equals("notFound")){
                        return "You are not Registered user";
                    }

                    sb.append(line+"\n");

                }
                is.close();
                result=sb.toString();

            }catch(Exception e){
                e.printStackTrace();
                return "No connection is available";
            }
            //parse json data
            try{
                JSONArray ja=new JSONArray(result);
                JSONObject jo=null;

                for(int i=0;i<ja.length();i++){

                    jo=ja.getJSONObject(i);

                    final String SOLVINGSTRING=jo.getString("solvingString");

                  userRankStatics person=new userRankStatics();

                  person.setSOLVING_STRING(SOLVINGSTRING);
                    arrayList.add(person);

                    DbContract.userSolvingString=arrayList;

//                    System.out.append(SOLVINGSTRING+" FROM Dbcontract to backgrouondTask");



                }
            } catch(Exception e){
                e.printStackTrace();
                return "No connection is available";
            }
            DbContract.showRankingUserDataUpdated=true;
            return "Updated information of user";

            //retrive data from json object end
        }



        return "Server is switched off";
    }

    @Override
    protected synchronized void onPostExecute(String result) {
        if(dialog.isShowing())dialog.dismiss();


        switch (result){
            case "Error":
                DbContract.Alert(ctx,"Activity Information","Something went wrong\nTry again");
                break;
            case "LoginFailed":
                DbContract.Alert(ctx,"Activity Information","Incorrect user name or password\nTry again");
                break;
            case "signUpAlready":
                DbContract.Alert(ctx,"Activity Information","This user name is already taken\nTry another");
                break;

                case "signUpFailed":
                DbContract.Alert(ctx,"Activity Information","Something went wrong on SignUp\nTry again");
                break;



        }
        Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
    }




}
