package ru.pda.nitro;

import android.app.Activity;
import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.DialogFragment;
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

import android.preference.*;

import ru.pda.nitro.bricks.BrickInfo;
import ru.pda.nitro.bricks.BricksList;
import ru.pda.nitro.listfragments.FavoritesListFragment;
import ru.forpda.interfaces.forum.*;
import android.graphics.*;
import android.app.*;
import ru.pda.nitro.dialogs.*;
import ru.pda.nitro.bricks.*;
import java.util.ArrayList;
import ru.forpda.common.*;

public class MainActivity extends BaseActivity
{
	ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout ;
	private ListView mDrawerList ;
	private FrameLayout frameDrawer;
	private Fragment mContent;
	private static ImageView avatar;
	private Handler handler = new Handler();
	private boolean profile_menu = false;
	
	private static MenuAdapter mAdapter;
	private static ArrayList<BrickInfo> menus;
	public static UserProfile profile;
	public static TextView textNick;

	@Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_frame_drawer);
		
		profile = new UserProfile();
		BaseState.setLogin(profile.isLogined());
		BaseState.setMTitle(getTitle());
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setDisplayShowHomeEnabled(true);

		
		mDrawerLayout = (DrawerLayout) findViewById(R .id. drawer_layout);
		mDrawerList = (ListView) findViewById(R .id. left_drawer);
		frameDrawer = (FrameLayout)findViewById(R.id.frameDraver);

		menus = new ArrayList<BrickInfo>();
		mAdapter = new MenuAdapter(this, R.layout.row, menus);
		
		getMenu();
		
		mDrawerList.addHeaderView(mainMenuHeader());
		mDrawerList.setAdapter(mAdapter);
		mDrawerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					if(!profile_menu)
					startDeleteMode();
					return false;
				}
			});

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout ,
												  R .drawable.ic_drawer_white , R.string.app_menu , R.string.app_name) {
			public void onDrawerClosed(View view)
			{
				ab. setTitle(BaseState.getMTitle());
				//	invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
				mAdapter.notifyDataSetChanged();

			}

			public void onDrawerOpened(View drawerView)
			{
				ab.setTitle(R.string.app_menu);
				//	invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		if (savedInstanceState == null)
			setDefaultContent(BaseState.isLogin());

    }
	
	private View mainMenuHeader(){
		View header = getLayoutInflater().inflate(R.layout.main_menu_header, null, false);
		avatar = (ImageView)header.findViewById(R.id.imageViewAvatar);
		final ImageView imageNavigation = (ImageView)header.findViewById(R.id.imageViewNavigation);
		textNick = (TextView)header.findViewById(R.id.textViewNick);
		imageNavigation.setOnClickListener(new OnClickListener(){
		
				@Override
				public void onClick(View p1)
				{
					
					if(!profile_menu && !DeleteMode){
						imageNavigation.setImageResource(R.drawable.ic_action_collapse);
					if(profile.getLogin().equals("гость") | profile.getLogin().equals("")){
						getLoginMenu();
					}else{
						getLogOutMenu();
					}
					profile_menu = true;
					}else{
						imageNavigation.setImageResource(R.drawable.ic_action_expand);
						getMenu();
						profile_menu = false;
					}
				}
			});
				setUserData();
		return header;
	}
	
	
	public static void setUserData(){
		textNick.setText(profile.getLogin());
		imageLoader.displayImage(profile.getAvatar(), avatar);
		
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener
	{

		public void onItemClick(AdapterView <?> parent , View view , int position, long id)
		{
			BrickInfo item = mAdapter.getItem(position - 1);

			if (!DeleteMode) {
				if (BaseState.getMTitle().equals(item.getTitle()))
				{
					mDrawerLayout.closeDrawer(frameDrawer);
					mDrawerList.setItemChecked(position, false);
				}
				else
				{
					selectItem(position - 1, item);
				}
			}

		}

	}

	private void selectItem(final int position, final BrickInfo item)
	{
		BaseState.setMTitle(item.getTitle());
		mDrawerLayout.closeDrawer(frameDrawer);
		handler.postDelayed(new Runnable(){

				@Override
				public void run()
				{
					setContent(item.createFragment());
					mDrawerList.setItemChecked(position, false);	
				}
			}, 1000);
			
			}
			
	private void setContent(Fragment fragment){

		getSupportFragmentManager()
			.beginTransaction()
			.replace(R.id.content_frame , fragment)
			.commit();
	}
			
	private void setDefaultContent(boolean login){

		if(login){
			mContent = menus.get(getPosition()).createFragment();
		}else{
			mContent = menus.get(1).createFragment();
		}
		setContent(mContent);
	}

	private int getPosition(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		for(int i = 0; i < menus.size(); i++){

			if(menus.get(i).getName().equals(prefs.getString("mainFavorite_", "favorites"))){
				BaseState.setMTitle(menus.get(i).getTitle());
				return i;
			}
		}
		return 0;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		menu.setGroupVisible(R.id.group_groops, BaseState.isGroop_menu());
		menu.setGroupVisible(R.id.group_profile, BaseState.isLogin_menu());

		return super.onPrepareOptionsMenu(menu);
	}
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
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
		
		switch(item.getItemId()){
			case R.id.menu_finish:
				finish();
				break;
			case R.id.menu_add_groops:
				DialogFragment df = new AddGroopsDialogFragment();
				df.show(getSupportFragmentManager(), null);
				break;
			case R.id.menu_register:
				showActionViewActivity(this, "http://4pda.ru/forum/index.php?act=Reg&CODE=00");
		}
		
        return super.onOptionsItemSelected(item);
    }
		
	 private void getMenu(){
		 menus.clear();
		 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		 menus = BricksList.getBricks(prefs);
		 setAdapter(menus);
	 }
	 
	 public static void getLogOutMenu(){
		 menus.clear();
		 menus = BricksList.getLogoutMenu();
		 setAdapter(menus);
	 }
	public static void getLoginMenu(){
		menus.clear();
		menus = BricksList.getLoginMenu();
		setAdapter(menus);
	}
	 
	 private static void setAdapter(ArrayList<BrickInfo> menus){
		 mAdapter.setData(menus);
		 mAdapter.notifyDataSetChanged();
	 }
	 
	public class MenuAdapter extends ArrayAdapter<BrickInfo>
    {
        final LayoutInflater inflater;
		private Context context;
		SharedPreferences prefs;
		private Typeface face, current_face;
		
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
			current_face = Typeface.createFromAsset(context.getAssets(), "4pda/fonts/Roboto-Black.ttf");
			face = Typeface.createFromAsset(context.getAssets(), "4pda/fonts/Roboto-Regular.ttf");
			
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
			holder.text.setTypeface(face);
			 if (BaseState.getMTitle().equals(item.getTitle()))
			 {
			 holder.text.setTypeface(current_face);
			 }

			 
			return convertView;
			
        }
		public class ViewHolder{
			public CheckBox check;
			public TextView text;
			public ImageView image;
			public LinearLayout linear;
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

}
