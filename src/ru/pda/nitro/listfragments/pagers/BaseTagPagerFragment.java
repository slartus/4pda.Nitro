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
import android.graphics.*;
import ru.pda.nitro.listfragments.*;
import ru.forpda.common.*;


public class BaseTagPagerFragment extends Fragment 
{
	public PagerSlidingTabStrip tabs;
	public ViewPager mViewPager;
	public PagerAdapter mPagerAdapter;
	private String tag;
	private String rubric;
	public ArrayList<String> urls;
    public String[] title = {"Все", "Android", "IOS", "WP"};
	public String currentColor = "#4973b9";

	
	
  
	//4pda.ru/games/tag/games-for-android/
	//4pda.ru/software/tag/programs-for-windows-phone-7/
	
	public void onResumePager(int position){
		FragmentLifecycle fragmentToShow = (FragmentLifecycle)mPagerAdapter.getItem(position);
		fragmentToShow.onResumeFragment();
		Log.d("onResumePager: " + position);
	}
	

	
//	protected View getTagView(LayoutInflater inflater, ViewGroup container){return null;}
	
	public ArrayList<String> getRubricTag(){
		rubric = getArguments().getString(NewsListFragment.NEWS_URL);
		tag = rubric.equals("games") ? rubric : "programs";
		Log.d("rubric: " + rubric + " tag: " + tag);
		ArrayList<String> list = new ArrayList<String>();
		list.add(rubric + "/");
		list.add(rubric + "/tag/" + tag + "-for-android/");
		list.add(rubric + "/tag/" + tag + "-for-ios/");
		list.add(rubric + "/tag/" + tag + "-for-windows-phone-7/");
		
		return list;
	}


	public class PagerAdapter extends FragmentStatePagerAdapter {
		private List<Fragment> fragments;

		public PagerAdapter(FragmentManager fm) {
			super(fm);
			this.fragments = new ArrayList<Fragment>();
			for(int i = 0; i < urls.size(); i++){
				Log.d(urls.get(i));
				fragments.add(NewsListFragment.newInstance(urls.get(i), i));
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
		public int getItemPosition(Object object)
		{
			return super.getItemPosition(object);
		}



		@Override
		public int getCount() {
			return fragments.size();
		}

	}

}
