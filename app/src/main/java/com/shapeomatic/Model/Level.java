package com.shapeomatic.Model;

/**
 * Created by Alex on 6/10/2016.
 */
public class Level {
    private int colorRes;
    private int addedShapes;
    private int decreasedShapeRefreshMillies;

    public Level(int colorRes, int addedShapes, int decreasedShapeRefresh) {
        this.colorRes = colorRes;
        this.addedShapes = addedShapes;
        this.decreasedShapeRefreshMillies = decreasedShapeRefresh;
    }

    public int getColorRes() {
        return this.colorRes;
    }

    public int getAddedShapes() {
        return this.addedShapes;
    }

    public double getDecreasedShapeRefreshMillies() {
        return this.decreasedShapeRefreshMillies;
    }
}
