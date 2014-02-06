package ru.pda.nitro.widgets;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import ru.pda.nitro.widgets.R;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;
import android.database.sqlite.*;
import android.util.*;
import android.database.*;
import android.widget.*;
import android.net.*;
import android.view.*;
import android.graphics.*;
import android.os.*;
import ru.forpda.interfaces.forum.*;
import ru.pda.nitro.widgets.database.*;

public class ListWidgetAdapter implements RemoteViewsFactory
{

	ArrayList<Topic> data;
	Context context;
	SimpleDateFormat sdf;
	int widgetID;

	ListWidgetAdapter(Context ctx, Intent intent)
	{
		context = ctx;
		widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
									  AppWidgetManager.INVALID_APPWIDGET_ID);
	}

	@Override
	public void onCreate()
	{
		
		data = new ArrayList<Topic>();
	
	}

	@Override
	public int getCount()
	{
		return data.size();
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public RemoteViews getLoadingView()
	{
		return null;
	}

	@Override
	public RemoteViews getViewAt(final int position)
	{
		final RemoteViews rView = new RemoteViews(context.getPackageName(), R.layout.list_item);
		rView.setImageViewResource(R.id.imgFlag, 0);
		if(data.get(position).getHasUnreadPosts())
		rView.setImageViewResource(R.id.imgFlag, R.drawable.new_flag);
		
		rView.setTextViewText(R.id.textViewAutor, data.get(position).getLastPostAuthor());
		rView.setTextViewText(R.id.textViewDate, data.get(position).getLastPostDate());
		rView.setTextViewText(R.id.textViewDescription, data.get(position).getDescription());
		rView.setTextViewText(R.id.textViewForumTitle, data.get(position).getForumTitle());
		rView.setTextViewText(R.id.textViewTitle, data.get(position).getTitle());
		
		Intent clickIntent = new Intent();
		clickIntent.putExtra(ListWidget.LIST_ITEM_POSITION_KEY, position);
		
		rView.setOnClickFillInIntent(R.id.linearStartPost, clickIntent);
	
		return rView;
	}

	@Override
	public int getViewTypeCount()
	{
		return 1;
	}

	@Override
	public boolean hasStableIds()
	{
		return true;
	}

	@Override
	public void onDataSetChanged()
	{
		data = getLocalData();
	}
	
	public ArrayList<Topic> getLocalData() {
        data = new ArrayList<Topic>();
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(Contract.Favorite.CONTENT_URI, null, null, null, Contract.Favorite.DEFAULT_SORT_ORDER);
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        Topic topic = new Topic(null, null);
                        topic.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Favorite.description)));
                        topic.setForumTitle(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Favorite.forumTitle)));
                        topic.setHasUnreadPosts(cursor.getInt(cursor.getColumnIndexOrThrow(Contract.Favorite.hasUnreadPosts)) == 1 ? true : false);
                        topic.setId(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Favorite.id)));
                        topic.setLastPostAuthor(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Favorite.lastAvtor)));
                        topic.setLastPostDate(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Favorite.lastDate)));
                        topic.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Contract.Favorite.title)));
                        data.add(topic);
                    } while (cursor.moveToNext());

                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        return data;
    }

	@Override
	public void onDestroy()
	{

	}

}
