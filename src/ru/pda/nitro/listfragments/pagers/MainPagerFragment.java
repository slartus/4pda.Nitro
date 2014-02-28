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
import ru.pda.nitro.bricks.*;
import android.content.*;
import android.preference.*;
import android.widget.*;
import ru.pda.nitro.listfragments.*;


public class MainPagerFragment extends Fragment implements FragmentLifecycle
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
		setHasOptionsMenu(true);
		setRetainInstance(true);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

		list = BricksList.getBricks(prefs);
		mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOffscreenPageLimit(list.size());
		setPageView(getPosition());
		
		mViewPager.setOnPageChangeListener(
            new ViewPager.SimpleOnPageChangeListener() {

				@Override
                public void onPageSelected(int position)
				{
					setTitle(position);
					onResumePager(position);                }
            });	
	}
	
	@Override
	public void onResumeFragment()
	{
		onResumePager(mViewPager.getCurrentItem());
	}
	
	private void onResumePager(int position){
		FragmentLifecycle fragmentToShow = (FragmentLifecycle)mPagerAdapter.getItem(position);
		fragmentToShow.onResumeFragment();
	}
	
	private void setTitle(int position)
	{
		BrickInfo item = list.get(position);
		BaseState.setMTitle(item.getTitle());
		getActivity().getActionBar().setTitle(item.getTitle());
	}

	private int getPosition()
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		String favorite = prefs.getString("mainFavorite_", "favorites");
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i).getName().equals(favorite))
			{
				setTitle(i);
				return i;
			}
		}
		return 1;
	}

	public void setPageView(int position)
	{
		mViewPager.setCurrentItem(position, false);
	}

	public class PagerAdapter extends FragmentStatePagerAdapter
	{
		private List<Fragment> fragments;

		public PagerAdapter(FragmentManager fm)
		{
			super(fm);
			this.fragments = new ArrayList<Fragment>();
			for (BrickInfo info : list)
			{
				fragments.add(info.createFragment());
			}
		}

		@Override
		public Fragment getItem(int i)
		{
			return fragments.get(i);
		}

		@Override
		public int getCount()
		{
			return fragments.size();
		}
	}

}
