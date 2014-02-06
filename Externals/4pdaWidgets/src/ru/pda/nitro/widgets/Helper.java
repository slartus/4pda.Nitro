package ru.pda.nitro.widgets;
import android.database.*;
import android.content.*;
import ru.pda.nitro.widgets.database.*;
import android.content.pm.*;
import android.provider.*;
import android.os.*;
import android.app.*;
import android.text.*;

public class Helper
{
	public static final String TOPIC_ID_KEY = "ru.pda.nitro.topicsview.TOPIC_ID_KEY";
    public static final String TOPIC_URL_KEY = "ru.pda.nitro.topicsview.TOPIC_URL_KEY";
    public static final String NAVIGATE_ACTION_KEY = "ru.pda.nitro.topicsview.NAVIGATE_ACTION_KEY";
    public static final String TOPIC_TITLE_KEY = "ru.pda.nitro.topicsview.TOPIC_TITLE_KEY";
	public static final String TOPIC_LIST_TITLE_KEY = "ru.pda.nitro.topicsview.TOPIC_LIST_TITLE_KEY";
	public static final String TOPIC_GROOP_URI_KEY = "ru.pda.nitro.topicsview.TOPIC_GROOP_URI_KEY";	
	
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
		{

    	}

    	return false;
    }*/
	
	public static void startItem(Context context, int itemPos){
		Cursor c = context.getContentResolver().query(Contract.Favorite.CONTENT_URI, null, null, null, Contract.Favorite.DEFAULT_SORT_ORDER);
		c.moveToPosition(itemPos);

		final CharSequence topicId = c.getString(c.getColumnIndex(Contract.Favorite.id));
		final CharSequence topicTitle = c.getString(c.getColumnIndex(Contract.Favorite.title));
		final CharSequence navidate = "getnewpost";
		updateItem(context, itemPos);
		show(context, topicId,topicTitle, "Избранное", navidate);
	}

    public static void show(Context context, CharSequence topicId, CharSequence topicTitle, CharSequence topicListTitle,
                            CharSequence navigateAction) {
        Intent intent = new Intent(ListWidget.TOPIC_ACTIVITY_INTENT_KEY);

		intent.putExtra(TOPIC_ID_KEY, topicId);
        if (!TextUtils.isEmpty(navigateAction))
            intent.putExtra(NAVIGATE_ACTION_KEY, navigateAction);
        if (!TextUtils.isEmpty(topicTitle))
            intent.putExtra(TOPIC_TITLE_KEY, topicTitle);
		if (!TextUtils.isEmpty(topicListTitle))
            intent.putExtra(TOPIC_LIST_TITLE_KEY, topicListTitle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
    }
	
	private static void updateItem(final Context context, final int i) {
       Handler handler = new Handler();
        handler.post(new Runnable() {

				@Override
				public void run() {
					Cursor cursor = null;
					try {
						cursor = context.getContentResolver().query(Contract.Favorite.CONTENT_URI, null, null, null, Contract.Favorite.DEFAULT_SORT_ORDER);
						if (cursor != null && cursor.moveToPosition(i)) {
							long l = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
							ContentValues cv = new ContentValues();
							cv.put(Contract.Favorite.hasUnreadPosts, false);

						context.getContentResolver().update(ContentUris.withAppendedId(Contract.Favorite.CONTENT_URI, l), cv, null, null);
						}
					} finally {
						if (cursor != null)
							cursor.close();
					}


				}
			});
    }
	
}
