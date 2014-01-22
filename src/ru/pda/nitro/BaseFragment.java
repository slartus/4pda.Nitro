package ru.pda.nitro;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.*;

public abstract class BaseFragment extends Fragment {
    public LinearLayout linearProgress;
    public LinearLayout linearError;
    public Button buttonError;
	private boolean refresh = false;
	private boolean loading = false;
    

    public abstract void getData();

    public View initialiseUi(View v) {
		
        linearProgress = (LinearLayout) v.findViewById(R.id.linearProgress);
        linearError = (LinearLayout) v.findViewById(R.id.linearError);
        buttonError = (Button) v.findViewById(R.id.buttonError);
        buttonError.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View p1) {
                getData();
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

	public void showError(boolean isError) {
        if (isError) {
            linearProgress.setVisibility(View.GONE);
            linearError.setVisibility(View.VISIBLE);
        } else {
            if (!isRefresh())
                linearProgress.setVisibility(View.VISIBLE);
            linearError.setVisibility(View.GONE);

        }
    }
	
	public void hideProgress() {
        linearProgress.setVisibility(View.GONE);
    }
}
