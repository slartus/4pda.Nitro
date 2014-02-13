package ru.pda.nitro.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.RemoteViews;
import java.util.*;
import android.util.*;
import java.text.SimpleDateFormat;
import android.content.*;
import ru.pda.nitro.*;
import android.app.*;
import android.net.*;
import android.os.*;
import java.text.*;



public class TimeWidget extends AppWidgetProvider {

	private DateFormat df = new SimpleDateFormat("HH:mm");
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		
		final int widgetCount = appWidgetIds.length;
		for (int i = 0; i < widgetCount; i++) {
			int appWidgetId = appWidgetIds[i];
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
	}

	public void updateAppWidget(Context context, final AppWidgetManager appWidgetManager, final int appWidgetId) {
		final RemoteViews rView = new RemoteViews(context.getPackageName(), R.layout.time_widget);
		
		
		rView.setTextViewText(R.id.textViewTime, df.format(new Date()));
		appWidgetManager.updateAppWidget(appWidgetId, rView);
		
		Intent updIntent=  new Intent(context, TimeWidget.class);
		updIntent.setAction(WidgetsHelper.UPDATE_TIME_WIDGETS);
		Uri adata = Uri.parse(updIntent.toUri(Intent.URI_INTENT_SCHEME));
		updIntent.setData(adata);
		PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, updIntent, 0);

		AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + getTimeOutForNextMinute(), 6000, pIntent);
		
	}
	
	public static long getTimeOutForNextMinute() {
		int nextMinute = GregorianCalendar.getInstance().get(Calendar.MINUTE)+1;
	
		Calendar nextCalendar = new GregorianCalendar();
		nextCalendar.setTime(GregorianCalendar.getInstance().getTime());
		nextCalendar.set(Calendar.MINUTE, nextMinute);
		nextCalendar.set(Calendar.SECOND, 0);
		nextCalendar.set(Calendar.MILLISECOND, 0);

		return nextCalendar.getTime().getTime() - GregorianCalendar.getInstance().getTime().getTime();
	}

	@Override
	public void onReceive(Context context, Intent intent)
	{
		super.onReceive(context, intent);
		
		if (intent.getAction().equalsIgnoreCase(WidgetsHelper.UPDATE_TIME_WIDGETS))
		{
			updateAllWidgets(context);
		}
	}

	public void updateAllWidgets(Context context)
	{
		ComponentName thisAppWidget = new ComponentName(
			context.getPackageName(), getClass().getName());
		AppWidgetManager appWidgetManager = AppWidgetManager
			.getInstance(context);
		int ids[] = appWidgetManager.getAppWidgetIds(thisAppWidget);
		for (int appWidgetID : ids)
		{
			updateAppWidget(context, appWidgetManager, appWidgetID);
		}
	}
	
}
