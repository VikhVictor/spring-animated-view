package com.test.animtry;

import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements AnimatedEditText.OnStateChangedListener {

    private AnimatedEditText mInput;
    private Button mSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInput = (AnimatedEditText) findViewById(R.id.btn_name);
        mInput.setValueText("Label");
        mInput.setInputText("Text text");

        mInput.setOnStateChangedListener(this);

        mSave = (Button) findViewById(R.id.btn_save);
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInput.performDoneClick();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

    @Override
    public void onStateChanged(boolean isOpened) {
        Toast.makeText(this, isOpened ? "isOpened" : "isClosed",
                Toast.LENGTH_SHORT).show();
    }
}
