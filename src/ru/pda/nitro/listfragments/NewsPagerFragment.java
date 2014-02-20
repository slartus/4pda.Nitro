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


public class NewsPagerFragment extends Fragment
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
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	//	final ActionBar actionBar = getActivity().getActionBar();
		mViewPager.setOffscreenPageLimit(urls.length);
		
		mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);
	//	mViewPager.setCurrentItem(0);
		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
															   .getDisplayMetrics());
		mViewPager.setPageMargin(pageMargin);

		tabs.setViewPager(mViewPager);
	/*	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
				.setText(BaseState.getSpannable(getActivity(), title[i]))
				.setTabListener(tabListener));
		}*/
		
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
/*

public class MainActivity extends FragmentActivity {

	private final Handler handler = new Handler();

		private ViewPager pager;
	private MyPagerAdapter adapter;

	private Drawable oldBackground = null;
	private int currentColor = 0xFF666666;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

			pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);

		changeColor(currentColor);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.action_contact:
			QuickContactFragment dialog = new QuickContactFragment();
			dialog.show(getSupportFragmentManager(), "QuickContactFragment");
			return true;

		}

		return super.onOptionsItemSelected(item);
	}

	private void changeColor(int newColor) {

		tabs.setIndicatorColor(newColor);

		// change ActionBar color just if an ActionBar is available
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			Drawable colorDrawable = new ColorDrawable(newColor);
			Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
			LayerDrawable ld = new LayerDrawable(new Drawable[] { colorDrawable, bottomDrawable });

			if (oldBackground == null) {

				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					ld.setCallback(drawableCallback);
				} else {
					getActionBar().setBackgroundDrawable(ld);
				}

			} else {

				TransitionDrawable td = new TransitionDrawable(new Drawable[] { oldBackground, ld });

				// workaround for broken ActionBarContainer drawable handling on
				// pre-API 17 builds
				// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
					td.setCallback(drawableCallback);
				} else {
					getActionBar().setBackgroundDrawable(td);
				}

				td.startTransition(200);

			}

			oldBackground = ld;

			// http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
			getActionBar().setDisplayShowTitleEnabled(false);
			getActionBar().setDisplayShowTitleEnabled(true);

		}

		currentColor = newColor;

	}

	public void onColorClicked(View v) {

		int color = Color.parseColor(v.getTag().toString());
		changeColor(color);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("currentColor", currentColor);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		currentColor = savedInstanceState.getInt("currentColor");
		changeColor(currentColor);
	}

	private Drawable.Callback drawableCallback = new Drawable.Callback() {
		@Override
		public void invalidateDrawable(Drawable who) {
			getActionBar().setBackgroundDrawable(who);
		}

		@Override
		public void scheduleDrawable(Drawable who, Runnable what, long when) {
			handler.postAtTime(what, when);
		}

		@Override
		public void unscheduleDrawable(Drawable who, Runnable what) {
			handler.removeCallbacks(what);
		}
	};

	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "Categories", "Home", "Top Paid", "Top Free", "Top Grossing", "Top New Paid",
				"Top New Free", "Trending" };

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			return SuperAwesomeCardFragment.newInstance(position);
		}

	}

}
*/
	
}
