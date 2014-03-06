package ru.pda.nitro.listfragments.pagers;
import android.view.*;
import com.astuetz.*;
import android.support.v4.view.*;
import ru.pda.nitro.*;
import android.os.*;
import android.graphics.*;
import ru.pda.nitro.listfragments.*;

public class SoftTagPagerFragment extends BaseTagPagerFragment implements FragmentLifecycle
{
	
	public static SoftTagPagerFragment newInstance(String url){
		SoftTagPagerFragment fragment = new SoftTagPagerFragment();
		Bundle args = new Bundle();
        args.putString(NewsListFragment.NEWS_URL, url);
        fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.soft_tag_view_pager, container, false);
		tabs = (PagerSlidingTabStrip) v.findViewById(R.id.soft_tabs);
		mViewPager = (ViewPager) v.findViewById(R.id.soft_pager);

		return v;	
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		urls = getRubricTag();
		mViewPager.setOffscreenPageLimit(urls.size());

		mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
		mViewPager.setAdapter(mPagerAdapter);

		tabs.setViewPager(mViewPager);

		tabs.setIndicatorHeight(6);
		tabs.setOnPageChangeListener(
            new ViewPager.SimpleOnPageChangeListener() {

				@Override
                public void onPageSelected(int position) {
                    onResumePager(position);
                }
            });	

		tabs.setIndicatorColor(Color.parseColor(currentColor));


	}
	@Override
	public void onResumeFragment()
	{
		onResumePager(mViewPager.getCurrentItem());
	}
}
