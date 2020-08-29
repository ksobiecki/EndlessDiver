package com.example.endlessdiver;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Diver {

    boolean isGoingLeft = false, isGoingRight = false, isShooting = false;
    int x, y, width, height, movementCounter = 0;
    Bitmap diver1, diver2, diverDead;
    private GameView gameView;

    Diver (GameView gameView, int screenX, int screenY, Resources res) {

        this.gameView = gameView;

        diver1 = BitmapFactory.decodeResource(res, R.drawable.diver);
        diver2 = BitmapFactory.decodeResource(res, R.drawable.diver);
        diverDead = BitmapFactory.decodeResource(res, R.drawable.diver_dead);

        width = diver1.getWidth()/4;
        height = diver1.getHeight()/4;

        x = (screenX-width) / 2;
        y = screenY - 400;

        diver1 = Bitmap.createScaledBitmap(diver1, width, height, false);
        diver2 = Bitmap.createScaledBitmap(diver2, width, height, false);
        diverDead = Bitmap.createScaledBitmap(diverDead, width, height, false);
    }

    // animation switcher
    Bitmap getDiver() {

        if (isShooting) {
            gameView.newBullet();
        }

        if (movementCounter == 0) {
            movementCounter++;
            return diver1;
        }
        movementCounter --;
        return diver2;
    }

    Bitmap getDead() {
        return diverDead;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}
