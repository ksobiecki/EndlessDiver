package com.example.endlessdiver;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {

    int x=0,y=0;
    Bitmap background, background_start, background_dark;

    Background (int screenX, int screenY, Resources res) {
        background = BitmapFactory.decodeResource(res, R.drawable.background);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);

        background_start = BitmapFactory.decodeResource(res, R.drawable.background_start);
        background_start = Bitmap.createScaledBitmap(background_start, screenX, screenY, false);

        background_dark = BitmapFactory.decodeResource(res, R.drawable.background_dark);
        background_dark = Bitmap.createScaledBitmap(background_dark, screenX, screenY, false);
    }
}
