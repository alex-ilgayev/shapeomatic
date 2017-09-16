package com.shapeomatic.Controller;

import android.graphics.Color;
import android.graphics.Paint;

import com.shapeomatic.Model.ShapeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Alex on 5/7/2016.
 */
public class Colors {
    private static Integer[] COLORS = { Color.YELLOW, Color.BLUE, Color.WHITE, Color.GREEN };
    private static Random RANDOM = new Random();

    private ArrayList<Integer> mNotYetChosen;
    private HashMap<Integer, Paint> mHashPaintList;

    public Colors() {
        mNotYetChosen = new ArrayList<>(Arrays.asList(COLORS.clone()));
        mHashPaintList = new HashMap<>();
        for(int color: COLORS) {
            Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
            p.setColor(color);
            mHashPaintList.put(color, p);
        }
    }

    private Paint getPaint(int color) {
        if(!mHashPaintList.keySet().contains(color))
            return null;
        return mHashPaintList.get(color);
    }

    public Paint getRandomPaint(boolean allowSameColor) {
        int color;
        if(!allowSameColor) {
            if(mNotYetChosen.size() == 0)
                return null;
            color = mNotYetChosen.get(RANDOM.nextInt(mNotYetChosen.size()));
        } else
            color = COLORS[RANDOM.nextInt(COLORS.length)];
        if(mNotYetChosen.contains(color))
            mNotYetChosen.remove(new Integer(color));

        return getPaint(color);
    }
}
