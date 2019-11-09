package com.nishant.mathsample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class customAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private ArrayList<userRankStatics>userRankList;

   public customAdapter(Context context){


       this.context=context;
       userRankList=DbContract.userRankList;

    }
    @Override
    public int getCount() {

        return userRankList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View converview, ViewGroup parent) {

        if(converview==null){
            inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            converview=inflater.inflate(R.layout.statics_rank_list_view_layout,parent,false);

        }
        TextView rankNo=converview.findViewById(R.id.userRankNoId);
        TextView name=converview.findViewById(R.id.userNameId);
        TextView institution=converview.findViewById(R.id.userInstitutionId);
        TextView totalSolved=converview.findViewById(R.id.userTotalProblemSolvedId);
       // TextView status=converview.findViewById(R.id.userCurrentStatusId);

        userRankStatics person=userRankList.get(position);

        String NAME=person.getNAME();
        String INSTITUTION=person.getINSTITUTION();
        String TOTOLSOLVED=person.getSOLVED();

       // System.out.println(NAME+" "+INSTITUTION+" "+TOTOLSOLVED+"   DbContract on");

       rankNo.setText(Integer.toString(position+1));
       name.setText(NAME);
       institution.setText(INSTITUTION);
       totalSolved.setText(TOTOLSOLVED);
      // status.setText(STATUS);


        return converview;
    }
}
