package ru.pda.application.adapters;
import android.widget.*;
import android.view.*;
import java.util.*;
import ru.pda.interfaces.forum.*;
import ru.pda.api.*;
import android.content.*;
import ru.pda.application.*;

public class ListAdapter extends BaseAdapter
{

	private Context context;
	private ArrayList<Topic> topic;
	final LayoutInflater inflater;
	
	public ListAdapter(Context context,ArrayList<Topic> topic){
		
		inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		
		this.context = context;
		this.topic = topic;
	}
	
	@Override
	public int getCount()
	{
		topic.size();
		// TODO: Implement this method
		return 0;
	}

	@Override
	public Object getItem(int p1)
	{
		
		// TODO: Implement this method
		return topic.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		// TODO: Implement this method
		return p1;
	}

	@Override
	public View getView(int position, View view, ViewGroup p3)
	{
		final ViewHolder holder;
		if(view == null){
			view = inflater.inflate(R.layout.item_list_topic, p3, false);
			holder = new ViewHolder();
			holder.textAutor = (TextView)view.findViewById(R.id.textViewAutor);
			holder.textDate = (TextView)view.findViewById(R.id.textViewDate);
			holder.textDescription = (TextView)view.findViewById(R.id.textViewDescription);
			holder.textForumTitle = (TextView)view.findViewById(R.id.textViewForumTitle);
			holder.textTitle = (TextView)view.findViewById(R.id.textViewTitle);
			view.setTag(holder);
			
		}else{
			holder = (ViewHolder) view.getTag();
		}
		holder.textAutor.setText(topic.get(position).getLastPostAuthor());
		holder.textDate.setText(topic.get(position).getLastPostDate().toString());
		holder.textDescription.setText(topic.get(position).getDescription());
		holder.textForumTitle.setText(topic.get(position).getForumTitle());
		holder.textTitle.setText(topic.get(position).getTitle());
		// TODO: Implement this method
		return view;
	}
	public class ViewHolder{
		public TextView textTitle;
		public TextView textDate;
		public TextView textDescription;
		public TextView textForumTitle;
		public TextView textAutor;
	}

}
