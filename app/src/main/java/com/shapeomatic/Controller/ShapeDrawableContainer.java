package com.shapeomatic.Controller;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.shapeomatic.Model.Shape;
import com.shapeomatic.Model.ShapeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Alex on 5/7/2016.
 */
public class ShapeDrawableContainer {
    private static ShapeDrawableContainer _ins = null;

    private HashMap<ShapeType, Drawable> mShapeTypeMap;

    public static ShapeDrawableContainer getInstance() {
        if(_ins == null)
            _ins = new ShapeDrawableContainer();
        return _ins;
    }

    private ShapeDrawableContainer() {
        mShapeTypeMap = new HashMap<>();
    }

    public void setShapeDrawable(ShapeType key, Drawable val) {
        this.mShapeTypeMap.put(key, val);
    }

    public Drawable getShapeDrawable(ShapeType type) {
        return mShapeTypeMap.get(type);
    }
}
