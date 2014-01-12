package ru.pda.application;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import ru.pda.application.listfragments.*;
import android.os.*;
import android.widget.*;
import android.view.View.*;

public class MainActivity extends Activity
{

	

	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null)
		{
            getFragmentManager().beginTransaction()
				.add(R.id.container, new PlaceholderFragment())
				.commit();
        }
	
    }

	

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
	{

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
		{
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
	{
		private EditText login, password;
		private Button send;
		private LoginTask loginTask;
        public PlaceholderFragment()
		{
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState)
		{
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
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
			// TODO: Implement this method
			super.onActivityCreated(savedInstanceState);
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
				// TODO: Implement this method
				super.onPreExecute();
				mLogin = login.getText().toString();
				mPassword = password.getText().toString();
			}
			
			
			@Override
			protected Boolean doInBackground(Void[] p1)
			{
				// TODO: Implement this meth
				try
				{
					if (profile.doLogin(mLogin,mPassword))
					{
						return true;
					}
				}
				catch (Exception e)
				{

                }
				return false;
			}
			

			@Override
			protected void onPostExecute(Boolean result)
			{
				// TODO: Implement this method
				super.onPostExecute(result);
				if (result)
				{
					getActivity().getActionBar().setTitle(profile.getLogin());
					getFragmentManager().beginTransaction()
						.add(R.id.container, new FavoritesListFragment())
						.commit();
				}
				else
				{
					Toast.makeText(getActivity(), "Ошибка авторизации", Toast.LENGTH_SHORT).show();
					
				}
			}

		}
		
    }

}
