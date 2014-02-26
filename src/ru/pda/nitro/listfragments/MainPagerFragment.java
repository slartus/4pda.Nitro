package ru.pda.nitro.listfragments;

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
import ru.pda.nitro.bricks.*;
import android.content.*;
import android.preference.*;


public class MainPagerFragment extends Fragment
{
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
	private ArrayList<BrickInfo> list;
   	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.main_view_pager, container, false);
		mViewPager = (ViewPager) v.findViewById(R.id.main_pager);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		
		list = BricksList.getBricks(prefs);
		mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOffscreenPageLimit(list.size());
		
		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
															   .getDisplayMetrics());
		mViewPager.setPageMargin(pageMargin);

		mViewPager.setOnPageChangeListener(
            new ViewPager.SimpleOnPageChangeListener() {

				@Override
                public void onPageSelected(int position) {
					BrickInfo item = list.get(position);
					BaseState.setMTitle(item.getTitle());
					getActivity().getActionBar().setTitle(item.getTitle());
					
					FragmentLifecycle fragmentToShow = (FragmentLifecycle)mPagerAdapter.getItem(position);
					fragmentToShow.onResumeFragment();
					
                }
            });	

	}
	
	public void setPageView(int position)
	{
		mViewPager.setCurrentItem(position);
	}

	public class PagerAdapter extends FragmentStatePagerAdapter {
		private List<Fragment> fragments;

		public PagerAdapter(FragmentManager fm) {
			super(fm);
			this.fragments = new ArrayList<Fragment>();
				for(BrickInfo info : list){
				fragments.add(info.createFragment());
			}
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
