package com.shapeomatic.Model;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.shapeomatic.Controller.Settings;
import com.shapeomatic.Controller.ShapeDrawableContainer;

import java.lang.reflect.Constructor;

/**
 * Created by Alex on 5/4/2016.
 */
public class Shape implements Cloneable {

    public static Constructor<Shape> SHAPE_CONSTR;

    private static final float MID_SIZE = 0.8f;
    private static final float LEFT_RIGHT_SIZE = 0.5f;
    private static final float X_LEFT = 0.2f;
    private static final float X_RIGHT = 0.8f;
    private static final float X_LEFT_LEFT = 0.01f;
    private static final float X_RIGHT_RIGHT = 0.99f;

    private int mCenterX;
    private int mCenterY;
    private int mGrad; // 0-360
    protected ShapeType mType;
    private int mLoadingPercent;

    private float mAnimationPercent = 100;
    private float mSrcPos = 0;
//    private float mDstPos;
    private float mSrcScale = 0;
//    private float mDstScale;

    public Shape(int centerX, int centerY, int grad, ShapeType type) {
        this.mCenterX = centerX;
        this.mCenterY = centerY;
        this.mGrad = grad;
        this.mLoadingPercent = Settings.SCALE_STEPS;
        this.mType = type;
    }

    public void draw(Canvas c) {
        int scalePercent = getLoadingPercent();

        int actualRadius = (int)(Settings.RADIUS*(((float)scalePercent)/100));
        Rect r = new Rect(getCenterX()-actualRadius, getCenterY()-actualRadius
                , getCenterX()+actualRadius, getCenterY()+actualRadius);
        c.save();
        c.rotate(getGrad(), getCenterX(), getCenterY());
        Drawable d = ShapeDrawableContainer.getInstance().getShapeDrawable(getShapeType());
        d.setBounds(r);
        d.draw(c);
        c.restore();
    }

    public void setCenterX(int centerX) {
        this.mCenterX = centerX;
    }

    public int getCenterX() {
        return mCenterX;
    }

    public void setCenterY(int centerY) {
        this.mCenterY = centerY;
    }

    public int getCenterY() {
        return mCenterY;
    }

    public void setGrad(int grad) {
        this.mGrad = grad;
    }

    public int getGrad() {
        return mGrad;
    }

    public ShapeType getShapeType() {
        return mType;
    }

    public void setLoadingPercent(int loadingPercent) {
        this.mLoadingPercent = loadingPercent;
    }

    public int getLoadingPercent() {
        return mLoadingPercent;
    }

    public void drawSampleMid(Canvas c) {
        int height = c.getHeight();
        int width = c.getWidth();

        float centerX = (0.5f - mSrcPos)*(mAnimationPercent/100) + mSrcPos;
        float scale = (MID_SIZE - mSrcScale)*(mAnimationPercent/100) + mSrcScale;
        mAnimationPercent = mAnimationPercent == 100 ? mAnimationPercent: mAnimationPercent + Settings.PICKER_ANIMATION_SCALE_STEP;

        setCenterX((int)(width * centerX));
        setCenterY(height/2);

        setGrad(0);

        float diameter = Settings.RADIUS*2;
        float wantedDiamater = (float)(Math.min(height, width) * scale);

        float dif = wantedDiamater / diameter;
        int finalPercent = (int)(dif*100);
        setLoadingPercent(finalPercent);
        draw(c);
    }

    public void drawSampleRight(Canvas c) {
        int height = c.getHeight();
        int width = c.getWidth();

        float centerX = (X_RIGHT - mSrcPos)*(mAnimationPercent/100) + mSrcPos;
        float scale = (LEFT_RIGHT_SIZE - mSrcScale)*(mAnimationPercent/100) + mSrcScale;
        mAnimationPercent = mAnimationPercent == 100 ? mAnimationPercent: mAnimationPercent + Settings.PICKER_ANIMATION_SCALE_STEP;

        setCenterX((int)(width * centerX));
        setCenterY(height/2);

        setGrad(0);

        float diameter = Settings.RADIUS*2;
        float wantedDiamater = (Math.min(height, width) * scale);

        float dif = wantedDiamater / diameter;
        int finalPercent = (int)(dif*100);
        setLoadingPercent(finalPercent);
        draw(c);
    }

