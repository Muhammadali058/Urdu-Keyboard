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

    private KeyboardView kv;
    private Keyboard keyboard;

    private boolean caps = false;
    private TextView editText;
    int selection = 0;

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setPreviewEnabled(false);
        kv.setOnKeyboardActionListener(this);

        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/urdu_font.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/urdu_font.ttf");
        FontsOverride.setDefaultFont(this, "DEFAULT_BOLD", "fonts/urdu_font.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/urdu_font.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/urdu_font.ttf");


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

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);

                try {
                    String text = editText.getText().toString();
                    if(text.length() > 0 && selection > 0) {
                        String newText = text.substring(0, selection - 1) + text.substring(selection);
                        editText.setText(newText);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboard.setShifted(caps);
                kv.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                break;
            default:
                char code = (char)primaryCode;
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }

                ic.commitText(String.valueOf(code),1);

                ic.requestCursorUpdates(CURSOR_UPDATE_MONITOR);

                editText.setText(editText.getText().toString() + String.valueOf(code));
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