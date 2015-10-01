package com.flavpopescu.taglayout;

import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Log.i("MainActivity", "#size screen width x height: " + width + " x " + height);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        Paint paint = new Paint();
        paint.setTypeface(typeface);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        Log.i("TagLayout", "text size is : " + paint.getTextSize());
        float textWidth = paint.measureText(getString(R.string.fox));
        Log.i("TagLayout", "#size measure text: " + textWidth);

        TextView textView = (TextView) findViewById(R.id.textView_sampleText);
        textView.setTypeface(typeface);

        View view = findViewById(R.id.ruler);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)textWidth, 5);
        params.addRule(RelativeLayout.BELOW, R.id.textViewExplanation);
        view.setLayoutParams(params);
    }
}
