package ru.pda.nitro.listfragments;
import android.content.*;
import android.database.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import java.util.*;
import ru.forpda.interfaces.forum.*;
import ru.pda.nitro.*;
import ru.pda.nitro.database.*;

import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import ru.pda.nitro.bricks.*;
import ru.pda.nitro.topicsview.*;
import android.widget.AdapterView.*;
import ru.pda.nitro.dialogs.*;

public class GroopsListFragment extends BaseListFragment implements LoaderManager.LoaderCallbacks<Cursor>
{
	private CursorAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.list_topic, container, false);
		return initialiseListUi(v);
	}

	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		mAdapter = new SetDetailCursorAdapter(getActivity(), null, 0);
		listView.setAdapter(mAdapter);
		registerForContextMenu(listView);
		addTestGroops();
		getLoaderManager().restartLoader(0, null, this);
		setProgress(false);
		
	}
	
	private void addTestGroops(){
		Cursor cursor = getActivity().getContentResolver().query(getUri(), null, null, null, Contract.Groops.DEFAULT_SORT_ORDER);
		if(!cursor.moveToFirst()){
		ContentValues cv = new ContentValues();
		cv.put(Contract.Groops.title, "Тестовая группа");
		getActivity().getContentResolver().insert(getUri(), cv);
		}
	cursor.close();
	} 
	
	@Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (l < 0) return;
		final Cursor cursor = (Cursor) mAdapter.getItem(i);
		final String title = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Groops.title));
		TopicActivity.show(getActivity(), getUri(), title, l);
	
		}
	  
	public void onCreateContextMenu(android.view.ContextMenu contextMenu, android.view.View view,
                                    android.view.ContextMenu.ContextMenuInfo contextMenuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.groops_context_menu, contextMenu);
    }

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		DialogFragment dialogFragment;
		Bundle args;
		switch (item.getItemId()) {
			case R.id.context_rename_groop:
				dialogFragment = new RenameDialogFragment();
				args = new Bundle();
				args.putParcelable("_uri", ContentUris.withAppendedId(Contract.Groops.CONTENT_URI, info.id));
				final Cursor cursor = (Cursor) mAdapter.getItem(info.position);
				final int columnName = cursor.getColumnIndexOrThrow(Contract.Groops.title);
				String name = cursor.getString(columnName);
				args.putString("_name", name);
				dialogFragment.setArguments(args);
				dialogFragment.show(getActivity().getSupportFragmentManager(), null);
				
				break;
			case R.id.context_delete_groop:
				dialogFragment = new DeleteDialogFragment();
				args = new Bundle();
				args.putParcelable("_uri", ContentUris.withAppendedId(Contract.Groops.CONTENT_URI, info.id));
				dialogFragment.setArguments(args);
				dialogFragment.show(getActivity().getSupportFragmentManager(), null);
				break;
		}
		return super.onContextItemSelected(item);
	}
	

	@Override
	public Uri getUri()
	{
		return GroopsBrick.URI;
	}

	@Override
	public String getName()
	{
	
		return GroopsBrick.NAME;
	}

	@Override
	public String getTitle()
	{
	
		return GroopsBrick.TITLE;
	}


	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args)
	{
		
		return new CursorLoader(getActivity(),Contract.Groops.CONTENT_URI , null, null, null, Contract.Groops.DEFAULT_SORT_ORDER);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor)
	{
		if (getActivity() == null)
		{
			return;
		}

		int token = loader.getId();
		if (token == 0)
		{
			mAdapter.changeCursor(cursor);

		}
		else
		{
			cursor.close();
		}
	}
	@Override
	public void onLoaderReset(Loader<Cursor> loader)
	{
	}
	
	private class SetDetailCursorAdapter extends CursorAdapter
	{

		public SetDetailCursorAdapter(Context context, Cursor c, int flags)
		{
			super(context, c, flags);
		}


		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent)
		{
			return getActivity().getLayoutInflater().inflate(R.layout.groops_item, parent, false);
		}

		@Override
		public void bindView(View view, Context context, final Cursor cursor)
		{

			final String title = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Groops.title));
			final TextView textView1 = (TextView) view.findViewById(R.id.textView);
			
			textView1.setText(title);
			textView1.setSelected(true);

		}
	}
	
	@Override
	public void getData()
	{
	}

	@Override
	public boolean inBackground()
	{

		return false;
	}

	@Override
	public void inExecute()
	{

	}

	@Override
	public void onScrollStateChanged(AbsListView p1, int p2)
	{

	}

	@Override
	public void onScroll(AbsListView p1, int p2, int p3, int p4)
	{

	}
	
	@Override
	public ArrayList<? extends IListItem> getList() throws ParseException, IOException
	{
		return null;
	}

}
