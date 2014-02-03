package ru.pda.nitro.profile;
import android.support.v4.app.*;
import android.widget.*;
import android.view.*;
import android.os.*;
import android.view.View.*;
import ru.pda.nitro.R;
import ru.pda.nitro.*;
import ru.pda.nitro.listfragments.*;
import ru.pda.nitro.bricks.BricksProfile.*;
import android.app.*;

public class LoginFragment extends BaseFragment
{

	@Override
	public void getData()
	{
		// TODO: Implement this method
	}

		private EditText login, password;
		private Button send;
		private LoginTask loginTask;
		private LinearLayout linearLogin;
		private ProgressBar progressLogin;
		
        
		public LoginFragment()
		{
        }
		
		private String getTitle(){
			return LoginBrick.TITLE;
		}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState)
		{
            View rootView = inflater.inflate(R.layout.login_fragment, container, false);
			login = (EditText)rootView.findViewById(R.id.editTextLogin);
			password = (EditText)rootView.findViewById(R.id.editTextParol);
			linearLogin = (LinearLayout)rootView.findViewById(R.id.linearLayoutLogin);
			progressLogin = (ProgressBar)rootView.findViewById(R.id.progressBarLogin);
			send = (Button)rootView.findViewById(R.id.buttonLogin);
			send.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						Login();
					}
				});
            return rootView;
        }

		@Override
		public void onActivityCreated(Bundle savedInstanceState)
		{
			super.onActivityCreated(savedInstanceState);
			BaseState.setMTitle(getTitle());
			getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			getActivity().getActionBar().setTitle(getTitle());
			setProgress(false);
		}
		
		private void showProgress(boolean progress){
			if(progress){
				linearLogin.setVisibility(View.GONE);
				progressLogin.setVisibility(View.VISIBLE);
			}else{
				linearLogin.setVisibility(View.VISIBLE);
				progressLogin.setVisibility(View.GONE);
				
			}
		}
		
		private void Login()
		{
			loginTask = new LoginTask();
			loginTask.execute();
		}

		private class LoginTask extends AsyncTask<Void, Void, Boolean>
		{

			private String mLogin, mPassword;
			public LoginTask()
			{
			}

			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				showProgress(true);
				mLogin = login.getText().toString();
				mPassword = password.getText().toString();
			}
			
			
			@Override
			protected Boolean doInBackground(Void[] p1)
			{
				try
				{
				return MainActivity.profile.doLogin(mLogin,mPassword);
		
				}
				catch (Exception e)
				{

                }
				return false;
			}
			

			@Override
			protected void onPostExecute(Boolean result)
			{
				super.onPostExecute(result);
				if (result)
				{
					getFragmentManager().beginTransaction()
						.add(R.id.content_frame, new FavoritesListFragment())
						.commit();
					BaseState.setLogin(result);
					MainActivity.getLogOutMenu();
				}
				else
				{
					showProgress(false);
					Toast.makeText(getActivity(), "Ошибка авторизации", Toast.LENGTH_SHORT).show();
					
				}
				MainActivity.setNickName();
			}

		}
}
