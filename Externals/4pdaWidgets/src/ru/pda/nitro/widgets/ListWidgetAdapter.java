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
import ru.pda.nitro.database.*;
import ru.pda.nitro.listfragments.*;

public abstract class ListWidgetAdapter implements RemoteViewsFactory
{

	ArrayList<IListItem> data;
	Context context;
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
		App.getInstance().setWidgetsCount(App.getInstance().getWidgetsCount() + 1);
		data = getList();
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
		return getView(position);
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
		data = getData();
	}
	
	public abstract ArrayList<IListItem> getData();
	public abstract ArrayList<IListItem> getList();
	public abstract RemoteViews getView(int position);

	@Override
	public void onDestroy()
	{
	}

}
