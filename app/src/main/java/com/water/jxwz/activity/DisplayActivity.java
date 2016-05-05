package com.water.jxwz.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.water.jxwz.R;

/**
 * Created by KingJA on 2016/1/24.
 */
public class DisplayActivity extends Activity {

    private ImageView iv_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        iv_display = (ImageView) findViewById(R.id.iv_display);
        String path = getIntent().getStringExtra("PHOTO");
        Log.i("displayActivity", "path: " + path);
        Bitmap bitmap = BitmapFactory.decodeFile(path);
//        Matrix matrix = new Matrix();
//        matrix.setRotate(90);
//        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        iv_display.setImageBitmap(bitmap);
    }
}
