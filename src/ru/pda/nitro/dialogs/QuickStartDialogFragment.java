package ru.pda.nitro.dialogs;
import android.os.*;
import android.app.*;
import android.content.*;
import android.widget.*;
import android.content.DialogInterface.OnClickListener;
import java.util.*;
import ru.pda.nitro.bricks.*;
import android.preference.*;
import android.view.View.*;
import android.view.*;
import ru.pda.nitro.*;


public class QuickStartDialogFragment extends BaseDialogFragment {
	public static final int RESULT_ALWAYS = 123;
	public static final int RESULT_JUST_NOW = 321;
	public static final String TOPIC_ID_KEY = "ru.pda.nitro.listfragments.TopicsListFragment.TOPIC_ID_KEY";
	public static final String NAVIGATE_ACTION_KEY = "ru.pda.nitro.listfragments.TopicsListFragment.NAVIGATE_ACTION_KEY";
	private static ArrayList<BrickInfo> menus;
	private MenuAdapter adapter;

	public static QuickStartDialogFragment newInstance() {
		QuickStartDialogFragment f = new QuickStartDialogFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
	//	args.putCharSequence(TOPIC_ID_KEY, topicId);
		f.setArguments(args);

		return f;
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
		menus = BricksList.getBricks(prefs);
	  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		adapter = new MenuAdapter(getActivity(), R.layout.row, menus);
	  builder.setAdapter(adapter, myClickListener);

		return builder.create();
	}

	OnClickListener myClickListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface p1, int p2)
		{
			BrickInfo item = adapter.getItem(p2);
			
		}

	};
	private Intent getIntent(CharSequence topicId, CharSequence navigateAction) {
		Intent intent = getActivity().getIntent();
		intent.putExtra(NAVIGATE_ACTION_KEY, navigateAction);
		intent.putExtra(TOPIC_ID_KEY, topicId);
		return intent;
	}

	public class MenuAdapter extends ArrayAdapter<BrickInfo>
    {
        final LayoutInflater inflater;
		private Context context;

        public MenuAdapter(Context context, int textViewResourceId, ArrayList<BrickInfo> objects) {
            super(context, textViewResourceId, objects);

			this.context = context;
            inflater = LayoutInflater.from(context);
		}


        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            final ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row, parent, false);
				holder = new ViewHolder();
				holder.linear = (LinearLayout)convertView.findViewById(R.id.linearRowBackground);
				
				holder.text = (TextView) convertView.findViewById(R.id.row_title);
				
				convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

			BrickInfo item = this.getItem(position);

			holder.text.setText(item.getTitle());

			return convertView;

        }
		public class ViewHolder{
			public TextView text;
			public LinearLayout linear;
		}
    }

}
