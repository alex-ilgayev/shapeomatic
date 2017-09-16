package com.shapeomatic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shapeomatic.Controller.DAL;
import com.shapeomatic.Controller.LevelContainer;
import com.shapeomatic.Controller.Settings;
import com.shapeomatic.Controller.ShapeDrawableContainer;
import com.shapeomatic.Model.Level;
import com.shapeomatic.Model.Shape;
import com.shapeomatic.Model.ShapeType;
import com.shapeomatic.Model.User;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameView extends View {

    public static final String SHAPE_CLASS_NAME = "com.shapeomatic.Model.Shape";

    private static final List<ShapeType> VALUES =
            Collections.unmodifiableList(Arrays.asList(ShapeType.values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    private boolean mGameOn = false;

    private SharedPreferences mPrefs;

    private boolean mHasInit = false;

    private ArrayList<View> mHiddenViews;
    private ArrayList<View> mMainViews;

    private Typeface mFont;

    private Vibrator mVibrator;

    private ArrayList<Shape> mGame;
    private int mWidth;
    private int mHeight;
    private int mCurrPickIdx;
    private ArrayList<Shape> mPickingList;
    private ShapePickerView mPickerView;
    private ArrayList<ShapeType> mAvailableShapes = new ArrayList<>(Arrays.asList(ShapeType.values()));

    private int mLevel = 0;

    private int mLives = Settings.MAX_LIVES;
    private TextView mTvLives;

    private int mScore = 0;

//    private TextView mTvLevel;

    private int mHearts = Settings.MAX_HEARTS;
    private LinearLayout mLvHearts;

    private ArrayList<Shape> mAnimGone;

    private ImageView mBtnLeft;
    private ImageView mBtnRight;

    private Animation mAnimBtnClickLeft;
    private Animation mAnimBtnUpLeft;
    private Animation mAnimBtnClickRight;
    private Animation mAnimBtnUpRight;

    private Handler mTimerHandler;
    private TimerRunnable mTimerRunnable;

    private Handler mLevelHandler;
    private LevelRunnable mLevelRunnable;

    private List<View> mViewsToColor;

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // some reflection code.
        Constructor<Shape> shapeConstr;
        Class shape;
        try {
            shape = Class.forName(SHAPE_CLASS_NAME);
            shapeConstr = shape.getConstructor(int.class, int.class, int.class,
                    ShapeType.class);
            Shape.SHAPE_CONSTR = shapeConstr;
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
        } catch(NoSuchMethodException e) {
            e.printStackTrace();
        }

        mGame = new ArrayList<>();
        mAnimGone = new ArrayList<>();
        mPickingList = new ArrayList<>();
        mCurrPickIdx = (-1);

        mPrefs = getContext().getSharedPreferences(GameActivity.PREF_TAG, Context.MODE_PRIVATE);

        mVibrator = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);

        mFont = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/YehudaCLM-Light.ttf");
        Settings.FONT = mFont;

        Settings.TIMER_INTERVAL_MILLIS = Settings.TIMER_INTERVAL_MILLIS_DEFAULT;

        mTimerHandler = new Handler();
        mTimerRunnable = new TimerRunnable(mTimerHandler);
//        mTimerHandler.postDelayed(mTimerRunnable, Settings.TIMER_INTERVAL_MILLIS);

        mLevelHandler = new Handler();
        mLevelRunnable = new LevelRunnable(mLevelHandler);

        // TODO: change if shape added.
        ShapeDrawableContainer container = ShapeDrawableContainer.getInstance();
        container.setShapeDrawable(ShapeType.SQUARE, getResources().getDrawable(R.drawable.shape_square));
        container.setShapeDrawable(ShapeType.SQUARE_2, getResources().getDrawable(R.drawable.shape_square_2));
        container.setShapeDrawable(ShapeType.CIRCLE, getResources().getDrawable(R.drawable.shape_circle));
        container.setShapeDrawable(ShapeType.CIRCLE_2, getResources().getDrawable(R.drawable.shape_circle_2));
        container.setShapeDrawable(ShapeType.TRIANGLE_1, getResources().getDrawable(R.drawable.shape_triangle));
        container.setShapeDrawable(ShapeType.TRIANGLE_2, getResources().getDrawable(R.drawable.shape_triangle_2));
        container.setShapeDrawable(ShapeType.STAR, getResources().getDrawable(R.drawable.shape_star));
        container.setShapeDrawable(ShapeType.STAR_2, getResources().getDrawable(R.drawable.shape_star_2));
        container.setShapeDrawable(ShapeType.OCTAGON, getResources().getDrawable(R.drawable.shape_octagon));
        container.setShapeDrawable(ShapeType.DIAMOND, getResources().getDrawable(R.drawable.shape_diamond));
        container.setShapeDrawable(ShapeType.CYLINDER, getResources().getDrawable(R.drawable.shape_cylinder));
        container.setShapeDrawable(ShapeType.PYRAMID, getResources().getDrawable(R.drawable.shape_pyramid));
        container.setShapeDrawable(ShapeType.STAR_3, getResources().getDrawable(R.drawable.shape_star_3));
    }

    private void initExternalViews() {
        mPickerView = (ShapePickerView)  (this.getRootView()).findViewById(R.id.viewPicker);

        mCurrPickIdx = 0;
//
        for(int i = 0; i< ShapeType.values().length; i++) {
            Shape p = generateNewRandomShape(false);
            addPickingShape(p);
        }

//        mTvLevel = (TextView) (this.getRootView()).findViewById(R.id.tvLevel);
//        mTvLevel.setTypeface(mFont);
        mTvLives = (TextView) (this.getRootView()).findViewById(R.id.tvLives);
        mTvLives.setTypeface(mFont);

        mLvHearts = (LinearLayout) (this.getRootView()).findViewById(R.id.lvHearts);

        mBtnLeft = (ImageView) (this.getRootView()).findViewById(R.id.btnLeft);
        mBtnRight = (ImageView) (this.getRootView()).findViewById(R.id.btnRight);

        mAnimBtnClickLeft = AnimationUtils.loadAnimation(this.getContext(), R.anim.anim_btn_click);
        mAnimBtnUpLeft = AnimationUtils.loadAnimation(this.getContext(), R.anim.anim_btn_up);
        mAnimBtnClickRight = AnimationUtils.loadAnimation(this.getContext(), R.anim.anim_btn_click);
        mAnimBtnUpRight = AnimationUtils.loadAnimation(this.getContext(), R.anim.anim_btn_up);

        mBtnLeft.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mBtnLeft.startAnimation(mAnimBtnClickLeft);

                        movePickerLeft();
                        break;
                    case MotionEvent.ACTION_UP:
                        mBtnLeft.startAnimation(mAnimBtnUpLeft);
                        break;
                }

                return true;
            }
        });

        mBtnRight.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mBtnRight.startAnimation(mAnimBtnClickRight);

                        movePickerRight();
                        break;
                    case MotionEvent.ACTION_UP:
                        mBtnRight.startAnimation(mAnimBtnUpRight);

                        break;
                }

                return true;
            }
        });

        Button startBtn = (Button) (this.getRootView()).findViewById(R.id.main1);
        startBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewGame();

            }
        });

        mHiddenViews = new ArrayList<>();
        mHiddenViews.add((this.getRootView()).findViewById(R.id.hidden1));
        mHiddenViews.add((this.getRootView()).findViewById(R.id.hidden2));
        mHiddenViews.add((this.getRootView()).findViewById(R.id.hidden3));
        mHiddenViews.add((this.getRootView()).findViewById(R.id.hidden4));

        mMainViews = new ArrayList<>();
        mMainViews.add((this.getRootView()).findViewById(R.id.main1));
        mMainViews.add((this.getRootView()).findViewById(R.id.btnLeaderboard));
        mMainViews.add((this.getRootView()).findViewById(R.id.btnFacebookLogin));

        mViewsToColor = new ArrayList<>();
        mViewsToColor.add((this.getRootView()).findViewById(R.id.color1));
        mViewsToColor.add((this.getRootView()).findViewById(R.id.color2));
        mViewsToColor.add((this.getRootView()).findViewById(R.id.color3));
        mViewsToColor.add((this.getRootView()).findViewById(R.id.color4));
        mViewsToColor.add((this.getRootView()).findViewById(R.id.hidden2));
        mViewsToColor.add((this.getRootView()).findViewById(R.id.hidden4));

        mHasInit = true;

        mLevel = 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = false;
        int actionType = event.getAction();
        int actionIdx = event.getActionIndex();

        if (actionType == MotionEvent.ACTION_DOWN || actionType == MotionEvent.ACTION_POINTER_DOWN
                || actionType == MotionEvent.ACTION_POINTER_1_DOWN || actionType == MotionEvent.ACTION_POINTER_2_DOWN
                || actionType == MotionEvent.ACTION_POINTER_3_DOWN) {
            float x,y;
            if(event.getAction() != MotionEvent.ACTION_DOWN) {
                x = event.getX(actionIdx);
                y = event.getY(actionIdx);
            }
            else {
                x = event.getX();
                y = event.getY();
            }
            Shape p = findTouchedShape(x, y);
            if(p == null)
                return false;

            removeShape(p);

            result = true;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mGameOn) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(5);
            paint.setTextSize(400);
            paint.setColor(Color.argb(80, 200, 200, 200));
            if(mScore >= 100)
                canvas.drawText(String.valueOf(mScore), mWidth/2-330, mHeight/2+100, paint);
            else if(mScore >= 10) {
                canvas.drawText(String.valueOf(mScore), mWidth/2-220, mHeight/2+100, paint);
            } else {
                canvas.drawText(String.valueOf(mScore), mWidth/2-100, mHeight/2+100, paint);
            }

        }

        for(Shape p: mGame) {
            p.draw(canvas);

            int percent = p.getLoadingPercent();
            if(percent < 100) {
                percent += Settings.SCALE_STEPS;
                if(percent > 100)
                    percent = 100;
                p.setLoadingPercent(percent);
            }
        }

        for(Iterator<Shape> it = mAnimGone.listIterator(); it.hasNext();) {
            Shape p = it.next();

            p.draw(canvas);

            int percent = p.getLoadingPercent();
            percent -= Settings.DESCALE_STEPS;
            if(percent <= 0)
                it.remove();
            else
                p.setLoadingPercent(percent);
        }

        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }

    private void addShape(Shape p) {
        mLives--;
        updateLives();

        if(mLives == 0) {
            endGame();
        }

        mGame.add(p);

        invalidate();
    }

    private void addPickingShape(Shape p) {
        mPickingList.add(p);
        mPickerView.addShape(p);
        mPickerView.invalidate();
    }

    private void removeShape(Shape p) {
        if(mGameOn && p.getShapeType() != mPickingList.get(mCurrPickIdx).getShapeType()) {
            failTouch();
            return;
        }

        mLives++;
        updateLives();

        mGame.remove(p);
        mAnimGone.add(p);

        calcScore();

        invalidate();
    }

    private Shape generateNewRandomShape(boolean allowSameShape) {

        ShapeType type;
        if(allowSameShape)
            type = VALUES.get(RANDOM.nextInt(SIZE));
        else
            type = mAvailableShapes.get(RANDOM.nextInt(mAvailableShapes.size()));
        if(mAvailableShapes.contains(type))
            mAvailableShapes.remove(type);

        Shape p = null;

        try {
            p = Shape.SHAPE_CONSTR.newInstance(0, 0, 0, type);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }

        setRandomCoordinates(p, false);
        return p;
    }

    private void setRandomCoordinates(Shape p, boolean checkProximity) {
        if(p == null)
            return;
        boolean tooClose;
        int x, y, grad;
        int xMin = Settings.RADIUS + getPaddingLeft(), xMax = mWidth - getPaddingRight() - Settings.RADIUS;
        int yMin = Settings.RADIUS + getPaddingTop(), yMax = mHeight - getPaddingBottom() - Settings.RADIUS;
        int sX, sY;
        double dx, dy;
        int distance;
        do {
            x = RANDOM.nextInt(xMax-xMin) + xMin;
            y = RANDOM.nextInt(yMax-yMin) + yMin;

            tooClose = false;
            if(checkProximity) {
                for(Shape shape: mGame) {
                    sX = shape.getCenterX();
                    sY = shape.getCenterY();
                    dx = (sX-x)*(sX-x);
                    dy = (sY-y)*(sY-y);
                    distance = (int)Math.sqrt(dx+dy);
                    if(distance < Settings.RADIUS) {
                        tooClose = true;
                        break;
                    }
                }
            }
        } while(tooClose == true);

        grad = RANDOM.nextInt(360);

        p.setCenterX(x);
        p.setCenterY(y);
        p.setGrad(grad);
    }

    private Shape generateRandomShapeFromPickingList() {
        if(mHasInit == false)
            initExternalViews();

        int randomIDx = RANDOM.nextInt(mPickingList.size());
        Shape p = mPickingList.get(randomIDx).clone();
        setRandomCoordinates(p, true);

        return p;
    }

    private Shape findTouchedShape(float x, float y) {
        Shape chosen = null;

        double dx, dy;

        for(Shape p: mGame) {
            dx = Math.pow(x - p.getCenterX(), 2);
            dy = Math.pow(y - p.getCenterY(), 2);

            if ((dx + dy) < Math.pow(Settings.RADIUS, 2)) {
                chosen = p;
            }
        }

        return chosen;
    }

    private void calcScore() {
        mScore += Settings.SCORE_STEPS;
    }

    private void updateLives() {
        mTvLives.setText(String.valueOf(mLives));
    }

    private void restart() {
        mGame.clear();
        mLives = Settings.MAX_LIVES;
        updateLives();
        mLevel = 0;
        mScore = 0;
        mPickerView.resetShapes();
        mPickingList.clear();
        mAvailableShapes = new ArrayList<>(Arrays.asList(ShapeType.values()));


//
//        p = generateNewRandomShape(false);
//        addPickingShape(p);
//
//        p = generateNewRandomShape(false);
//        addPickingShape(p);
//
//        p = generateNewRandomShape(false);
//        addPickingShape(p);

        invalidate();
    }

    private void endGame() {
        if(!mGameOn) {
            restart();
            for(int i = 0; i< ShapeType.values().length; i++) {
                Shape p = generateNewRandomShape(false);
                addPickingShape(p);
            }
            return;
        }

        mTimerHandler.removeCallbacks(mTimerRunnable);
        mTimerRunnable.kill();

        mLevelHandler.removeCallbacks(mLevelRunnable);
        mLevelRunnable.kill();

//        User user = DAL.getInstance().getUser(mPrefs, GameActivity.PREF_TAG_USER);
//        if(mScore > user.getScore()) {
//            user.setScore(mScore);
//            DAL.getInstance().putUser(mPrefs, GameActivity.PREF_TAG_USER, user);
//        }

        Intent intent = new Intent(getContext(), EndGameActivity.class);
        intent.putExtra(EndGameActivity.TAG_FINAL_SCORE, mScore);
        getContext().startActivity(intent);
     }

    private void movePickerLeft() {
        mCurrPickIdx = (mCurrPickIdx + 1) % mPickingList.size();

        mPickerView.moveLeft();
    }

    private void movePickerRight() {
        mCurrPickIdx  = (mCurrPickIdx == 0 ? mPickingList.size()-1 : mCurrPickIdx - 1);

        mPickerView.moveRight();
    }

    private void failTouch() {
        mVibrator.vibrate(Settings.VIBRATION_DURATION_MILLIS);

        if(mHearts == 0) {
            endGame();
            return;
        }


        View heart = mLvHearts.getChildAt(Settings.MAX_HEARTS - mHearts);
        heart.setVisibility(INVISIBLE);

        mHearts--;
    }

    private void startNewGame() {
        for(View v : mMainViews) {
            v.setVisibility(INVISIBLE);
        }

        for(View v: mHiddenViews) {
            v.setVisibility(VISIBLE);
        }

        restart();
        Shape p = generateNewRandomShape(false);
        addPickingShape(p);

        mGameOn = true;
        moveNextLevel();

        mTimerHandler.postDelayed(mLevelRunnable, Settings.LEVEL_INTERVAL_MILLIES);

        invalidate();
    }

    private void moveNextLevel() {
        mLevel ++;

        Level newLevel = LevelContainer.getInstance().getLevel(mLevel);

        // adding new shapes.
        for(int i=0; i<newLevel.getAddedShapes(); i++) {
            addPickingShape(generateNewRandomShape(false));
        }

        // reducing time interval
        Settings.TIMER_INTERVAL_MILLIS -= newLevel.getDecreasedShapeRefreshMillies();

        showLevel(mLevel);
    }

    private void showLevel(int level) {
//        mTvLevel.setText(String.valueOf(level));


        Level currLevel = LevelContainer.getInstance().getLevel(level-1);
        Level nextLevel = LevelContainer.getInstance().getLevel(level);

        for(View v : mViewsToColor){
            TransitionDrawable trans = new TransitionDrawable(new Drawable[]{getResources().getDrawable(currLevel.getColorRes()),
                    getResources().getDrawable(nextLevel.getColorRes())});
            v.setBackground(trans);
            trans.startTransition(Settings.LEVEL_CROSSFADE_MILLIES);
        }
        invalidate();
    }

    public void onBackPressed() {
        Intent intent = new Intent(getContext(), ExitPromptActivity.class);
        intent.putExtra(ExitPromptActivity.TAG_MAIN_MENU, mGameOn);
        getContext().startActivity(intent);
    }

    public void onPause() {
        mTimerHandler.removeCallbacks(mTimerRunnable);
        mTimerRunnable.kill();

        mLevelHandler.removeCallbacks(mLevelRunnable);
        mLevelRunnable.kill();
    }

    public void onResume() {
        mTimerRunnable = new TimerRunnable(mTimerHandler);
        mTimerHandler.postDelayed(mTimerRunnable, Settings.TIMER_INTERVAL_MILLIS);

        mLevelRunnable = new LevelRunnable(mLevelHandler);
        if(mGameOn)
            mTimerHandler.postDelayed(mLevelRunnable, Settings.LEVEL_INTERVAL_MILLIES);
    }

    private class TimerRunnable implements Runnable{

        private Handler handler;
        private boolean isKilled=false;

        public TimerRunnable(Handler handler) {
            this.handler = handler;
        }

        @Override
        final public void run(){
            if(isKilled)
                return;

            Shape p = generateRandomShapeFromPickingList();

            addShape(p);

            if(isKilled)
                return;

            handler.postDelayed(this, Settings.TIMER_INTERVAL_MILLIS);
        }

        public void kill(){
            isKilled=true;
        }
    }

    private class LevelRunnable implements Runnable{

        private Handler handler;
        private boolean isKilled=false;

        public LevelRunnable(Handler handler) {
            this.handler = handler;
        }

        @Override
        final public void run(){
            if(isKilled)
                return;

            moveNextLevel();

            if(isKilled)
                return;

            handler.postDelayed(this, Settings.LEVEL_INTERVAL_MILLIES);
        }

        public void kill(){
            isKilled=true;
        }
    }
}
