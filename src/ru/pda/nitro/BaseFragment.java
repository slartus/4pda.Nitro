package ru.pda.nitro;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.*;
import uk.co.senab.actionbarpulltorefresh.library.*;
import android.os.*;

public abstract class BaseFragment extends Fragment {
    public LinearLayout linearProgress;
    public LinearLayout linearError;
    public TextView buttonError, buttonErrorOk;
	private boolean refresh = false;
	private boolean loading = false;
    public Handler handler;

    public abstract void getData();

    public View initialiseUi(View v) {
		
        linearProgress = (LinearLayout) v.findViewById(R.id.linearProgress);
        linearError = (LinearLayout) v.findViewById(R.id.linearError);
        buttonError = (TextView) v.findViewById(R.id.buttonErrorRefresh);
		buttonErrorOk = (TextView)v.findViewById(R.id.buttonErrorOk);
        buttonError.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View p1) {
                hideError();
				setProgress(true);
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
        return v;
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

	public void showStatus(boolean isError) {
        if (isError) {
            linearProgress.setVisibility(View.GONE);
            linearError.setVisibility(View.VISIBLE);
        } else {
            linearProgress.setVisibility(!isRefresh() ? View.VISIBLE : View.GONE);
            linearError.setVisibility(View.GONE);

        }
    }
	
	public void hideError(){
		linearError.setVisibility(View.GONE);
	}
	
	public void hideProgress() {
        linearProgress.setVisibility(View.GONE);
    }
	
	public void setProgress(boolean loading)
	{

        ((IRefreshActivity) getActivity()).getPullToRefreshAttacher().setRefreshing(loading);

    }
	
	public void getPullToRefreshAttacher(View view){
		((IRefreshActivity) getActivity()).getPullToRefreshAttacher().setRefreshableView(view, new PullToRefreshAttacher.OnRefreshListener() {
				@Override
				public void onRefreshStarted(View view)
				{
					refreshData();
				}
			});
	}
	
	protected void refreshData(){};
}
