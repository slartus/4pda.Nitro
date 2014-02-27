package ru.pda.nitro.listfragments.pagers;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.pda.nitro.R;
import ru.pda.nitro.*;

import com.astuetz.PagerSlidingTabStrip;
import android.util.*;
import android.graphics.*;
import ru.pda.nitro.listfragments.*;


public class NewsPagerFragment extends Fragment implements FragmentLifecycle
{
	private PagerSlidingTabStrip tabs;
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
    private String[] urls = {"", "news/", "software/", "games/", "reviews/"};
    private String[] title = {"Все", "Новости", "Софт", "Игры", "Обзоры"};
	private String currentColor = "#4973b9";
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.news_view_pager, container, false);
		tabs = (PagerSlidingTabStrip) v.findViewById(R.id.tabs);
		mViewPager = (ViewPager) v.findViewById(R.id.pager);
		return v;
	}
	
	@Override
	public void onResumeFragment()
	{
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	
		mViewPager.setOffscreenPageLimit(urls.length);
		
		mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
	
		tabs.setViewPager(mViewPager);
		
		tabs.setIndicatorHeight(6);
		tabs.setOnPageChangeListener(
            new ViewPager.SimpleOnPageChangeListener() {
			
				@Override
                public void onPageSelected(int position) {
                    
					FragmentLifecycle fragmentToShow = (FragmentLifecycle)mPagerAdapter.getItem(position);
					fragmentToShow.onResumeFragment();
				
                }
            });	
	
		tabs.setIndicatorColor(Color.parseColor(currentColor));
		
		
	}
	
	public class PagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;

	public PagerAdapter(FragmentManager fm) {
		super(fm);
		this.fragments = new ArrayList<Fragment>();
		for(int i = 0; i < urls.length; i++){
            fragments.add(NewsListFragment.newInstance(urls[i], i));
        }
	}
	
		@Override
		public CharSequence getPageTitle(int position) {
			return BaseState.getSpannable(getActivity(), title[position]);
		}
		

    @Override
    public Fragment getItem(int i) {
      return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
	
}
