package com.example.community;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 3;
    private ViewPager2 viewPager2;
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager2 = findViewById(R.id.viewPager2);
        pagerAdapter = new MyPagerAdapter(this);
        viewPager2.setAdapter(pagerAdapter);
        viewPager2.setCurrentItem(1);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

    }
    @Override
    public void onBackPressed() {
        int middle=NUM_PAGES/2;
        if (viewPager2.getCurrentItem() == middle) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else if(viewPager2.getCurrentItem() > middle){
            // Otherwise, select the previous step.
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() - 1);
        }else{
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    }

    private  class MyPagerAdapter extends FragmentStateAdapter {

        public MyPagerAdapter( FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new ChatBox();
                case 1:
                    return new CameraFrag();
                case 2:
                    return new StoriesFrag();
                default:
                    return null;
            }
            // return null;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}