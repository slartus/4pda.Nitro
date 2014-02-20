package ru.pda.nitro.bricks;

import java.util.ArrayList;
import android.content.*;
import ru.pda.nitro.profile.*;
import ru.pda.nitro.bricks.BricksProfile.*;
import ru.pda.nitro.*;

/**
 * Created by slartus on 12.01.14.
 */
public class BricksList {
    /**
     * Список всех элементов из "быстрый запуск"
     * @return
     */
    public static ArrayList<BrickInfo> getBricks(SharedPreferences prefs) {
        ArrayList<BrickInfo> res = new ArrayList<BrickInfo>();
		res.add(new GroopsBrick(prefs));
        res.add(new NewsBrick(prefs));
		if(BaseState.isLogin()){
        res.add(new FavoritesBrick(prefs));
		res.add(new SubscribesBrick(prefs));
		}
        res.add(new ForumsBrick(prefs));
        
		return res;
		
    }
	
	public static ArrayList<BrickInfo> getLoginMenu(){
		ArrayList<BrickInfo> res = new ArrayList<BrickInfo>();
		res.add(new LoginBrick());
		return res;
	}
	
	public static ArrayList<BrickInfo> getLogoutMenu(){
		ArrayList<BrickInfo> res = new ArrayList<BrickInfo>();
		res.add(new ProfileBrick());
		res.add(new LogoutBrick());
		return res;
	}
}
