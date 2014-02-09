package ru.pda.nitro.adapters;
import android.widget.*;
import android.view.*;
import java.util.*;

import ru.forpda.interfaces.forum.Topic;
import ru.pda.nitro.R;

import android.content.*;

import android.util.*;
import ru.forpda.common.*;
import android.graphics.*;
import ru.forpda.interfaces.forum.*;


public class TopicListAdapter extends BaseListAdapter
{

	private Context context;
	private ArrayList<Topic> topics;
	final LayoutInflater inflater;
	private Typeface face;
	
	public TopicListAdapter(Context context,ArrayList<Topic> topics){
		
		inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		face = Typeface.createFromAsset(context.getAssets(), "4pda/fonts/Roboto-Regular.ttf");
		
		this.context = context;
		this.topics = topics;
	}

	@Override
	public void setData(ArrayList<IListItem> data)
	{
		this.topics = (ArrayList<Topic>) data;
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
			view = inflater.inflate(R.layout.list_item, p3, false);
			holder = new ViewHolder();
			holder.textAutor = (TextView)view.findViewById(R.id.textViewAutor);
			holder.textDate = (TextView)view.findViewById(R.id.textViewDate);
			holder.textDescription = (TextView)view.findViewById(R.id.textViewDescription);
			holder.textForumTitle = (TextView)view.findViewById(R.id.textViewForumTitle);
			holder.textTitle = (TextView)view.findViewById(R.id.textViewTitle);
			holder.textTitle.setTypeface(face);
			view.setTag(holder);
			
		}else{
			holder = (ViewHolder) view.getTag();
		}
		Topic topic = topics.get(position);
		holder.textAutor.setText(topic.getLastPostAuthor());
		holder.textDate.setText(topic.getLastPostDate());
		holder.textDescription.setText(topic.getDescription());
		holder.textForumTitle.setText(topic.getForumTitle());
		holder.textTitle.setText(topic.getTitle());
		holder.textTitle.setTextColor(Color.parseColor("#000000"));
		if(topic.getHasUnreadPosts()){
			holder.textTitle.setTextColor(Color.parseColor("#33B5E5"));
		}

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
