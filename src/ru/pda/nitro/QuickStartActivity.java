package ru.pda.nitro;
import android.os.*;
import android.app.*;
import android.content.*;
import ru.pda.nitro.listfragments.*;
import ru.pda.nitro.bricks.*;
import android.support.v4.app.Fragment;
import ru.forpda.interfaces.menu.*;
import java.util.*;
import android.preference.*;

public class QuickStartActivity extends BaseActivity
{
	public final static String BRICK_NAME_KEY = "ru.pda.nitro.QuickStartActivity.BRICK_NAME_KEY";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.topic_layout);
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		ArrayList<BrickInfo> menu= BricksList.getBricks(prefs);
		String name = getIntent().getStringExtra(BRICK_NAME_KEY);
		for(int i =0; i < menu.size(); i++){
			if(menu.get(i).getName().equals(name)){
				getSupportFragmentManager().beginTransaction()
					.add(R.id.topic, menu.get(i).createFragment())
					.commit();
					break;
			}
		}
	}
	
	
	public static void show(Activity activity, CharSequence name){
		Intent intent = new Intent(activity, QuickStartActivity.class);
		intent.putExtra(BaseListFragment.QUICK_START_LIST_ITEM_SELECT, true);
		intent.putExtra(BRICK_NAME_KEY, name);
		activity.startActivity(intent);
		
	}
	
}
