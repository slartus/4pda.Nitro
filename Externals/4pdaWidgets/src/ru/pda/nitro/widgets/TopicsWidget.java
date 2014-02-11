package ru.pda.nitro.widgets;
import android.widget.*;
import android.content.*;
import android.app.*;
import android.appwidget.*;
import java.util.*;
import ru.forpda.interfaces.forum.*;
import ru.forpda.api.*;
import ru.forpda.http.*;
import ru.forpda.interfaces.*;
import java.io.IOException;
import android.net.Uri;
import java.text.ParseException;
import ru.pda.nitro.listfragments.*;
import ru.pda.nitro.database.*;
import ru.pda.nitro.topicsview.*;

public class TopicsWidget extends ListWidget
{

	@Override
	public String getWidgetTitle()
	{
		return "Избранное";
	}


	@Override
	public String getListKey()
	{
		return TOPICS_WIDGET_KEY;
	}



	public final static String TOPICS_WIDGET_KEY = "ru.pda.nitro.widgets.TopicsWidget.TOPICS_WIDGET_KEY";
	
	
	public void setList(RemoteViews rv, Context context, int appWidgetId)
	{
		Intent intent = new Intent(context, ListWidgetService.class);
		intent.putExtra(ListWidgetService.LIST_WIDGET_KEY, TOPICS_WIDGET_KEY);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		Uri data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME));
		intent.setData(data);
		rv.setRemoteAdapter(R.id.lvList, intent);
	}
	
	public void setListClick(RemoteViews rv, Context context, int appWidgetId)
	{
		Intent listClickIntent = new Intent(context, TopicsWidget.class);
		listClickIntent.setAction(START_TOPICK_KEY);
		PendingIntent listClickPIntent = PendingIntent.getBroadcast(context, 0,	listClickIntent, 0);
		rv.setPendingIntentTemplate(R.id.lvList, listClickPIntent);

	}
	
	@Override
	public Class<? extends ListWidget> getCurrentClass()
	{
		return TopicsWidget.class;
	}
	
	@Override
    private static ArrayList<Topic> getTopicsList() throws ParseException, IOException
	{
		ListInfo listInfo = new ListInfo();
        return TopicsApi.getFavorites(new HttpHelper(App.getInstance()), listInfo);
    }
	
	public static ArrayList<? extends ListInfo> inBackground(){

		try
		{
			return (ArrayList<ListInfo>)getTopicsList();
		}
		catch (ParseException e)
		{}
		catch (IOException e)
		{}
		return null;
		
	}
	
	public static void inPostExecute(Context context, ArrayList<ListInfo>data){
		BaseListFragment.deleteAllLocalData(context, Contract.Favorite.CONTENT_URI);
		TopicsListFragment.setLocalData(context, (ArrayList<Topic>)data, Contract.Favorite.CONTENT_URI);
		
	}
}
