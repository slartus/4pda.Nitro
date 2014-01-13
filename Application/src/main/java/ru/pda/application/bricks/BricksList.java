package ru.pda.application.bricks;

import java.util.ArrayList;

/**
 * Created by slartus on 12.01.14.
 */
public class BricksList {
    /**
     * Список всех элементов из "быстрый запуск"
     * @return
     */
    public static ArrayList<BrickInfo> getBricks() {
        ArrayList<BrickInfo> res = new ArrayList<BrickInfo>();
        res.add(new NewsBrick());
        res.add(new FavoritesBrick());
        res.add(new ForumsBrick());
        
		return res;
		
    }
}
