package ru.pda.nitro.listfragments;

import android.app.Fragment;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import ru.forpda.interfaces.forum.IListItem;
import android.widget.*;


/**
 * Created by slartus on 12.01.14.
 * Базовый класс для списков
 * Здесь общие свойства и методы для фрагментов списков
 */
public abstract class BaseListFragment extends Fragment {
    public LinearLayout linearProgress;
	public LinearLayout linearError;
	private boolean loading = false;
	private boolean refresh = false;
	
	public ListView listView;

	public void setRefresh(boolean refresh)
	{
		this.refresh = refresh;
	}

	public boolean isRefresh()
	{
		return refresh;
	}

	public void setLoading(boolean loading)
	{
		this.loading = loading;
	}

	public boolean isLoading()
	{
		return loading;
	}

	
	
	public abstract ArrayList<? extends IListItem> getList() throws ParseException, IOException;

    public abstract String getName();

    public abstract String getTitle();


}
