package com.trablone.fragmentattacher;

import java.util.ArrayList;

public class FragmentAttacher
{
	private static ArrayList<FragmentItem> fragments = new ArrayList<FragmentItem>();

	public void AddFragment(FragmentListener listener)
	{
		FragmentItem item = new FragmentItem();
		item.setListener(listener);
		fragments.add(item);
	}

	public void RemoveFragment(int position)
	{
		if(fragments.size() - 1 >= position)
			fragments.remove(position);
	}

	public void clear(){
		if(fragments.size() > 0)
			fragments.clear();
	}


	public FragmentListener getItem(int position)
	{
		return fragments.get(position).getListener();
	}



	private class FragmentItem
	{
		private FragmentListener listener;

		public void setListener(FragmentListener listener)
		{
			this.listener = listener;
		}

		public FragmentListener getListener()
		{
			return listener;
		}}


	public static abstract interface FragmentListener
	{
		public abstract void onFragmentSelected(android.support.v4.app.FragmentManager fm);

		public abstract void onFragmentUnselected(android.support.v4.app.FragmentManager fm);


	}
}
