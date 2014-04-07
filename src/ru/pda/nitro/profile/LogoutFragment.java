package ru.pda.nitro.profile;
import android.support.v4.app.*;
import android.view.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import ru.pda.nitro.R;
import ru.pda.nitro.*;
import ru.pda.nitro.bricks.BricksProfile.*;
import android.app.*;

public class LogoutFragment extends BaseFragment
{

	@Override
	public String getName()
	{
		// TODO: Implement this method
		return LogoutBrick.NAME;
	}

		
	public String getTitle(){
		return LogoutBrick.TITLE;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.logout_fragment, container, false);
		initialiseDataUi(rootView);
		return rootView;
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		BaseState.setMTitle(getTitle());
		getActivity().getActionBar().setTitle(getTitle());
		mSwipeRefreshLayout.setRefreshing(true);
	}
	
	@Override
	public void getData()
	{
		Task task = new Task();
		task.execute();
	}
	
	public class Task extends AsyncTask<Void, Void, Boolean>
	{

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			showStatus(linearData, progressBarData,true);
		}

		
		@Override
		protected Boolean doInBackground(Void[] p1)
		{
			try
			{
			MainActivity.profile.doLogout();
			return true;
			}
			catch (Throwable e)
			{}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result)
		{
		
			super.onPostExecute(result);
			if(result){
				BaseState.setLogin(false);
				MainActivity.getLoginMenu();
				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, new LoginFragment())
				.commit();
				
			}else{
				Toast.makeText(getActivity(), "Ошибка выхода", Toast.LENGTH_SHORT).show();
			}
			
			MainActivity.setUserData();
			showStatus(linearData,progressBarData,false);
		}
		
	}
}
