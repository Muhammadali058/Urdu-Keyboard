package com.braincoder.bckeyboard;

import static android.view.inputmethod.InputConnection.CURSOR_UPDATE_MONITOR;

import android.annotation.SuppressLint;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.CursorAnchorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyKeyboard extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private MyKeyboardView kv;
    private Keyboard keyboard;

    private boolean isShift = false;
    private boolean isCaps = false;
    public final static int KEYCODE_SHIFT = -1;
    public final static int KEYCODE_CAPS = -2;
    public final static int KEYCODE_DELETE = -3;
    public final static int KEYCODE_ALPHABET = -100;
    public final static int KEYCODE_NUMERIC = -101;
    public final static int KEYCODE_SYMBOLS = -102;
    private TextView editText;
    int selection = 0;

    @Override
    public View onCreateInputView() {
        kv = (MyKeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setPreviewEnabled(false);
        kv.setOnKeyboardActionListener(this);

        return kv;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateCandidatesView() {
        View view = getLayoutInflater().inflate(R.layout.demo_layout, null);
        setCandidatesViewShown(true);
        setCandidatesView(view);

        editText = view.findViewById(R.id.textView);
        ImageView okBtn = view.findViewById(R.id.okBtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });

        return view;
    }

    @Override
    public void onComputeInsets(Insets outInsets) {
        super.onComputeInsets(outInsets);
//        if (!isFullscreenMode()) {
//            outInsets.contentTopInsets = outInsets.visibleTopInsets;
//        }
    }

    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    private void switchKeyboard(InputConnection ic, int keyCode){
        switch (keyCode){
            case KEYCODE_NUMERIC:
                keyboard = new Keyboard(this, R.xml.n_qwerty);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
            case KEYCODE_ALPHABET:
                keyboard = new Keyboard(this, R.xml.qwerty);
                kv.setKeyboard(keyboard);
                kv.setOnKeyboardActionListener(this);
                break;
        }
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
//        playClick(primaryCode);

        switch(primaryCode){
            case -5:
                return;

            case KEYCODE_ALPHABET:
            case KEYCODE_NUMERIC:
            case KEYCODE_SYMBOLS:
                switchKeyboard(ic, primaryCode);
                return;

            case KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);

//                try {
//                    String text = editText.getText().toString();
//                    if(text.length() > 0 && selection > 0) {
//                        String newText = text.substring(0, selection - 1) + text.substring(selection);
//                        editText.setText(newText);
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
                break;
            case KEYCODE_SHIFT:
                if(isCaps){
                    isShift = false;
                    isCaps = false;
                    keyboard.setShifted(false);
                    kv.invalidateAllKeys();
                }else {
                    isShift = !isShift;
                    keyboard.setShifted(isShift);
                    kv.invalidateAllKeys();
                }

                kv.method();
                return;
            case KEYCODE_CAPS:
                isCaps = !isCaps;
                isShift = false;
                keyboard.setShifted(isCaps);
                kv.invalidateAllKeys();
                return;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char)primaryCode;
                if(Character.isLetter(code) && (isCaps || isShift)){
                    code = Character.toUpperCase(code);
                }

                ic.commitText(String.valueOf(code),1);

                ic.requestCursorUpdates(CURSOR_UPDATE_MONITOR);

                editText.setText(editText.getText().toString() + String.valueOf(code));
        }

        if(isShift){
            isShift = false;
            keyboard.setShifted(isShift);
            kv.invalidateAllKeys();
        }
    }

    @Override
    public void onUpdateCursorAnchorInfo(CursorAnchorInfo cursorAnchorInfo) {
        selection = cursorAnchorInfo.getSelectionStart();
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        Log.i("OnLongPress = ", keyCode +"");
        char c = 'w';
        if(keyCode == (int)c ){
            onKey((int)'1', null);
        }
        return true;

    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }
}