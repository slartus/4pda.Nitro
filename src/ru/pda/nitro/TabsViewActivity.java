package ru.pda.nitro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import ru.pda.nitro.BaseActivity;
import ru.pda.nitro.R;
import android.view.*;
import android.content.*;
import android.net.*;
import ru.pda.nitro.*;

/**
 * Created by slinkin on 21.01.14.
 */
public class TabsViewActivity extends BaseActivity {
	public static final String TOPIC_ACTIVITY_INTENT_KEY = "ru.pda.nitro.topicsview.TOPIC_ACTIVITY";
	public static final String TOPIC_ACTIVITY_CLASS_NAME_KEY = "ru.pda.nitro.topicsview.TOPIC_ACTIVITY_CLASS_NAME_KEY";
    public static final String TOPIC_ID_KEY = "ru.pda.nitro.topicsview.TOPIC_ID_KEY";
    public static final String TOPIC_URL_KEY = "ru.pda.nitro.topicsview.TOPIC_URL_KEY";
    public static final String NAVIGATE_ACTION_KEY = "ru.pda.nitro.topicsview.NAVIGATE_ACTION_KEY";
    public static final String TOPIC_TITLE_KEY = "ru.pda.nitro.topicsview.TOPIC_TITLE_KEY";
	public static final String TOPIC_LIST_TITLE_KEY = "ru.pda.nitro.topicsview.TOPIC_LIST_TITLE_KEY";
	public static final String TOPIC_GROOP_URI_KEY = "ru.pda.nitro.topicsview.TOPIC_GROOP_URI_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_layout);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
				.add(R.id.topic, new TabsViewFragment())
				.commit();
		}

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                if (extras.containsKey(TOPIC_LIST_TITLE_KEY))
                    setTitle(BaseState.getSpannable(this, extras.getString(TOPIC_LIST_TITLE_KEY)));
            }
        }


    }

	public static void show(Activity activity, Uri uri, CharSequence title, long l, String className){
		Intent intent = new Intent(activity.getApplicationContext(), TabsViewActivity.class); 
		intent.putExtra(TabsViewActivity.TOPIC_GROOP_URI_KEY, uri);
		intent.putExtra(TabsViewActivity.TOPIC_LIST_TITLE_KEY,title);
		intent.putExtra(TabsViewActivity.TOPIC_ACTIVITY_CLASS_NAME_KEY, className);

		activity.startActivity(intent);
	}

    public static void show(Activity activity, CharSequence topicId,
                            CharSequence navigateAction, String className) {
        show(activity, topicId, null, null, navigateAction, className);
    }

    public static void show(Context activity, CharSequence topicId, CharSequence topicTitle, CharSequence topicListTitle,
                            CharSequence navigateAction, String className) {
        Intent intent = new Intent(TOPIC_ACTIVITY_INTENT_KEY);

		intent.putExtra(TOPIC_ID_KEY, topicId);
        if (!TextUtils.isEmpty(navigateAction))
            intent.putExtra(NAVIGATE_ACTION_KEY, navigateAction);
        if (!TextUtils.isEmpty(topicTitle))
            intent.putExtra(TOPIC_TITLE_KEY, topicTitle);
		if (!TextUtils.isEmpty(topicListTitle))
            intent.putExtra(TOPIC_LIST_TITLE_KEY, topicListTitle);
		if (!TextUtils.isEmpty(className))
			intent.putExtra(TOPIC_ACTIVITY_CLASS_NAME_KEY, className);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}
