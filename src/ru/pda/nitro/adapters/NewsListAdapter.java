package ru.pda.nitro.adapters;
import android.widget.*;
import android.view.*;
import java.util.*;
import ru.forpda.interfaces.forum.*;
import android.content.*;

public class NewsListAdapter extends BaseAdapter
{

	private ArrayList<News> news;
	private Context context;
	
	
	public NewsListAdapter(Context context, ArrayList<News> data){
		this.news = data;
		this.context = context;
	}
	
	public void setData(ArrayList<News> data){
		this.news = data;
	}
	
	@Override
	public int getCount()
	{
		// TODO: Implement this method
		return news.size();
	}

	@Override
	public Object getItem(int p1)
	{
		// TODO: Implement this method
		return news.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		// TODO: Implement this method
		return p1;
	}

	@Override
	public View getView(int p1, View p2, ViewGroup p3)
	{
		// TODO: Implement this method
		return null;
	}

}
