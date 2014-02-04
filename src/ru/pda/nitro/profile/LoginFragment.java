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
		private EditText login, password;
		private CheckBox checBoxPrivacy;
		private LoginTask loginTask;
		
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
			checBoxPrivacy = (CheckBox)rootView.findViewById(R.id.checkBoxPrivacy);
			initialiseDataUi(rootView);
			
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
		
	@Override
	public void getData()
	{
		loginTask = new LoginTask();
		loginTask.execute();
	}

		private class LoginTask extends AsyncTask<Void, Void, Boolean>
		{

			private String mLogin, mPassword;
			private boolean privacy;
			public LoginTask()
			{
			}

			@Override
			protected void onPreExecute()
			{
				super.onPreExecute();
				showStatus(linearData, progressBarData,true);
				mLogin = login.getText().toString();
				mPassword = password.getText().toString();
				privacy = checBoxPrivacy.isChecked();
			}
			
			
			@Override
			protected Boolean doInBackground(Void[] p1)
			{
				try
				{
				return MainActivity.profile.doLogin(mLogin,mPassword, privacy);
		
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
					showStatus(linearData, progressBarData, false);
					Toast.makeText(getActivity(), "Ошибка авторизации", Toast.LENGTH_SHORT).show();
					
				}
				MainActivity.setUserData();
			}

		}
}
