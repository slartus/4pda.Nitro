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

import ru.pda.nitro.R;
import java.util.*;

public class NewsPagerFragment extends Fragment
{
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
    private String[] urls = {"", "news/", "software/", "games/", "reviews/"};
    private String[] title = {"Все", "Новости", "Софт", "Игры", "Обзоры"};

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.view_pager, container, false);
		mViewPager = (ViewPager) v.findViewById(R.id.pager);
		
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		final ActionBar actionBar = getActivity().getActionBar();
		mViewPager.setOffscreenPageLimit(urls.length);
		
		mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setCurrentItem(0);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				mViewPager.setCurrentItem(tab.getPosition());
				
			}

			public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
			}

			public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
			}
		};

		if(actionBar.getTabCount() > 0)
			actionBar.removeAllTabs();
		for (int i = 0; i < urls.length; i++) {
			actionBar.addTab(
				actionBar.newTab()
				.setText(title[i])
				.setTabListener(tabListener));
		}
		
			

		mViewPager.setOnPageChangeListener(
            new ViewPager.SimpleOnPageChangeListener() {

				@Override
                public void onPageSelected(int position) {
                    getActivity().getActionBar().setSelectedNavigationItem(position);
				
					FragmentLifecycle fragmentToShow = (FragmentLifecycle)mPagerAdapter.getItem(position);
					fragmentToShow.onResumeFragment();
				
                }
            });	
		
	}
	public class PagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments;

	public PagerAdapter(FragmentManager fm) {
		super(fm);
		this.fragments = new ArrayList<Fragment>();
		for(int i = 0; i < urls.length; i++){
			fragments.add(new NewsListFragment().newInstance(urls[i], i));
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
