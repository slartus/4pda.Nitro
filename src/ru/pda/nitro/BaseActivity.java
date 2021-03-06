package ru.pda.nitro;
import android.app.*;
import android.content.*;
import android.net.*;
import android.os.*;
import android.support.v4.app.*;
import com.nostra13.universalimageloader.core.*;
import uk.co.senab.actionbarpulltorefresh.library.*;

public class BaseActivity extends FragmentActivity implements IRefreshActivity
{

	@Override
	public PullToRefreshAttacher getPullToRefreshAttacher()
	{
		return mPullToRefreshAttacher;
	}

	private PullToRefreshAttacher mPullToRefreshAttacher;
	public ActionBar ab;
	protected static ImageLoader imageLoader = ImageLoader.getInstance();
	

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		ab = getActionBar();
		ab.setIcon(R.drawable.ab_4pda);
		mPullToRefreshAttacher = new PullToRefreshAttacher(this);
	}
	
	
	public static void showActionViewActivity(Activity activity, String url){
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		activity.startActivity(Intent.createChooser(intent, "Открыть в..."));
		
	}
}
