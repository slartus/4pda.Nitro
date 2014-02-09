package ru.pda.nitro.widgets;

import android.app.*;
import android.appwidget.*;
import android.content.*;
import android.net.*;
import android.view.*;
import android.widget.*;
import ru.pda.nitro.widgets.*;
import android.util.*;
import ru.pda.nitro.*;
import ru.forpda.interfaces.*;
import java.util.*;
import ru.forpda.interfaces.forum.*;

public abstract class ListWidget extends AppWidgetProvider
{
	public final static String LIST_WIDGET_ACTION_KEY = "ru.pda.nitro.widgets.ListWidtet.LIST_WIDGET_ACTION_KEY";
	public final String START_TOPICK_KEY = "ru.pda.nitro.widgets.ListWidget.START_TOPICK_KEY";
	private final String START_APP_KEY = "ru.pda.nitro.widgets.ListWidget.START_APP_KEY";
	private final String UPDATE_CURENT_WIDGETS = "ru.pda.nitro.widgets.ListWidget.UPDATE_CURENT_WIDGETS";
	private final String WIDGETS_ID_KEY = "ru.pda.nitro.widgets.ListWidget.WIDGETS_ID_KEY";
	private final String MAIN_ACTIVITY_INTENT_KEY = "ru.pda.nitro.MAIN_ACTIVITY";
	
	public final static String LIST_ITEM_POSITION_KEY = "ru.pda.nitro.widgets.ListWidget.LIST_ITEM_POSITION_KEY";
	
	public static boolean update = true;
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
						 int[] appWidgetIds)
	{
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		for (int i : appWidgetIds)
		{
			updateWidget(context, appWidgetManager, i);
		}
	}

	void updateWidget(Context context, AppWidgetManager appWidgetManager,
					  int appWidgetId)
	{
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
		
		setUpdateTV(rv, context, appWidgetId);

		setList(rv, context, appWidgetId);

		setListClick(rv, context, appWidgetId);

		appWidgetManager.updateAppWidget(appWidgetId, rv);
		
		if(update)
		appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
														R.id.lvList);
	}

	void setUpdateTV(RemoteViews rv, Context context, int appWidgetId)
	{
	
		if(update){
			rv.setViewVisibility(R.id.ivUpdate, View.VISIBLE);
			rv.setViewVisibility(R.id.progressBar, View.GONE);
		}else{
			rv.setViewVisibility(R.id.ivUpdate, View.GONE);
			rv.setViewVisibility(R.id.progressBar, View.VISIBLE);
		}
			
		Intent updIntent=  new Intent(context, getCurrentClass());
		updIntent.setAction(UPDATE_CURENT_WIDGETS);
		updIntent.putExtra(WIDGETS_ID_KEY, appWidgetId);
		Uri adata = Uri.parse(updIntent.toUri(Intent.URI_INTENT_SCHEME));
		updIntent.setData(adata);
		PendingIntent updPIntent = PendingIntent.getBroadcast(context, 0, updIntent, 0);
		rv.setOnClickPendingIntent(R.id.ivUpdate, updPIntent);
		
		Intent startIntent=  new Intent(context, getCurrentClass());
		startIntent.setAction(START_APP_KEY);
		Uri startdata = Uri.parse(startIntent.toUri(Intent.URI_INTENT_SCHEME));
		startIntent.setData(startdata);
		PendingIntent startPIntent = PendingIntent.getBroadcast(context, 0, startIntent, 0);
		rv.setOnClickPendingIntent(R.id.ivIcon, startPIntent);

	}
	public abstract Class<? extends ListWidget> getCurrentClass();

	public abstract void setList(RemoteViews rv, Context context, int appWidgetId);

	public abstract void setListClick(RemoteViews rv, Context context, int appWidgetId);
	
	public static ArrayList<IListItem> inBackground(){
		return null;
	}
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		super.onReceive(context, intent);
		
		int itemPos = intent.getIntExtra(LIST_ITEM_POSITION_KEY, -1);
		if (itemPos != -1)
		{
			
			if (intent.getAction().equalsIgnoreCase(START_TOPICK_KEY))
			{
				
				WidgetsHelper.startItem(context, itemPos);
				updateUllWidgets(context);
				
			}
		}
		
		if (intent.getAction().equalsIgnoreCase(START_APP_KEY))
		{
			Intent i = new Intent(MAIN_ACTIVITY_INTENT_KEY);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
		
		if (intent.getAction().equalsIgnoreCase(UPDATE_CURENT_WIDGETS))
		{
					update = false;
					int appWidgetId = intent.getIntExtra(WIDGETS_ID_KEY, -1);
					
					AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
					updateWidget(context, appWidgetManager, appWidgetId);
						
					context.startService(new Intent(context, UpdateWidgetsService.class));

		}
		
		if(intent.getAction().equalsIgnoreCase(WidgetsHelper.UPDATE_ALL_WIDGETS)){
			update = true;
			updateUllWidgets(context);
		}
	}

	public void updateUllWidgets(Context context){
		ComponentName thisAppWidget = new ComponentName(
			context.getPackageName(), getClass().getName());
		AppWidgetManager appWidgetManager = AppWidgetManager
			.getInstance(context);
		int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
		for (int appWidgetID : ids)
		{
			updateWidget(context, appWidgetManager, appWidgetID);
		}
	}
}
