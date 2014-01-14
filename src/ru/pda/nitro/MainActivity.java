package ru.pda.nitro;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import android.os.*;
import android.widget.*;
import android.view.View.*;

import android.content.res.*;
import android.view.*;
import android.content.*;
import java.util.*;

import android.preference.*;
import android.util.*;

import ru.pda.nitro.bricks.BrickInfo;
import ru.pda.nitro.bricks.BricksList;
import ru.pda.nitro.listfragments.FavoritesListFragment;
import ru.forpda.interfaces.forum.*;

public class MainActivity extends BaseActivity
{
	public static ArrayList<Topic> topics;
	public static ArrayList<News> news;
	public static ArrayList<Forum> forums;
	
	ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout ;
	private ListView mDrawerList ;
	private FrameLayout frameDrawer;
	private CharSequence mTitle ;
	private MenuAdapter mAdapter;
	private ArrayList<BrickInfo> menus;
	private UserProfile profile;
	private int default_item;

	

	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_frame_drawer);

		profile = new UserProfile();
		
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(true);

		mTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R .id. drawer_layout);
		mDrawerList = (ListView) findViewById(R .id. left_drawer);
		frameDrawer = (FrameLayout)findViewById(R.id.frameDraver);

		menus = new ArrayList<BrickInfo>();
		mAdapter = new MenuAdapter(this, R.layout.row, menus);
		getMenu();
		
		mDrawerList.setAdapter(mAdapter);
		mDrawerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					// TODO: Implement this metho
					startDeleteMode();
					return false;
				}
			});

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout ,
												  R .drawable.ic_drawer_white , R.string.app_menu , R.string.app_name) {
			public void onDrawerClosed(View view)
			{
				ab. setTitle(mTitle);
				//	invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
				mAdapter.notifyDataSetChanged();

			}

			public void onDrawerOpened(View drawerView)
			{
				ab.setTitle(R.string.app_menu);
				//	invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setLongClickable(true);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		if (savedInstanceState == null)
		{
			
            setDefaultContent();
        }
		

    }

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle . syncState();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private void setDefaultContent(){
	Fragment mContent;
		if(profile.isLogined()){
		//	setTitle(profile.getLogin());
			mContent = menus.get(getPosition()).createFragment();
		}else{
			mContent = new PlaceholderFragment();
		}
		setContent(mContent);
	}
	
	private int getPosition(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		for(int i = 0; i < menus.size(); i++){
			
			if(menus.get(i).getName().equals(prefs.getString("mainFavorite_", "favorites"))){
				setTitle(menus.get(i).getTitle());
			return i;
			}
		}
		return 0;
	}


	private class DrawerItemClickListener implements ListView.OnItemClickListener
	{

		public void onItemClick(AdapterView <?> parent , View view , int position, long id)
		{
			BrickInfo item = mAdapter.getItem(position);

			if (DeleteMode) {
				//	item.setSelected(!item.isSelected());
				//	mAdapter.notifyDataSetChanged();
			} else {
				if (mTitle.equals(item.getTitle()))
				{
					mDrawerLayout.closeDrawer(frameDrawer);
					mDrawerList.setItemChecked(position, false);
				}
				else
				{
					selectItem(position, item);
				}
			}

		}

	}

	ActionMode mMode;

    public Boolean DeleteMode = false;

    private void startDeleteMode() {

        mMode = startActionMode(new AnActionModeOfEpicProportions());
        DeleteMode = true;
		getMenu();
        mDrawerList.setSelection(AbsListView.CHOICE_MODE_MULTIPLE);
        mAdapter.notifyDataSetChanged();
    }

    private void stopDeleteMode(Boolean finishActionMode) {
        if (finishActionMode && mMode != null) {
            mMode.finish();
        }
        DeleteMode = false;
		getMenu();
        mDrawerList.setSelection(AbsListView.CHOICE_MODE_NONE);
        mAdapter.notifyDataSetChanged();
    }

	private final class AnActionModeOfEpicProportions implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
           
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
			//     stopDeleteMode(true);
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            stopDeleteMode(false);
        }
    }


	private void setContent(Fragment fragment){
		
		getFragmentManager().beginTransaction()
			.replace(R.id.content_frame , fragment)
			.commit();

	}

	private void selectItem(int position, BrickInfo item)
	{
			setContent(item.createFragment());
			mDrawerList.setItemChecked(position, false);
			setTitle(item.getTitle());

		mDrawerLayout.closeDrawer(frameDrawer);


	}

	@Override
	public void setTitle(CharSequence title)
	{
		mTitle = title ;
		ab.setTitle(mTitle);
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
       	if (mDrawerToggle.onOptionsItemSelected(item))
		{
			return true;
		}
		
        return super.onOptionsItemSelected(item);
    }
	
	/**
	 *Адаптер NavigationDrawer
	 */
		
	 private void getMenu(){
		 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		 menus = BricksList.getBricks(prefs);
		 mAdapter.setData(menus);
		 mAdapter.notifyDataSetChanged();
	 }
	 
	class MenuAdapter extends ArrayAdapter<BrickInfo>
    {
        final LayoutInflater inflater;
		private Context context;
		SharedPreferences prefs;
		
		public void setData(ArrayList<BrickInfo> data) {
            if (getCount() > 0)
                clear();
            if (data != null) {
                for (BrickInfo item : data) {
					if(!DeleteMode && item.isSelected())
                    add(item);
					else if(DeleteMode)
						addAll(item);
                }
            }
        }
		
        public MenuAdapter(Context context, int textViewResourceId, ArrayList<BrickInfo> objects) {
            super(context, textViewResourceId, objects);

			this.context = context;
            inflater = LayoutInflater.from(context);
			prefs = PreferenceManager.getDefaultSharedPreferences(context);
			
		}
		

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            final ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row, parent, false);
				holder = new ViewHolder();
				holder.linear = (LinearLayout)convertView.findViewById(R.id.linearRowBackground);
				holder.image = (ImageView)convertView.findViewById(R.id.imageView);
				holder.image.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View p1)
						{
							BrickInfo theme = (BrickInfo)holder.check.getTag();

							//	holder.image.setImageResource(theme.isFavorite() ? R.drawable.ic_action_important : R.drawable.ic_action_not_important);

							SharedPreferences.Editor e = prefs.edit();
							e.putString("mainFavorite_" , theme.getName());
							e.putBoolean("mainMenu_" + theme.getName() , true);

							e.commit();
							getMenu();
						}
					});
				holder.text = (TextView) convertView.findViewById(R.id.row_title);
				holder.check = (CheckBox) convertView.findViewById(android.R.id.text1);
				holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							BrickInfo theme = (BrickInfo)holder.check.getTag();
							theme.setSelected(buttonView.isChecked());

							SharedPreferences.Editor e = prefs.edit();
							e.putBoolean("mainMenu_" + theme.getName() , theme.isFavorite() ? true : buttonView.isChecked());
							e.commit();
							getMenu();
						}
					});
				convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

			BrickInfo item = this.getItem(position);

            holder.check.setTag(item);
			holder.image.setVisibility(DeleteMode && !item.getTitle().equals("Поиск") ? View.VISIBLE : View.GONE);

			holder.image.setImageResource(item.isFavorite() ? R.drawable.ic_action_important_light : R.drawable.ic_action_not_important_light );//(theme ? R.drawable.ic_action_important_light : R.drawable.ic_action_important) : (theme ? R.drawable.ic_action_not_important_light : R.drawable.ic_action_not_important));
			holder.check.setVisibility(DeleteMode ? View.VISIBLE : View.GONE);
			holder.check.setChecked(item.isSelected());

			holder.text.setText(item.getTitle());
			/*	textView1.setTypeface(face);
			 if (current_position == position)
			 {
			 textView1.setTypeface(current_face);
			 }*/

			 
			return convertView;
			
        }
		public class ViewHolder{
			public CheckBox check;
			public TextView text;
			public ImageView image;
			public LinearLayout linear;
		}
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

}