package ru.pda.nitro.widgets;
import android.content.*;
import android.widget.*;
import android.appwidget.*;
import java.util.*;
import ru.forpda.interfaces.*;
import java.io.*;
import ru.pda.nitro.listfragments.*;
import ru.pda.nitro.database.*;
import ru.forpda.interfaces.forum.*;
import ru.forpda.http.*;
import java.text.*;
import android.net.Uri;

public class NewsWidget extends ListWidget
{

	@Override
	public String getWidgetTitle()
	{
		return "Новости";
	}

	public final static String NEWS_WIDGET_KEY = "ru.pda.nitro.widgets.NewsWidget.NEWS_WIDGET_KEY";
	private static NewsList newsList = new NewsList(new HttpHelper(App.getInstance()), "");
	
	@Override
	public Class<? extends ListWidget> getCurrentClass()
	{
		return NewsWidget.class;
	}

	@Override
	public void setList(RemoteViews rv, Context context, int appWidgetId)
	{
		Intent intent = new Intent(context, ListWidgetService.class);
		intent.putExtra(ListWidgetService.LIST_WIDGET_KEY, NEWS_WIDGET_KEY);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		Uri data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME));
		intent.setData(data);
		rv.setRemoteAdapter(R.id.lvList, intent);
		
	}

	@Override
	protected String getClassName()
	{
		super.getClassName();
		return NewsListFragment.NEWS_LIST_FRAGMENT;
	}
	
	

	@Override
	public void setListClick(RemoteViews rv, Context context, int appWidgetId)
	{
		// TODO: Implement this method
	}

	@Override
	public String getListKey()
	{
		return NEWS_WIDGET_KEY;
	}

	public static ArrayList<? extends ListInfo> inBackground(){
		
		try
		{
			newsList.loadNextNewsPage();
			return (ArrayList<ListInfo>)newsList;
		}
		catch (ParseException e)
		{}
		catch (IOException e)
		{}
		return null;

	}

	public static void inPostExecute(Context context, ArrayList<ListInfo>data){
		NewsListFragment.setLocalData(context, (ArrayList<News>)data, Contract.News.CONTENT_URI, "");

	}
}
