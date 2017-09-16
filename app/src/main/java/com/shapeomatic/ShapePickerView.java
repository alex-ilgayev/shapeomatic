package com.shapeomatic;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.shapeomatic.Model.Shape;

import java.util.ArrayList;
import java.util.HashMap;

public class ShapePickerView extends View {

    private int mWidth;
    private int mHeight;

    private ArrayList<Shape> mPickingList;
    private int mCurrIdx;

    // indexes from -2 to 2.
    private HashMap<Integer, Shape> mCurrentShown;

    public ShapePickerView(Context context) {
        super(context);
        init();
    }

    public ShapePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPickingList = new ArrayList<>();
        mCurrIdx = (-1);

        mCurrentShown = new HashMap<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mPickingList.isEmpty())
            return;

        Shape p = mCurrentShown.get(0);
        p.drawSampleMid(canvas);

        Shape rightShape = mCurrentShown.get(1);
        Shape leftShape = mCurrentShown.get(-1);

        rightShape.drawSampleRight(canvas);
        leftShape.drawSampleLeft(canvas);

        Shape rightRightShape = mCurrentShown.get(2);
        Shape leftLeftShape = mCurrentShown.get(-2);

        rightRightShape.drawSampleRightRight(canvas);
        leftLeftShape.drawSampleLeftLeft(canvas);

        invalidate();
    }

    public void moveLeft() {
        if(mPickingList.isEmpty())
            return;

        mCurrentShown.get(1).setMovingAnimationFromRight();
        mCurrentShown.get(0).setMovingAnimationFromMid();
        mCurrentShown.get(-1).setMovingAnimationFromLeft();
        mCurrentShown.get(2).setRightAppearingAnimation();

        mCurrentShown.put(-2, mCurrentShown.get(-1));
        mCurrentShown.put(-1, mCurrentShown.get(0));
        mCurrentShown.put(0, mCurrentShown.get(1));
        mCurrentShown.put(1, mCurrentShown.get(2));

        mCurrIdx = (mCurrIdx + 1) % mPickingList.size();

        int nextNextIdx = (mCurrIdx + 2) % mPickingList.size();

        mCurrentShown.put(2, mPickingList.get(nextNextIdx).clone());

    }

    public void moveRight() {
        if(mPickingList.isEmpty())
            return;

        mCurrentShown.get(1).setMovingAnimationFromRight();
        mCurrentShown.get(0).setMovingAnimationFromMid();
        mCurrentShown.get(-1).setMovingAnimationFromLeft();
        mCurrentShown.get(-2).setLeftAppearingAnimation();

        mCurrentShown.put(2, mCurrentShown.get(1));
        mCurrentShown.put(1, mCurrentShown.get(0));
        mCurrentShown.put(0, mCurrentShown.get(-1));
        mCurrentShown.put(-1, mCurrentShown.get(-2));

        mCurrIdx = (mCurrIdx == 0 ? mPickingList.size() - 1 : mCurrIdx - 1);

        int prevIdx = mCurrIdx == 0 ? mPickingList.size()-1 : mCurrIdx-1;
        int prevPrevIdx = prevIdx == 0 ? mPickingList.size()-1 : prevIdx-1;

        mCurrentShown.put(-2, mPickingList.get(prevPrevIdx).clone());
    }

    public void addShape(Shape p) {
        if(mPickingList.isEmpty())
            mCurrIdx = 0;
        mPickingList.add(p);

        int nextIdx = (mCurrIdx + 1) % mPickingList.size();
        int prevIdx = mCurrIdx == 0 ? mPickingList.size()-1 : mCurrIdx-1;
        int prevPrevIdx = prevIdx == 0 ? mPickingList.size()-1 : prevIdx-1;
        int nextNextIdx = (mCurrIdx + 2) % mPickingList.size();

        mCurrentShown.put(0, mPickingList.get(mCurrIdx).clone());
        mCurrentShown.put(1, mPickingList.get(nextIdx).clone());
        mCurrentShown.put(2, mPickingList.get(nextNextIdx).clone());
        mCurrentShown.put(-1, mPickingList.get(prevIdx).clone());
        mCurrentShown.put(-2, mPickingList.get(prevPrevIdx).clone());
    }

    public void resetShapes() {
        mPickingList.clear();
        mCurrIdx = (-1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }
}
