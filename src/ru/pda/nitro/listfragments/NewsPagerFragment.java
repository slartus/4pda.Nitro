package ru.pda.nitro.listfragments;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.pda.nitro.R;

public class NewsPagerFragment extends Fragment
{
	private ViewPager mViewPager;
	private PagerAdapter mPagerAdapter;
    private String[] urls = {"", "news/", "software/", "games/", "reviews/"};
    private String[] title = {"Все", "Новости", "Софт", "Игры", "Обзоры"};

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		// TODO: Implement this method
		View v = inflater.inflate(R.layout.news_list_pager, container, false);
		mViewPager = (ViewPager) v.findViewById(R.id.pager);
		
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onActivityCreated(savedInstanceState);
		final ActionBar actionBar = getActivity().getActionBar();
		mViewPager.setOffscreenPageLimit(urls.length);
		
		mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create a tab listener that is called when the user changes tabs.
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				// show the given tab
				mViewPager.setCurrentItem(tab.getPosition());
				
			}

			public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
				// hide the given tab
			}

			public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
				// probably ignore this event
			}
		};

		// Add 3 tabs, specifying the tab's text and TabListener
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
                    // When swiping between pages, select the
                    // corresponding tab.
                    getActivity().getActionBar().setSelectedNavigationItem(position);
                }
            });
			
			mViewPager.setCurrentItem(0);
		
	}
	public class PagerAdapter extends FragmentPagerAdapter {
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
      return new NewsListFragment().newInstance(urls[i]);
    }

    @Override
    public int getCount() {
        return urls.length;
    }

}

	
}
