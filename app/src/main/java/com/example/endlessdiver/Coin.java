package com.example.endlessdiver;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Coin {

    public int speed = 0;
    int x=0, y=0, width, height;
    Bitmap coin;

    Coin (Resources res) {

        coin = BitmapFactory.decodeResource(res, R.drawable.coin);

        width = coin.getWidth()/5;
        height = coin.getHeight()/5;

        coin = Bitmap.createScaledBitmap(coin, width, height, false);

    }

    Bitmap getCoin () {
        return coin;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}
