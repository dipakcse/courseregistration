package com.example.assignment4;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PageAdapter extends FragmentStatePagerAdapter {
    int countTab;
    public PageAdapter(FragmentManager fm, int countTab) {
        super(fm);
        this.countTab = countTab;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                AvailableCourseTab availableCourseTab = new AvailableCourseTab();
                return availableCourseTab;
            case 1:
                RegisteredCourseTab registeredCourseTab = new RegisteredCourseTab();
                return registeredCourseTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return countTab;
    }
}
