package ru.pda.nitro.adapters;
import android.widget.*;
import android.view.*;
import java.util.*;

import ru.forpda.interfaces.forum.Topic;
import ru.pda.nitro.R;

import android.content.*;

import android.util.*;

public class TopicListAdapter extends BaseAdapter
{

	private Context context;
	private ArrayList<Topic> topics;
	final LayoutInflater inflater;
	
	public TopicListAdapter(Context context,ArrayList<Topic> topics){
		
		inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		
		this.context = context;
		this.topics = topics;
	}
	
	public void setData(ArrayList<Topic> data){
		this.topics = data;
	}
	
	@Override
	public int getCount()
	{
	
	return topics.size();
	}

	@Override
	public Object getItem(int p1)
	{
		// TODO: Implement this method
		return topics.get(p1);
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
		Topic topic = topics.get(position);
		Log.e("nitro", "title: " + topic.getTitle());
		holder.textAutor.setText(topic.getLastPostAuthor());
		holder.textDate.setText(topic.getLastPostDate().toString());
		holder.textDescription.setText(topic.getDescription());
		holder.textForumTitle.setText(topic.getForumTitle());
		holder.textTitle.setText(topic.getTitle());
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
