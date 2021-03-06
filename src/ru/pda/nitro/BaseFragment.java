package ru.pda.nitro;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.*;
import uk.co.senab.actionbarpulltorefresh.library.*;
import android.os.*;
import android.support.v4.app.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import ru.pda.nitro.bricks.BricksProfile.*;
import ru.pda.nitro.bricks.*;
import android.text.*;
import android.text.style.*;
import android.app.*;
import ru.pda.nitro.classes.*;
import android.view.*;
import ru.pda.nitro.listfragments.*;
import android.support.v4.widget.*;



public abstract class BaseFragment extends Fragment implements FragmentLifecycle, SwipeRefreshLayout.OnRefreshListener{
//    private PullToRefreshAttacher mPullToRefreshAttacher;
	public LinearLayout linearProgress;
    public LinearLayout linearError;
	public LinearLayout linearData;
	public ProgressBar progressBarData;
	private Button buttonData;
	
    public TextView buttonError, buttonErrorOk;
	private boolean refresh = false;
	private boolean loading = false;
    public Handler handler;
	public SwipeRefreshLayout mSwipeRefreshLayout;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
    protected void getData()
	{}

	
	public void onResumeFragment(){
		setCurrentFragmentMenu();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
		
		}
	
	public void setCurrentFragmentMenu(){
		BaseState.setLogin_menu(getName().equals(LoginBrick.NAME));
		BaseState.setGroop_menu(getName().equals(GroopsBrick.NAME));
		refreshActionBarMenu(getActivity());
	}
	

    public View initialiseUi(View v) {
		mSwipeRefreshLayout = (SwipeRefreshLayout)v.findViewById(R.id.refresh);
		linearProgress = (LinearLayout) v.findViewById(R.id.linearProgress);
        linearError = (LinearLayout) v.findViewById(R.id.linearError);
        buttonError = (TextView) v.findViewById(R.id.buttonErrorRefresh);
		buttonErrorOk = (TextView)v.findViewById(R.id.buttonErrorOk);
        buttonError.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View p1) {
                hideError();
				mSwipeRefreshLayout.setRefreshing(true);
				getData();
            }
        });
		buttonErrorOk.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					hideError();
				}
			});
			
		mSwipeRefreshLayout.setOnRefreshListener(this);
        
	/*	mSwipeRefreshLayout.setColorScheme
		(R.color.red, R.color.red,
		 R.color.red, R.color.red);*/
        return v;
    }

	@Override
	public void onRefresh()
	{
		mSwipeRefreshLayout.setRefreshing(true);
		refreshData();
	}

	
	
	
	public void initialiseDataUi(View rootView){
		linearData =(LinearLayout)rootView.findViewById(R.id.linearLayoutData);
		progressBarData = (ProgressBar)rootView.findViewById(R.id.progressBarData);
		buttonData = (Button)rootView.findViewById(R.id.buttonData);
		buttonData.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					getData();
				}
			});
	}
	
	public static void refreshActionBarMenu(FragmentActivity activity)
		{
			activity.invalidateOptionsMenu();
		}
	
	public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean isLoading() {
        return loading;
    }
	
	public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public boolean isRefresh() {
        return refresh;
    }

	public void showStatus(View one, View two, boolean status) {
        if (status) {
            one.setVisibility(View.GONE);
            two.setVisibility(View.VISIBLE);
        } else {
            one.setVisibility(!isRefresh() ? View.VISIBLE : View.GONE);
            two.setVisibility(View.GONE);

        }
    }
	
	public void hideError(){
		linearError.setVisibility(View.GONE);
	}
	
	public void hideProgress() {
        linearProgress.setVisibility(View.GONE);
    }
	
/*	public void setProgress(boolean loading)
	{
        ((IRefreshActivity)getActivity()).getPullToRefreshAttacher().setRefreshing(loading);
    }*/

/*	public void getPullToRefreshAttacher(View view){
		((IRefreshActivity)getActivity()).getPullToRefreshAttacher().setRefreshableView(view, new PullToRefreshAttacher.OnRefreshListener() {
				@Override
				public void onRefreshStarted(View view)
				{
					refreshData();
				}
			});
	}*/
	
	protected void refreshData(){};
	protected String getName(){return null;}

    protected String getTitle(){return null;}
}
