package ru.pda.nitro;
import android.app.*;
import android.content.*;
import android.database.*;
import android.net.*;
import ru.pda.nitro.database.*;
import ru.pda.nitro.listfragments.*;
import ru.pda.nitro.topicsview.*;

public class WidgetsHelper
{
	public final static String LIST_WIDGETS_ACTION_KEY = "ru.pda.nitro.widgets.action.LIST_WIDGETS_ACTION_KEY";
	public final static String UPDATE_ALL_WIDGETS = "ru.pda.nitro.widgets.ListWidget.UPDATE_ALL_WIDGETS";
	
	/*	public static boolean isNitroInstaled(Context context)
	 {
	 PackageManager pm = context.getPackageManager();
	 final String THIS_PACKAGE = "ru.pda.nitro";

	 try
	 {
	 ApplicationInfo applicationInfo = pm.getApplicationInfo(THIS_PACKAGE, 0);
	 int flags = applicationInfo.flags;

	 return true;
	 }
	 catch (PackageManager.NameNotFoundException e)
	 {

	 }

	 return false;
	 }

	 public static boolean isExternal(Context context)
	 {

	 PackageManager pm = context.getPackageManager();
	 final String THIS_PACKAGE = context.getPackageName();

	 try
	 {
	 ApplicationInfo applicationInfo = pm.getApplicationInfo(THIS_PACKAGE, 0);
	 int flags = applicationInfo.flags;

	 return (flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE;

	 }
	 catch (PackageManager.NameNotFoundException e)
	 {/

	 }

	 return false;
	 }*/

	public static void startItem(Context context, int itemPos){
		Cursor c = context.getContentResolver().query(Contract.Favorite.CONTENT_URI, null, null, null, Contract.Favorite.DEFAULT_SORT_ORDER);

		if(c.moveToPosition(itemPos)){

			final CharSequence topicId = c.getString(c.getColumnIndex(Contract.Favorite.id));
			final CharSequence topicTitle = c.getString(c.getColumnIndex(Contract.Favorite.title));
			final CharSequence navidate = "getnewpost";
			TopicsListFragment.updateItem(context, itemPos);
			TopicActivity.show(context, topicId,topicTitle, "Избранное", navidate);
		}

		c.close();

	}
	
	private static void updateClientList(){
		
	}

	public static void updateAllWidgets(Context context){
		Intent intent = new Intent(LIST_WIDGETS_ACTION_KEY);
		intent.setAction(UPDATE_ALL_WIDGETS);
		context.sendBroadcast(intent);
	}
	
}
