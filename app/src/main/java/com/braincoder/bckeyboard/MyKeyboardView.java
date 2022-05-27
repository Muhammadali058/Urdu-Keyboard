package com.braincoder.bckeyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import java.util.List;

public class MyKeyboardView extends KeyboardView {
    public MyKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(25);
        paint.setColor(Color.RED);

        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for(Keyboard.Key key: keys) {
            if(key.label != null)
                canvas.drawText(key.label.toString(), key.x + (key.width/2) + 20, key.y + 50, paint);

            if(key.codes[0] == -1){
                Drawable dr = (Drawable) getResources().getDrawable(R.drawable.pressed);
                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
                dr.draw(canvas);

                if (key.icon != null) {
                    key.icon.setBounds(key.x, key.y, key.x + key.width - 50, key.y + key.height - 50);
                    key.icon.draw(canvas);
                }
            }
        }
    }

//    private Keyboard.Key longPressedKey = null;
//    @Override
//    protected boolean onLongPress(Keyboard.Key key) {
////        char c = 'w';
////        if(key.codes[0] == (int)c ){
////            getOnKeyboardActionListener().onKey((int)'2', null);
////        }
////        return super.onLongPress(key);
//
//        if (key.popupCharacters != null && key.popupCharacters.length() > 0)
//            longPressedKey = key;
//        return super.onLongPress(key);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    MyKeyboardView mMiniKeyboard;
    private View mMiniKeyboardContainer;
    private int[] mWindowOffset;
    PopupWindow mPopupKeyboard;
    @Override
    protected boolean onLongPress(Keyboard.Key popupKey) {
        int popupKeyboardId = popupKey.popupResId;

        if (popupKeyboardId != 0) {
            mPopupKeyboard = new PopupWindow();
            mPopupKeyboard.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
            mPopupKeyboard.setOutsideTouchable(true);
            mPopupKeyboard.setFocusable(false);

            mPopupKeyboard.setTouchInterceptor(new OnTouchListener(){
                public boolean onTouch(View v, MotionEvent event){
                    if (event.getAction() == MotionEvent.ACTION_OUTSIDE){
                        mPopupKeyboard.dismiss();
                        return true;
                    }

                    return false;
                }
            });

            if (mMiniKeyboardContainer == null) {
                mMiniKeyboardContainer = LayoutInflater.from(getContext()).inflate(R.layout.popup_layout, null);
                mMiniKeyboard = (MyKeyboardView) mMiniKeyboardContainer.findViewById(android.R.id.keyboardView);

                if (mWindowOffset == null) {
                    mWindowOffset = new int[2];
                    getLocationInWindow(mWindowOffset);
                }

                mMiniKeyboard.setOnKeyboardActionListener(new OnKeyboardActionListener() {
                    public void onKey(int primaryCode, int[] keyCodes) {
                        mPopupKeyboard.dismiss();
                        mPopupKeyboard = null;
                        getOnKeyboardActionListener().onKey(primaryCode,keyCodes);
                    }

                    public void onText(CharSequence text) {
                        mPopupKeyboard.dismiss();
                        mPopupKeyboard = null;
                    }

                    public void swipeLeft() {
                    }

                    public void swipeRight() {
                    }

                    public void swipeUp() {
                    }

                    public void swipeDown() {
                    }

                    public void onPress(int primaryCode) {

                    }

                    public void onRelease(int primaryCode) {

                    }
                });

            }
            else
            {

                mMiniKeyboard = (MyKeyboardView) mMiniKeyboardContainer.findViewById(android.R.id.keyboardView);
            }

            Keyboard keyboard;
            if (popupKey.popupCharacters != null) {
                keyboard = new Keyboard(getContext(), R.xml.keyboard_popup_template,
                        popupKey.popupCharacters, -1, getPaddingLeft() + getPaddingRight());
            } else {
                keyboard = new Keyboard(getContext(), R.xml.keyboard_popup_template);
            }

            mMiniKeyboard.setKeyboard(keyboard);
            mMiniKeyboard.setPopupParent(this);
            mMiniKeyboardContainer.measure(
                    MeasureSpec.makeMeasureSpec(getWidth(), MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(getHeight(), MeasureSpec.AT_MOST));

            int popupX = popupKey.x + mWindowOffset[0];
            popupX -= mMiniKeyboard.getPaddingLeft();
            int popupY = popupKey.y + mWindowOffset[1];
            popupY += getPaddingTop();
            popupY -= mMiniKeyboard.getMeasuredHeight();
            popupY -= mMiniKeyboard.getPaddingBottom();

            popupX = popupX + popupKey.width - mMiniKeyboardContainer.getMeasuredWidth();
            popupY = popupY - mMiniKeyboardContainer.getMeasuredHeight();
            final int x = popupX + mMiniKeyboardContainer.getPaddingRight() + mWindowOffset[0];
            final int y = popupY + mMiniKeyboardContainer.getPaddingBottom() + mWindowOffset[1]  + 70;
            mMiniKeyboard.setPopupOffset(x < 0 ? 0 : x, y);
            mMiniKeyboard.setShifted(isShifted());

            mPopupKeyboard.setContentView(mMiniKeyboardContainer);

            mPopupKeyboard.setWidth(mMiniKeyboardContainer.getMeasuredWidth());
            mPopupKeyboard.setHeight(mMiniKeyboardContainer.getMeasuredHeight());
            mPopupKeyboard.showAtLocation(this, Gravity.NO_GRAVITY, x, y);

            invalidateAllKeys();

//            char c = popupKey.popupCharacters.charAt(1);
//            getOnKeyboardActionListener().onKey((int) c,null);

            return true;
        }

        return false;
    }

}
