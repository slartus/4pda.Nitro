package ru.pda.nitro.topicsview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import ru.pda.nitro.BaseActivity;
import ru.pda.nitro.R;

/**
 * Created by slinkin on 21.01.14.
 */
public class TopicActivity extends BaseActivity {
    public static final String TOPIC_ID_KEY = "ru.pda.nitro.topicsview.TOPIC_ID_KEY";
    public static final String TOPIC_URL_KEY = "ru.pda.nitro.topicsview.TOPIC_URL_KEY";
    public static final String TOPIC_TITLE_KEY = "ru.pda.nitro.topicsview.TOPIC_TITLE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_layout);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.topic, new TopicView())
                    .commit();
        }

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                if (extras.containsKey(TOPIC_TITLE_KEY))
                    setTitle(extras.getString(TOPIC_TITLE_KEY));
            }
        }
    }


    public static void show(Activity activity, CharSequence topicId) {
        show(activity, topicId, null);
    }

    public static void show(Activity activity, CharSequence topicId, CharSequence topicTitle) {
        Intent intent = new Intent(activity.getApplicationContext(), TopicActivity.class);

        intent.putExtra(TOPIC_ID_KEY, topicId);
        if (!TextUtils.isEmpty(topicTitle))
            intent.putExtra(TOPIC_TITLE_KEY, topicTitle);
        activity.startActivity(intent);
    }
}
