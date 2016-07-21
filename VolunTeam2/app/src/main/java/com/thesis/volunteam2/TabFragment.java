package com.thesis.volunteam2;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TabFragment extends Fragment {

    public static TabLayout tabLayout;
    public static ViewPager viewPager;
    public static int int_items = 4 ;
    private int[] imageResId = {
            R.drawable.ic_library_books_black_24dp,
            R.drawable.ic_assignment_black_24dp,
            R.drawable.ic_notifications_black_24dp,
            R.drawable.ic_search_black_24dp
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x =  inflater.inflate(R.layout.tabdrawer,null);
        tabLayout = (TabLayout) x.findViewById(R.id.sliding_tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);
        viewPager.setAdapter(new TabFragmentAdapter(getChildFragmentManager()));
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();
            }
        });

        return x;

    }
    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(imageResId[0]);
        tabLayout.getTabAt(1).setIcon(imageResId[1]);
        tabLayout.getTabAt(2).setIcon(imageResId[2]);
        tabLayout.getTabAt(3).setIcon(imageResId[3]);
    }

    class TabFragmentAdapter extends FragmentPagerAdapter {


        public TabFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new ListFragment();
                case 1 : return new ListFragment();
                case 2 : return new ListFragment();
                case 3 : return new ListFragment();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }



        @Override
        public CharSequence getPageTitle(int position) {

            return null;
        }
    }

}


