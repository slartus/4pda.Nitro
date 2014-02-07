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
import android.content.*;
import android.preference.*;
import android.net.*;

public class LoginFragment extends BaseFragment
{
		private EditText login, password;
		private CheckBox checBoxPrivacy;
		private LoginTask loginTask;
		private TextView help, error;
		
		public String getTitle(){
			return LoginBrick.TITLE;
		}
		
		public String getName(){
			return LoginBrick.NAME;
		}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState)
		{
            View rootView = inflater.inflate(R.layout.login_fragment, container, false);
			login = (EditText)rootView.findViewById(R.id.editTextLogin);
			password = (EditText)rootView.findViewById(R.id.editTextParol);
			checBoxPrivacy = (CheckBox)rootView.findViewById(R.id.checkBoxPrivacy);
			error = (TextView)rootView.findViewById(R.id.textViewError);
			
			help = (TextView)rootView.findViewById(R.id.textViewHelp);
			help.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						BaseActivity.showActionViewActivity(getActivity(), "http://4pda.ru/forum/index.php?act=Reg&CODE=10");
					}
				});
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
			checBoxPrivacy.setChecked(isChecked());
			setProgress(false);
		}
		
		private boolean isChecked(){
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			return prefs.getBoolean("_privacy", false);
		}
		
		private void savePrivacyCheced(boolean checed){
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
			SharedPreferences.Editor e = prefs.edit();
			e.putBoolean("_privacy", checed).commit();
			
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
				savePrivacyCheced(privacy);
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
					help.setVisibility(View.VISIBLE);
					error.setVisibility(View.VISIBLE);
					error.setText("Ошибка авторизации");
				}
				MainActivity.setUserData();
			}

		}
}
