//package com.shapeomatic.Model;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapShader;
//import android.graphics.BlurMaskFilter;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.EmbossMaskFilter;
//import android.graphics.LinearGradient;
//import android.graphics.Paint;
//import android.graphics.RadialGradient;
//import android.graphics.Rect;
//import android.graphics.Shader;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//
//import com.shapeomatic.Controller.Settings;
//import com.shapeomatic.Controller.ShapeDrawableContainer;
//
///**
// * Created by Alex on 5/4/2016.
// */
//public class CircleShape extends Shape{
//
//    public CircleShape(int centerX, int centerY, int grad, Paint p) {
//        super(centerX, centerY, grad, p);
//        mType = ShapeType.CIRCLE;
//    }
//
////    @Override
////    public void draw(Canvas c){
////        int scalePercent = getLoadingPercent();
////
////        double radius = Settings.RADIUS;
//////        int actualR = (int) Math.sqrt(radius*radius/2.0);
////        int actualR = (int) radius;
////        int actualRadius = (int)(actualR*(((float)scalePercent)/100));
////        Rect r = new Rect(getCenterX()-actualRadius, getCenterY()-actualRadius
////                , getCenterX()+actualRadius, getCenterY()+actualRadius);
////        c.save();
////        c.rotate(getGrad(), getCenterX(), getCenterY());
//////        c.drawRect(r, getPaint());
////        Drawable d = ShapeDrawableContainer.getInstance().getShapeDrawable(getShapeType());
////        d.setBounds(r);
////        d.draw(c);
////        c.restore();
////
//////        int scalePercent = getLoadingPercent();
//////        int actualRadius = (int)(Settings.RADIUS*(((float)scalePercent)/100));
//////
////////        EmbossMaskFilter filter = new EmbossMaskFilter(new float[]{1, 1, 1} , 0.4f, 10, 8.2f);
////////        getPaint().setMaskFilter(filter);
//////
//////        c.drawCircle(getCenterX(), getCenterY(),actualRadius, getPaint());
////    }
//
//    @Override
//    public Shape clone() {
//        CircleShape p = new CircleShape(getCenterX(), getCenterY(), getGrad(), getPaint());
//        return p;
//    }
//}
