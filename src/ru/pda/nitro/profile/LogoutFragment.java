package ru.pda.nitro.profile;
import android.support.v4.app.*;
import android.view.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;
import ru.pda.nitro.R;
import ru.pda.nitro.*;
import ru.pda.nitro.bricks.BricksProfile.*;

public class LogoutFragment extends Fragment
{
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
		BaseState.setMTitle(getTitle());
		getActivity().getActionBar().setTitle(getTitle());
		
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
			UserProfile profile = new UserProfile();
			try
			{
			profile.doLogout();
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
			}
			else{
				showProgress(false);
			}
		}
		
	}
}
