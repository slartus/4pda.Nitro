package ru.pda.nitro.widgets;
import android.content.*;
import android.appwidget.*;
import java.util.*;
import ru.forpda.interfaces.forum.*;
import android.widget.*;
import android.graphics.*;
import ru.pda.nitro.listfragments.*;
import ru.pda.nitro.database.*;

public class TopicsAdapter extends ListWidgetAdapter
{

	
	private ArrayList<Topic> data;
	
	Context context;
	int widgetID;

	TopicsAdapter(Context ctx, Intent intent)
	{
		super(ctx, intent);
		context = ctx;
		widgetID = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
									  AppWidgetManager.INVALID_APPWIDGET_ID);
	}
	
	@Override
	public ArrayList<IListItem> getList()
	{
		data = new ArrayList<Topic>();
		return (ArrayList<IListItem>)data;
	}

	@Override
	public RemoteViews getView(int position)
	{
		final RemoteViews rView = new RemoteViews(context.getPackageName(), R.layout.list_item);
		if(data != null && data.size() > 0){
			rView.setTextColor(R.id.textViewTitle, Color.parseColor("#000000"));
			if(data.get(position).getHasUnreadPosts())
				rView.setTextColor(R.id.textViewTitle, Color.parseColor("#33B5E5"));

			rView.setTextViewText(R.id.textViewAutor, data.get(position).getLastPostAuthor());
			rView.setTextViewText(R.id.textViewDate, data.get(position).getLastPostDate());
			rView.setTextViewText(R.id.textViewDescription, data.get(position).getDescription());
			rView.setTextViewText(R.id.textViewForumTitle, data.get(position).getForumTitle());
			rView.setTextViewText(R.id.textViewTitle, data.get(position).getTitle());

			Intent clickIntent = new Intent();
			clickIntent.putExtra(ListWidget.LIST_ITEM_POSITION_KEY, position);

			rView.setOnClickFillInIntent(R.id.linearStartPost, clickIntent);
		}
		return rView;
	}

	@Override
	public ArrayList<IListItem> getData()
	{
		data = TopicsListFragment.getLocalTopicsData(context, Contract.Favorite.CONTENT_URI);
		return (ArrayList<IListItem>)data;
		}
	

}
