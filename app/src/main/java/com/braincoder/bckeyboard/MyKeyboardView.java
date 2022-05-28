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
import android.view.KeyEvent;
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
        paint.setTextSize(28);
        paint.setColor(getResources().getColor(R.color.keyboard_key_text_color));

        List<Keyboard.Key> keys = getKeyboard().getKeys();
        for(Keyboard.Key key: keys) {
            if(key.label != null){

//            if(key.codes[0] == 103){
//                Drawable dr = (Drawable) getResources().getDrawable(R.color.black);
//                dr.setBounds(key.x, key.y, key.x + key.width, key.y + key.height);
//                dr.draw(canvas);
//
//                if (key.icon != null) {
//                    key.icon = getResources().getDrawable(R.drawable.ic_keyboard_capslock);
//                    key.icon.setBounds(key.x, key.y + 20, key.x + key.width, key.y + key.height);
//                    key.icon.draw(canvas);
//                }
//            }


                if(key.label.toString().equalsIgnoreCase("q"))
                    canvas.drawText("1", key.x + (key.width/2) + 25, key.y + 60, paint);

                if(key.label.toString().equalsIgnoreCase("w"))
                    canvas.drawText("2", key.x + (key.width/2) + 25, key.y + 60, paint);

                if(key.label.toString().equalsIgnoreCase("e"))
                    canvas.drawText("3", key.x + (key.width/2) + 25, key.y + 60, paint);

                if(key.label.toString().equalsIgnoreCase("r"))
                    canvas.drawText("4", key.x + (key.width/2) + 25, key.y + 60, paint);

                if(key.label.toString().equalsIgnoreCase("t"))
                    canvas.drawText("5", key.x + (key.width/2) + 25, key.y + 60, paint);

                if(key.label.toString().equalsIgnoreCase("y"))
                    canvas.drawText("6", key.x + (key.width/2) + 25, key.y + 60, paint);

                if(key.label.toString().equalsIgnoreCase("u"))
                    canvas.drawText("7", key.x + (key.width/2) + 25, key.y + 60, paint);

                if(key.label.toString().equalsIgnoreCase("i"))
                    canvas.drawText("8", key.x + (key.width/2) + 25, key.y + 60, paint);

                if(key.label.toString().equalsIgnoreCase("o"))
                    canvas.drawText("9", key.x + (key.width/2) + 25, key.y + 60, paint);

                if(key.label.toString().equalsIgnoreCase("p"))
                    canvas.drawText("0", key.x + (key.width/2) + 25, key.y + 60, paint);
                if(key.label.toString().equalsIgnoreCase("ا"))
                    canvas.drawText("آ", key.x + (key.width/2) + 20, key.y + 65, paint);
            }
        }
    }

    public void method(){

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
                mMiniKeyboard.setPreviewEnabled(false);

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

            if(popupKey.codes[0] == 46 || popupKey.codes[0] == 1748){
                mPopupKeyboard.showAtLocation(this, Gravity.NO_GRAVITY, x, y);
                invalidateAllKeys();
            }else {
                char c = popupKey.popupCharacters.charAt(0);
                getOnKeyboardActionListener().onKey((int) c, null);
            }

            return true;
        }

        return false;
    }

}
