package com.braincoder.bckeyboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.braincoder.bckeyboard.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

//        String characters = "اآبپتٹثجچح";
//        String characters = "خدڈذرڑزژسش";
//        String characters = "سضطظعغفقکگ";
//        String characters = "لمنوہھءیے";
        String characters = ",";
        for (char c : characters.toCharArray()){
            Log.i(c + " = ", String.valueOf((int) c));
        }
    }

    private void init(){
        binding.chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent settings = new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS);
//                startActivity(settings);

                InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
                imeManager.showInputMethodPicker();

//                char c = binding.source.getText().charAt(0);
//                Log.i("Characted = ", String.valueOf((int) c));
//                Toast.makeText(MainActivity.this, c + "", Toast.LENGTH_SHORT).show();
            }
        });

    }
}