package com.shapeomatic.Controller;

import android.graphics.drawable.Drawable;

import com.shapeomatic.Model.Level;
import com.shapeomatic.Model.ShapeType;
import com.shapeomatic.Controller.Settings;
import com.shapeomatic.R;

import java.util.HashMap;

/**
 * Created by Alex on 5/7/2016.
 */
public class LevelContainer {
    private static LevelContainer _ins = null;

    // starts with 1 shape.
    // level 2 is the first color transition.
    // advised to have one shape added then.
    private static final Level LEVEL0 = new Level(R.color.levelColor1, 0, 0);
    private static final Level LEVEL1 = new Level(R.color.levelColor1, 0, 0);
    private static final Level LEVEL2 = new Level(R.color.levelColor2, 1, 0);
    private static final Level LEVEL3 = new Level(R.color.levelColor3, 0, 0);
    private static final Level LEVEL4 = new Level(R.color.levelColor4, 1, 50);
    private static final Level LEVEL5 = new Level(R.color.levelColor5, 0, 0);
    private static final Level LEVEL6 = new Level(R.color.levelColor6, 1, 50);
    private static final Level LEVEL7 = new Level(R.color.levelColor7, 0, 0);
    private static final Level LEVEL8 = new Level(R.color.levelColor8, 1, 0);
    private static final Level LEVEL9 = new Level(R.color.levelColor9, 0, 0);
    private static final Level LEVEL10 = new Level(R.color.levelColor10, 1, 50);
    private static final Level LEVEL11 = new Level(R.color.levelColor1, 0, 0);
    private static final Level LEVEL12 = new Level(R.color.levelColor2, 0, 0);
    private static final Level LEVEL13 = new Level(R.color.levelColor3, 0, 50);
    private static final Level LEVEL14 = new Level(R.color.levelColor4, 1, 0);
    private static final Level LEVEL15 = new Level(R.color.levelColor5, 0, 0);
    private static final Level LEVEL16 = new Level(R.color.levelColor6, 0, 50);
    private static final Level LEVEL17 = new Level(R.color.levelColor7, 0, 0);
    private static final Level LEVEL18 = new Level(R.color.levelColor8, 1, 0);
    private static final Level LEVEL19 = new Level(R.color.levelColor9, 0, 0);
    private static final Level LEVEL20 = new Level(R.color.levelColor10, 0, 50);
    private static final Level LEVEL21 = new Level(R.color.levelColor1, 0, 0);
    private static final Level LEVEL22 = new Level(R.color.levelColor2, 1, 0);
    private static final Level LEVEL23 = new Level(R.color.levelColor3, 0, 50);
    private static final Level LEVEL24 = new Level(R.color.levelColor4, 0, 0);
    private static final Level LEVEL25 = new Level(R.color.levelColor5, 0, 0);
    private static final Level LEVEL26 = new Level(R.color.levelColor6, 1, 50);
    private static final Level LEVEL27 = new Level(R.color.levelColor7, 0, 0);
    private static final Level LEVEL28 = new Level(R.color.levelColor8, 0, 0);
    private static final Level LEVEL29 = new Level(R.color.levelColor9, 0, 0);
    private static final Level LEVEL30 = new Level(R.color.levelColor10, 1, 50);
    private static final Level LEVEL31 = new Level(R.color.levelColor1, 0, 0);
    private static final Level LEVEL32 = new Level(R.color.levelColor2, 0, 0);
    private static final Level LEVEL33 = new Level(R.color.levelColor3, 0, 0);
    private static final Level LEVEL34 = new Level(R.color.levelColor4, 1, 50);
    private static final Level LEVEL35 = new Level(R.color.levelColor5, 0, 0);
    private static final Level LEVEL36 = new Level(R.color.levelColor6, 0, 0);
    private static final Level LEVEL37 = new Level(R.color.levelColor7, 0, 0);
    private static final Level LEVEL38 = new Level(R.color.levelColor8, 1, 50);
    private static final Level LEVEL39 = new Level(R.color.levelColor9, 0, 0);
    private static final Level LEVEL40 = new Level(R.color.levelColor10, 0, 0);
    private static final Level LEVEL41 = new Level(R.color.levelColor1, 0, 0);
    private static final Level LEVEL42 = new Level(R.color.levelColor2, 0, 0);
    private static final Level LEVEL43 = new Level(R.color.levelColor3, 0, 0);

    private HashMap<Integer, Level> mLevelMap;

    public static LevelContainer getInstance() {
        if(_ins == null)
            _ins = new LevelContainer();
        return _ins;
    }

    private LevelContainer() {
        mLevelMap = new HashMap<>();
        mLevelMap.put(0, LEVEL0);
        mLevelMap.put(1, LEVEL1);
        mLevelMap.put(2, LEVEL2);
        mLevelMap.put(3, LEVEL3);
        mLevelMap.put(4, LEVEL4);
        mLevelMap.put(5, LEVEL5);
        mLevelMap.put(6, LEVEL6);
        mLevelMap.put(7, LEVEL7);
        mLevelMap.put(8, LEVEL8);
        mLevelMap.put(9, LEVEL9);
        mLevelMap.put(10, LEVEL10);
        mLevelMap.put(11, LEVEL11);
        mLevelMap.put(12, LEVEL12);
        mLevelMap.put(13, LEVEL13);
        mLevelMap.put(14, LEVEL14);
        mLevelMap.put(15, LEVEL15);
        mLevelMap.put(16, LEVEL16);
        mLevelMap.put(17, LEVEL17);
        mLevelMap.put(18, LEVEL18);
        mLevelMap.put(19, LEVEL19);
        mLevelMap.put(20, LEVEL20);
        mLevelMap.put(21, LEVEL21);
        mLevelMap.put(22, LEVEL22);
        mLevelMap.put(23, LEVEL23);
        mLevelMap.put(24, LEVEL24);
        mLevelMap.put(25, LEVEL25);
        mLevelMap.put(26, LEVEL26);
        mLevelMap.put(27, LEVEL27);
        mLevelMap.put(28, LEVEL28);
        mLevelMap.put(29, LEVEL29);
        mLevelMap.put(30, LEVEL30);
        mLevelMap.put(31, LEVEL31);
        mLevelMap.put(32, LEVEL32);
        mLevelMap.put(33, LEVEL33);
        mLevelMap.put(34, LEVEL34);
        mLevelMap.put(35, LEVEL35);
        mLevelMap.put(36, LEVEL36);
        mLevelMap.put(37, LEVEL37);
        mLevelMap.put(38, LEVEL38);
        mLevelMap.put(39, LEVEL39);
        mLevelMap.put(40, LEVEL40);
        mLevelMap.put(41, LEVEL41);
        mLevelMap.put(42, LEVEL42);
        mLevelMap.put(43, LEVEL43);
    }

    public Level getLevel(int num) {
        if(num > Settings.MAX_LEVEL)
            num = Settings.MAX_LEVEL;
        return mLevelMap.get(num);
    }
}
