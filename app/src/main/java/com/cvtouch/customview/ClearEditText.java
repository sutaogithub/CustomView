package com.cvtouch.customview;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


/**
 * emailTo intern_zhangsutao@cvte.com
 *
 * @author zhangsutao
 * @brief 带清除叉叉的EditText
 * @date 2016/9/26
 */
public class ClearEditText extends EditText implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {
    private static final String TAG = "ClearEditText";
    private final int DEFAULT_HINT_COLOR=Color.GRAY;
    private final int DEFAULT_HINT_ICON_TEXT_INTERVAL=8;
    private final InputMethodManager inputMethodManager;
    private Drawable mClearTextIcon;
    private Drawable mSearchIcon;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnTouchListener mOnTouchListener;
    private Paint mPaint;
    private DisplayMetrics mDisplayMetrics;
    private int mIconSize;
    private int mHintTextColor;
    private float mHintTextSize;
    private String mHintText;
    private float mHintInitX;
    private float mHintInitY;
    private float mHintX;
    private float mHintY;
    private boolean isInit;

    public ClearEditText(Context context) {
        this(context,null);
    }

    public ClearEditText(final Context context, AttributeSet attrs) {
        super(context, attrs);
        inputMethodManager=(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        initAttrs(attrs,context);
        init(context);

    }

    private void initAttrs(AttributeSet attrs,Context context) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchClearEditText);
        mDisplayMetrics= context.getResources().getDisplayMetrics();
        mIconSize = (int) mTypedArray.getDimension(R.styleable.SearchClearEditText_hint_iconSize,getTextSize());
        mHintTextColor = mTypedArray.getColor(R.styleable.SearchClearEditText_hint_textColor,DEFAULT_HINT_COLOR);
        mHintTextSize = mTypedArray.getDimension(R.styleable.SearchClearEditText_hint_textSize,getTextSize());
        mHintText = mTypedArray.getString(R.styleable.SearchClearEditText_hintText);
        mTypedArray.recycle();
    }

    private void init(Context context){


        final Drawable drawable = ContextCompat.getDrawable(context, R.drawable.abc_ic_clear_mtrl_alpha);
        mSearchIcon=context.getDrawable(R.drawable.cvt1_ic_search);
        mSearchIcon.setBounds(0,0,mIconSize,mIconSize);

        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, getCurrentHintTextColor());
        mClearTextIcon = wrappedDrawable;
        mClearTextIcon.setBounds(0, 0, mClearTextIcon.getIntrinsicHeight(), mClearTextIcon.getIntrinsicHeight());

        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(this);

        mPaint=new Paint();
        mPaint.setTextSize(mHintTextSize);
        mPaint.setColor(mHintTextColor);

    }
    @Override
    public void setOnFocusChangeListener(final OnFocusChangeListener onFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void setOnTouchListener(final OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d(TAG, "beforeTextChanged: ");
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        if (isFocused()) {
            setClearIconVisible(text.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        Log.d(TAG, "afterTextChanged: ");
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        Log.d(TAG, "onFocusChange: "+hasFocus);
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(view, hasFocus);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if(motionEvent.getAction()==MotionEvent.ACTION_UP&&inputMethodManager.isActive()){
            startAnimation(mHintInitX,0);
        }
        final int x = (int) motionEvent.getX();
        if (mClearTextIcon.isVisible() && x > getWidth() - getPaddingRight() - mClearTextIcon.getIntrinsicWidth()) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                setText("");
            }
            return true;
        }

        return mOnTouchListener != null && mOnTouchListener.onTouch(view, motionEvent);
    }
    private void setClearIconVisible(final boolean visible) {
        mClearTextIcon.setVisible(visible, false);
        final Drawable[] compoundDrawables = getCompoundDrawables();
        setCompoundDrawables(
                compoundDrawables[0],
                compoundDrawables[1],
                visible ? mClearTextIcon : null,
                compoundDrawables[3]);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawHintText(canvas,mHintX,mHintY);
    }

    public void startAnimation(float start, float end){
        ValueAnimator animator = ValueAnimator.ofFloat(mHintInitX,0);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mHintX= (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        float textWidth = mPaint.measureText(mHintText);
        mHintInitX = (getWidth() - mIconSize - textWidth - DEFAULT_HINT_ICON_TEXT_INTERVAL) / 2;
        mHintInitY = (getHeight() - mSearchIcon.getIntrinsicWidth()) / 2;
        if(!isInit){
            mHintX=mHintInitX;
            mHintY=mHintInitY;
            isInit=!isInit;
        }else {
            if(inputMethodManager.isActive()){
                Log.d(TAG, "onLayout: active");
            }else {
                Log.d(TAG, "onLayout: active");

            }
        }

    }

    private void drawHintText(Canvas canvas,float x,float y) {
        if (this.getText().toString().length() == 0) {
            float textHeight = getFontLeading(mPaint);
            canvas.save();
            canvas.translate(getScrollX() + x, getScrollY() + y);
            if (mSearchIcon != null) {
                mSearchIcon.draw(canvas);
            }
            canvas.drawText(mHintText, getScrollX() + mIconSize + DEFAULT_HINT_ICON_TEXT_INTERVAL, getScrollY() + (getHeight() - (getHeight() - textHeight) / 2) - mPaint.getFontMetrics().bottom - y, mPaint);
            canvas.restore();
        }
    }

    public float getFontLeading(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.bottom - fm.top;
    }
    private float getTextSizeSp(float size){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                size, mDisplayMetrics);
    }


}