    public void drawSampleLeft(Canvas c) {
        int height = c.getHeight();
        int width = c.getWidth();

        float centerX = (X_LEFT - mSrcPos)*(mAnimationPercent/100) + mSrcPos;
        float scale = (LEFT_RIGHT_SIZE - mSrcScale)*(mAnimationPercent/100) + mSrcScale;
        mAnimationPercent = mAnimationPercent == 100 ? mAnimationPercent: mAnimationPercent + Settings.PICKER_ANIMATION_SCALE_STEP;

        setCenterX((int)(width * centerX));
        setCenterY(height/2);

        setGrad(0);

        float diameter = Settings.RADIUS*2;
        float wantedDiamater = (Math.min(height, width) * scale);

        float dif = wantedDiamater / diameter;
        int finalPercent = (int)(dif*100);
        setLoadingPercent(finalPercent);
        draw(c);
    }

    public void drawSampleLeftLeft(Canvas c) {
        int height = c.getHeight();
        int width = c.getWidth();

        float centerX = (X_LEFT_LEFT - mSrcPos) * (mAnimationPercent / 100) + mSrcPos;
        float scale = (0 - mSrcScale) * (mAnimationPercent / 100) + mSrcScale;
        mAnimationPercent = mAnimationPercent == 100 ? mAnimationPercent : mAnimationPercent + Settings.PICKER_ANIMATION_SCALE_STEP;

        setCenterX((int) (width * centerX));
        setCenterY(height / 2);

        setGrad(0);

        float diameter = Settings.RADIUS * 2;
        float wantedDiamater = (Math.min(height, width) * scale);

        float dif = wantedDiamater / diameter;
        int finalPercent = (int) (dif * 100);
        setLoadingPercent(finalPercent);
        draw(c);
    }

    public void drawSampleRightRight(Canvas c) {
        int height = c.getHeight();
        int width = c.getWidth();

        float centerX = (X_RIGHT_RIGHT - mSrcPos) * (mAnimationPercent / 100) + mSrcPos;
        float scale = (0 - mSrcScale) * (mAnimationPercent / 100) + mSrcScale;
        mAnimationPercent = mAnimationPercent == 100 ? mAnimationPercent : mAnimationPercent + Settings.PICKER_ANIMATION_SCALE_STEP;

        setCenterX((int) (width * centerX));
        setCenterY(height / 2);

        setGrad(0);

        float diameter = Settings.RADIUS * 2;
        float wantedDiamater = (Math.min(height, width) * scale);

        float dif = wantedDiamater / diameter;
        int finalPercent = (int) (dif * 100);
        setLoadingPercent(finalPercent);
        draw(c);
    }

    public void setMovingAnimationFromLeft() {
        mAnimationPercent = 0;
        mSrcPos = X_LEFT;
        mSrcScale = LEFT_RIGHT_SIZE;
    }

    public void setMovingAnimationFromMid() {
        mAnimationPercent = 0;
        mSrcPos = 0.5f;
        mSrcScale = MID_SIZE;
    }

    public void setMovingAnimationFromRight() {
        mAnimationPercent = 0;
        mSrcPos = X_RIGHT;
        mSrcScale = LEFT_RIGHT_SIZE;
    }

    public void setRightAppearingAnimation() {
        mAnimationPercent = 0;
        mSrcPos = 1.0f;
        mSrcScale = 0;
    }

    public void setLeftAppearingAnimation() {
        mAnimationPercent = 0;
        mSrcPos = 0;
        mSrcScale = 0;
    }

    @Override
    public Shape clone() {
        try {
            return SHAPE_CONSTR.newInstance(getCenterX(), getCenterY(), getGrad(),
                    getShapeType());
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
