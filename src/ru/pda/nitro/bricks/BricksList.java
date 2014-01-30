package ru.pda.nitro.bricks;

import java.util.ArrayList;
import android.content.*;

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
        res.add(new FavoritesBrick(prefs));
        res.add(new ForumsBrick(prefs));
        
		return res;
		
    }
}
