package com.nishant.mathsample.introApp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nishant.mathsample.R;

import java.util.List;

public class IntroViewPagerAdapter extends PagerAdapter {
    private Context mcontext;
    private List<screenItem>screenItemList;
    public IntroViewPagerAdapter(Context mcontext, List<screenItem> screenItemList) {
        this.mcontext = mcontext;
        this.screenItemList = screenItemList;
    }



    @Override
    public int getCount() {
        return screenItemList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater=(LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layoutScreen=inflater.inflate(R.layout.layout_screen,null);

        ImageView imgSlide=layoutScreen.findViewById(R.id.introImageId);
        TextView title=layoutScreen.findViewById(R.id.introTitleId);
        TextView description=layoutScreen.findViewById(R.id.introDescriptionId);

        title.setText(screenItemList.get(position).getTitle());
        description.setText(screenItemList.get(position).getDescription());
        imgSlide.setImageResource(screenItemList.get(position).getScreenImage());

        container.addView(layoutScreen);

        return layoutScreen;

    }
}




















