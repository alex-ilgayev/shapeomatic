//package com.shapeomatic.Model;
//
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.graphics.Point;
//import android.graphics.Rect;
//
//import com.shapeomatic.Controller.Settings;
//
///**
// * Created by Alex on 5/4/2016.
// */
//public class TriangleShape extends Shape{
//
//    public TriangleShape(int centerX, int centerY, int grad, Paint p) {
//        super(centerX, centerY, grad, p);
//        mType = ShapeType.TRIANGLE;
//    }
////
////    @Override
////    public void draw(Canvas c){
////        getPaint().setStrokeWidth(10);
////        getPaint().setStyle(Paint.Style.STROKE);
////
////        int scalePercent = getLoadingPercent();
////
////        int actualRadius = (int)(Settings.RADIUS*(((float)scalePercent)/100));
////        int shortRadius = (int)((Math.sqrt(5.0)/2.0)*actualRadius);
////
////        Point p1, p2, p3;
////        p1 = new Point(getCenterX(), getCenterY() - actualRadius);
////        p2 = new Point(getCenterX() + shortRadius, getCenterY() + (actualRadius/2));
////        p3 = new Point(getCenterX() - shortRadius, getCenterY() + (actualRadius/2));
////
////        Path p = new Path();
////        p.setFillType(Path.FillType.EVEN_ODD);
////        p.moveTo(p1.x, p1.y);
////        p.lineTo(p2.x, p2.y);
////        p.lineTo(p3.x, p3.y);
////        p.lineTo(p1.x, p1.y);
////        p.close();
////
////        c.save();
////        c.rotate(getGrad(), getCenterX(), getCenterY());
////        c.drawPath(p, getPaint());
////        c.restore();
////    }
//
//    @Override
//    public Shape clone() {
//        TriangleShape p = new TriangleShape(getCenterX(), getCenterY(), getGrad(), getPaint());
//        return p;
//    }
//}
