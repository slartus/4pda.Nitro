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
	public void getData()
	{
		// TODO: Implement this method
	}

	private Button logout;
	private ProgressBar progressLogout;
	
	private String getTitle(){
		return LogoutBrick.TITLE;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.logout_fragment, container, false);
		progressLogout = (ProgressBar)rootView.findViewById(R.id.progressLogout);
		logout = (Button)rootView.findViewById(R.id.buttonLogout);
		logout.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					Task task = new Task();
					task.execute();
				}
			});
		return rootView;
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onActivityCreated(savedInstanceState);
		getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		
		BaseState.setMTitle(getTitle());
		getActivity().getActionBar().setTitle(getTitle());
		setProgress(false);
	}
	
	
	private void showProgress(boolean progress){
		if(progress){
			logout.setVisibility(View.GONE);
			progressLogout.setVisibility(View.VISIBLE);
		}else{
			logout.setVisibility(View.VISIBLE);
			progressLogout.setVisibility(View.GONE);
			
		}
	}
	
	public class Task extends AsyncTask<Void, Void, Boolean>
	{

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			showProgress(true);
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
				getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, new LoginFragment())
				.commit();
				BaseState.setLogin(false);
				MainActivity.getLoginMenu();
			}else{
				Toast.makeText(getActivity(), "Ошибка выхода", Toast.LENGTH_SHORT).show();
			}
			
			MainActivity.setNickName();
			showProgress(false);
		}
		
	}
}
