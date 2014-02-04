package ru.pda.nitro;
import android.app.*;
import android.os.*;
import uk.co.senab.actionbarpulltorefresh.library.*;
import android.support.v4.app.*;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BaseActivity extends FragmentActivity implements IRefreshActivity
{
	public ActionBar ab;
	private PullToRefreshAttacher mPullToRefreshAttacher;
	protected static ImageLoader imageLoader = ImageLoader.getInstance();
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		ab = getActionBar();
		mPullToRefreshAttacher = new PullToRefreshAttacher(this);
		
	}
	@Override
    public PullToRefreshAttacher getPullToRefreshAttacher() {
        return mPullToRefreshAttacher;
    }
}
