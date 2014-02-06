package ru.pda.nitro.widgets;
import android.widget.*;
import android.content.*;

public class ListWidgetService extends RemoteViewsService {

	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		return new ListWidgetAdapter(getApplicationContext(), intent);

	}

}
