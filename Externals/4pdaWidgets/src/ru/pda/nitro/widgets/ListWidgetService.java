package ru.pda.nitro.widgets;
import android.widget.*;
import android.content.*;

public class ListWidgetService extends RemoteViewsService {

	public final static String LIST_WIDGET_KEY = "ru.pda.nitro.widgets.ListWidgetService.LIST_WIDGET_KEY";
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		
		switch(intent.getStringExtra(LIST_WIDGET_KEY)){
			case TopicsWidget.TOPICS_WIDGET_KEY:
				return new TopicsAdapter(getApplicationContext(), intent);
		}
		return null;
	}

}
