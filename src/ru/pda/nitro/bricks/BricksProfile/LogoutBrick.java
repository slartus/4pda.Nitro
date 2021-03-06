package ru.pda.nitro.bricks.BricksProfile;

import android.support.v4.app.Fragment;
import ru.pda.nitro.bricks.*;
import ru.pda.nitro.profile.*;


/**
 * Created by slartus on 12.01.14.
 */
public class LogoutBrick extends BrickInfo
{


    public static final String NAME="logout";
    public static final String TITLE="Выход";
    public LogoutBrick() {
        super(NAME, TITLE,true, false);
    }

    @Override
    public Fragment createFragment() {
        return new LogoutFragment();
    }
	
	public Class<? extends Fragment> getClassList(){
		return LogoutFragment.class;
	}
}
