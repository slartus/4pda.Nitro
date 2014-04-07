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
import android.widget.*;
import ru.forpda.common.*;
import android.content.*;
import android.view.*;


public class NewsSpinnerFragment extends Fragment implements FragmentLifecycle, ActionBar.OnNavigationListener
{
    private String[] urls = {"", "news/", "software", "games", "reviews/"};
    private String[] title = {"Все", "Новости", "Софт", "Игры", "Обзоры"};
	private ActionBar bar;
	private Fragment fragment;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.news_view_spinner, container, false);
		return v;
	}

	@Override
	public void onResumeFragment()
	{
		onResumePager();
	}
	//4pda.ru/software/tag/programs-for-windows-phone-7/
	private void onResumePager(){
		FragmentLifecycle fragmentToShow = (FragmentLifecycle)fragment;
		fragmentToShow.onResumeFragment();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		bar = getActivity().getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		Context context = new ContextThemeWrapper(getActivity(),android.R.style.Theme_Holo);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, title);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		bar.setListNavigationCallbacks(adapter, this);
		setContent(0);
		
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
	setContent(itemPosition);
		return false;
	}
	
	private void setContent(int position){
		fragment = getItemFragment(position);
		getActivity().getSupportFragmentManager().beginTransaction()
		.replace(R.id.news_contenr, fragment)
		.commit();
	}
	
	private Fragment getItemFragment(int position){
		switch(position){
			case 0:
				return NewsListFragment.newInstance(urls[0], 0);
			case 1:
				return NewsListFragment.newInstance(urls[1], 0);
			case 2:
				return SoftTagPagerFragment.newInstance(urls[2]);
			case 3:
				return GamesTagPagerFragment.newInstance(urls[3]);
			case 4:
				return NewsListFragment.newInstance(urls[4], 0);
		}
		return null;
	}

}
