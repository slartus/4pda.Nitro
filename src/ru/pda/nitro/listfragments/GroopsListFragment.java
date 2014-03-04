package ru.pda.nitro.listfragments;

import android.app.ActionBar;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import ru.forpda.interfaces.forum.IListItem;
import ru.pda.nitro.R;
import ru.pda.nitro.bricks.GroopsBrick;
import ru.pda.nitro.database.Contract;
import ru.pda.nitro.dialogs.DeleteDialogFragment;
import ru.pda.nitro.dialogs.RenameDialogFragment;
import ru.pda.nitro.TabsViewActivity;
import android.widget.*;

public class GroopsListFragment extends BaseListFragment implements LoaderManager.LoaderCallbacks<Cursor> , FragmentLifecycle{
    private CursorAdapter mAdapter;
//	private ListView listView;
   
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_topic, container, false);
        listView = (ListView) v.findViewById(R.id.listViewTopic);
		
		return initialiseUi(v);
    }
	
	@Override
	public void onResumeFragment()
	{
		super.onResumeFragment();
	//	getPullToRefreshAttacher(listView);
		// TODO: Implement this method
	}
	


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    //     getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		super.onActivityCreated(savedInstanceState);
		
		listView.setOnItemClickListener(this);
        listView.setOnCreateContextMenuListener(this);
		
        mAdapter = new SetDetailCursorAdapter(getActivity(), null, 0);
        listView.setAdapter(mAdapter);
        getLoaderManager().restartLoader(0, null, this);
        setProgress(false);
		
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, final long l) {
        if (l < 0) return;
        handler = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run() {
                Cursor cursor = (Cursor) mAdapter.getItem(i);
                final String title = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Groops.title));
                Uri mUri = ContentUris.withAppendedId(getUri(), l).buildUpon().appendPath("Groop").build();
                Cursor groopCursor = null;

                try {
                    groopCursor = getActivity().getContentResolver().query(mUri, null, null, null, null);
                    if (groopCursor != null && groopCursor.moveToFirst())
                        TabsViewActivity.show(getActivity(), mUri, title, l, null);
                    else
                        Toast.makeText(getActivity(), "В этой группе нет тем.", Toast.LENGTH_SHORT).show();
                } finally {
                    if (groopCursor != null)
                        groopCursor.close();
                }


            }
        });

    }

    public void onCreateContextMenu(android.view.ContextMenu contextMenu, android.view.View view,
                                    android.view.ContextMenu.ContextMenuInfo contextMenuInfo) {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.groops_context_menu, contextMenu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.context_rename_groop:
				final Cursor cursor = (Cursor) mAdapter.getItem(info.position);
				showRenameDialogFragment(
				ContentUris.withAppendedId(Contract.Groops.CONTENT_URI, info.id),
				cursor.getString(cursor.getColumnIndexOrThrow(Contract.Groops.title)));
                break;
            case R.id.context_delete_groop:
                showDeleteDialog(
				ContentUris.withAppendedId(Contract.Groops.CONTENT_URI, info.id),
				info.id);
                break;
        }
        return super.onContextItemSelected(item);
    }
	
	private void showRenameDialogFragment(Uri mUri, String name){
		DialogFragment dialogFragment = RenameDialogFragment.newInstance(mUri, name);
		dialogFragment.show(getActivity().getSupportFragmentManager(), null);
	}
	
	private void showDeleteDialog(Uri mUri, long id){
		DialogFragment dialogFragment = DeleteDialogFragment.newInstance(mUri, id);
		dialogFragment.show(getActivity().getSupportFragmentManager(), null);
	}


    @Override
    public Uri getUri() {
        return GroopsBrick.URI;
    }

    @Override
    public String getName() {

        return GroopsBrick.NAME;
    }

    @Override
    public String getTitle() {

        return GroopsBrick.TITLE;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(), Contract.Groops.CONTENT_URI, null, null, null, Contract.Groops.DEFAULT_SORT_ORDER);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (getActivity() == null) {
            return;
        }

        int token = loader.getId();
        if (token == 0) {
            mAdapter.changeCursor(cursor);

        } else {
            cursor.close();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private class SetDetailCursorAdapter extends CursorAdapter {

        public SetDetailCursorAdapter(Context context, Cursor c, int flags) {
            super(context, c, flags);
        }


        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return getActivity().getLayoutInflater().inflate(R.layout.groops_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, final Cursor cursor) {

            final String title = cursor.getString(cursor.getColumnIndexOrThrow(Contract.Groops.title));
            final TextView textView1 = (TextView) view.findViewById(R.id.textView);

            textView1.setText(title);
            textView1.setSelected(true);

        }
    }

    @Override
    public void getData() {
    }

    @Override
    public boolean inBackground() {

        return false;
    }

    @Override
    public void inExecute() {

    }

    @Override
    public void onScrollStateChanged(AbsListView p1, int p2) {

    }

    @Override
    public void onScroll(AbsListView p1, int p2, int p3, int p4) {

    }

    @Override
    public ArrayList<? extends IListItem> getList() throws ParseException, IOException {
        return null;
    }
	
	@Override
	public String getClassName()
	{
		return null;
	}

}
