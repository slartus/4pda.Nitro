package ru.pda.nitro;
import android.app.*;
import android.os.*;
import uk.co.senab.actionbarpulltorefresh.library.*;

public class BaseActivity extends Activity implements IRefreshActivity
{
	public ActionBar ab;
	private PullToRefreshAttacher mPullToRefreshAttacher;
	

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
