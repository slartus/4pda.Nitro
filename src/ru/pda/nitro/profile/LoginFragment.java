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

public class LoginFragment extends Fragment
{
	private EditText login, password;
		private Button send;
		private LoginTask loginTask;
		
        
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
			getActivity().getActionBar().setTitle(getTitle());
		}
		
		private void Login()
		{
			loginTask = new LoginTask(new UserProfile());
			loginTask.execute();
		}

		private class LoginTask extends AsyncTask<Void, Void, Boolean>
		{

			private UserProfile profile;
			private String mLogin, mPassword;
			public LoginTask(UserProfile profile)
			{
				this.profile = profile;
			}

			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				mLogin = login.getText().toString();
				mPassword = password.getText().toString();
			}
			
			
			@Override
			protected Boolean doInBackground(Void[] p1)
			{
				try
				{
				return profile.doLogin(mLogin,mPassword);
		
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
				}
				else
				{
					Toast.makeText(getActivity(), "Ошибка авторизации", Toast.LENGTH_SHORT).show();
					
				}
			}

		}
}
