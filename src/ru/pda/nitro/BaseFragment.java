package ru.pda.nitro;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public abstract class BaseFragment extends Fragment {
    public LinearLayout linearProgress;
    public LinearLayout linearError;
    public Button buttonError;

    public abstract void getData();

    public View initialiseUi(View v) {
        linearProgress = (LinearLayout) v.findViewById(R.id.linearProgress);
        linearError = (LinearLayout) v.findViewById(R.id.linearError);
        buttonError = (Button) v.findViewById(R.id.buttonError);
        buttonError.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View p1) {
                getData();
            }
        });
        return v;
    }

}
