//package com.shapeomatic.Model;
//
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.graphics.drawable.Drawable;
//
//import com.shapeomatic.Controller.Settings;
//import com.shapeomatic.Controller.ShapeDrawableContainer;
//
///**
// * Created by Alex on 5/4/2016.
// */
//public class SquareShape extends Shape{
//
//    public SquareShape(int centerX, int centerY, int grad, Paint p) {
//        super(centerX, centerY, grad, p);
//        mType = ShapeType.SQUARE;
//    }
////
////    @Override
////    public void draw(Canvas c){
////        int scalePercent = getLoadingPercent();
////
////        double radius = Settings.RADIUS;
////        int actualR = (int) Math.sqrt(radius*radius/2.0);
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
////    }
//
//    @Override
//    public Shape clone() {
//        SquareShape p = new SquareShape(getCenterX(), getCenterY(), getGrad(), getPaint());
//        return p;
//    }
//}
