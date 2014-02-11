package ru.pda.nitro.widgets;
import java.util.*;
import ru.forpda.interfaces.forum.*;
import android.widget.*;
import android.content.*;
import android.appwidget.*;
import ru.pda.nitro.listfragments.*;
import android.database.*;
import ru.pda.nitro.database.*;
import android.view.*;

public class NewsAdapter extends ListWidgetAdapter
{
	private ArrayList<News> data = new ArrayList<News>();
	Context context;
	int widgetID;

	NewsAdapter(Context ctx, Intent intent)
	{
		super(ctx, intent);
		context = ctx;
		widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
									  AppWidgetManager.INVALID_APPWIDGET_ID);
	}
	@Override
	public ArrayList<IListItem> getData()
	{
		Cursor cursor = context.getContentResolver().query(Contract.News.CONTENT_URI, null, null, null, Contract.Favorite.DEFAULT_SORT_ORDER);
		if(cursor != null){
		data = LocalDataHelper.getLocalNews(cursor);
		cursor.close();
		return (ArrayList<IListItem>)data;
		}
		cursor.close();
		data = new ArrayList<News>();
		return (ArrayList<IListItem>)data;
	}

	@Override
	public ArrayList<IListItem> getList()
	{
		return (ArrayList<IListItem>)data;
	}

	@Override
	public RemoteViews getView(int position)
	{
		final RemoteViews rView = new RemoteViews(context.getPackageName(), R.layout.item_news);
		if(data != null && data.size() > 0){
			
			News item = data.get(position);
			rView.setTextViewText(R.id.textNewsAvtor, item.getAuthor());
			rView.setTextViewText(R.id.textNewsComments, String.valueOf(item.getCommentsCount()));
			rView.setTextViewText(R.id.textNewsDate, item.getNewsDate());
			rView.setTextViewText(R.id.textNewsDescription, item.getDescription());
			rView.setViewVisibility(R.id.textNewsSource, item.getSourceTitle() != null ? View.VISIBLE : View.GONE);
			rView.setTextViewText(R.id.textNewsSource, item.getSourceTitle());
			rView.setViewVisibility(R.id.textNewsTag, item.getTagTitle() != null ? View.VISIBLE : View.GONE);
			rView.setTextViewText(R.id.textNewsTag, item.getTagTitle());
			rView.setTextViewText(R.id.textNewsTitle, item.getTitle());

			Intent clickIntent = new Intent();
			clickIntent.putExtra(ListWidget.LIST_ITEM_POSITION_KEY, position);

			rView.setOnClickFillInIntent(R.id.linearStartPost, clickIntent);
		}
		return rView;
	}

}
