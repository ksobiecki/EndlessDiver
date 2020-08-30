package com.example.endlessdiver;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Enemy {

    public int speed = 0;
    int x=0, y=0, width, height;
    Bitmap enemy, enemySafe;

    Enemy(Resources res) {
        enemy = BitmapFactory.decodeResource(res, R.drawable.enemy);
        enemySafe = BitmapFactory.decodeResource(res, R.drawable.enemy_safe);

        width = enemy.getWidth()/5;
        height = enemy.getHeight()/5;

        enemy = Bitmap.createScaledBitmap(enemy, width, height, false);
        enemySafe = Bitmap.createScaledBitmap(enemySafe, width, height, false);
    }

    Bitmap getEnemy () {
        return enemy;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}
