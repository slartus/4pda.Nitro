package ru.pda.nitro.bricks.BricksProfile;

import android.support.v4.app.Fragment;
import ru.pda.nitro.bricks.*;
import ru.pda.nitro.profile.*;


/**
 * Created by slartus on 12.01.14.
 */
public class ProfileBrick extends BrickInfo
{


    public static final String NAME="profile";
    public static final String TITLE="Профиль";
    public ProfileBrick() {
        super(NAME, TITLE,true, false);
    }

    @Override
    public Fragment createFragment() {
        return new ProfileFragment();
    }
}
