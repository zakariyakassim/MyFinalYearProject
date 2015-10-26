package com.example.zakar.travelexpert;



import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PagerAdapter extends FragmentStatePagerAdapter {


    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }


    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabFragmentBus bus = new TabFragmentBus();
                return bus;
            case 1:
                TabFragmentTube tube = new TabFragmentTube();
                return tube;
            case 2:
                TabFragmentRail rail = new TabFragmentRail();
                return rail;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 0;
    }
}
