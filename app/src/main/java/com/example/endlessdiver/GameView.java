package com.example.endlessdiver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private Random random;
    private Paint paint;
    private SharedPreferences prefs;
    private GameActivity activity;

    LightSensor lightSensor;
    Proximity proximitySensor;
    Magnometer magnometer;
    Accelerometer accelerometer;

    float lightSensorValue = 0;
    float proximitySensorValue = -1;
    float magnometerValue = 0;
    float accelerometerValue = 0;

    private boolean isPlaying = true, isGameOver = false;
    private int screenX, screenY;

    private Diver diver;
    private Background background0, background1, background2;
    private Enemy[] enemies;
    private Coin[] coins;

    private List<Bullet> bullets = new ArrayList<>();
    private List<Bullet> trash = new ArrayList<>();

    public int score = 0;
    private int enemyAmount = 3;
    private int coinAmount = 5;
    private int bgVelocity = 10, diverVelocity = 50, bulletVelocity = 100, maxSpeed = 80;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;

        random = new Random();
        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        lightSensor = new LightSensor(activity);
        proximitySensor = new Proximity(activity);
        magnometer = new Magnometer(activity);
        accelerometer = new Accelerometer(activity);

        this.screenX = screenX;
        this.screenY = screenY;

        background0 = new Background(screenX, screenY, getResources());
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        background0.y = 0;
        background1.y = -screenY;
        background2.y = -2*screenY;

        diver = new Diver(this, screenX, screenY, getResources());

        enemies = new Enemy[enemyAmount];
        initializeEnemies();
        coins = new Coin[coinAmount];
        initializeCoins();
    }

    @Override
    public void run() {
        while ( isPlaying ) {
            update();
            draw();
            sleep();
        }
    }

    private void update() {
        getSensorsValues();
        manageBackground();
        manageDiver();
        manageBullets();
        manageEnemies();
        manageCoins();
    }

    private void draw() {

        if (getHolder().getSurface().isValid()) {

            Canvas canvas = getHolder().lockCanvas();

            drawBackgrounds(canvas);
            drawGameObjects(canvas);

            if (isGameOver) {
                isPlaying = false;
                drawDiver(canvas, true);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting();
                return;
            }

            drawDiver(canvas, false);

            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void sleep () {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void drawBackgrounds(Canvas canvas) {

        if (lightSensorValue < 1000)
        {
            if (background0.y < screenY) {
                canvas.drawBitmap(background0.background_start, background0.x, background0.y, paint);
            }
            canvas.drawBitmap(background1.background_dark, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background_dark, background2.x, background2.y, paint);
        }
        else {
            if (background0.y < screenY) {
                canvas.drawBitmap(background0.background, background0.x, background0.y, paint);
            }
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);
        }
    }

    private void drawGameObjects(Canvas canvas) {
        for (Enemy enemy : enemies) {
            if (proximitySensorValue != 0) canvas.drawBitmap(enemy.getEnemy(), enemy.x, enemy.y, paint);
            else canvas.drawBitmap(enemy.enemySafe, enemy.x, enemy.y, paint);
        }

        for (Bullet bullet : bullets) {
            canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);
        }

        for (Coin coin : coins) {
            canvas.drawBitmap(coin.getCoin(), coin.x, coin.y, paint);
        }

        if (score < 10) canvas.drawText(score + "", screenX/2f, 256, paint);
        else if (score < 100) canvas.drawText(score + "", screenX/2f - 64, 256, paint);
        else if (score < 1000) canvas.drawText(score + "", screenX/2f - 128, 256, paint);
    }

    private void drawDiver(Canvas canvas, boolean isDead) {
        if (isDead) canvas.drawBitmap(diver.getDead(), diver.x, diver.y, paint);
        else canvas.drawBitmap(diver.getDiver(), diver.x, diver.y, paint);
    }

    public void newBullet() {
        Bullet bullet = new Bullet(getResources());
        bullet.y = diver.y;
        bullet.x = diver.x + (diver.width/2);
        bullets.add(bullet);
    }

    private void initializeCoins() {
        for (int i=0; i<coinAmount; i++) {
            Coin coin = new Coin(getResources());
            coin.x = random.nextInt(screenX - coin.width);
            coin.speed = randomSpeed();
            coins[i] = coin;
        }
    }

    private void initializeEnemies() {
        for (int i=0; i<enemyAmount; i++) {
            Enemy enemy = new Enemy(getResources());
            enemy.x = random.nextInt(screenX - enemy.width);
            enemy.speed = randomSpeed();
            enemies[i] = enemy;
        }
    }

    private void manageBackground() {
        background0.y += bgVelocity;
        background1.y += bgVelocity;
        background2.y += bgVelocity;

        if (background1.y > screenY) {
            background1.y = -screenY;
        }

        if (background2.y > screenY) {
            background2.y = -screenY;
        }
    }

    private void manageDiver() {

        if (accelerometerValue < -0.2) {
            diver.isGoingLeft = false;
            diver.isGoingRight = true;
        } else if (accelerometerValue > 0.2) {
            diver.isGoingLeft = true;
            diver.isGoingRight = false;
        } else {
            diver.isGoingLeft = false;
            diver.isGoingRight = false;
        }

        if (diver.isGoingLeft) diver.x -= diverVelocity;
        else if (diver.isGoingRight) diver.x += diverVelocity;

        if (diver.x < 0) {
            diver.x = 0;
        }

        if (diver.x > screenX - diver.width) {
            diver.x = screenX - diver.width;
        }

        if (magnometerValue > 60) diver.isShooting = true;
        else diver.isShooting = false;
    }

    private void manageBullets() {
        for (Bullet bullet : bullets) {

            if (bullet.y < 0) trash.add(bullet);

            bullet.y -= bulletVelocity;

            for (Enemy enemy : enemies) {
                if (proximitySensorValue != 0) {
                    if (Rect.intersects(enemy.getCollisionShape(), bullet.getCollisionShape())) {
                        enemy.y = screenY + 500;
                        bullet.y = -500;
                    }
                }
            }
        }

        for (Bullet bullet : trash) {
            bullets.remove(bullet);
        }
    }

    private void manageEnemies() {

        for (Enemy enemy : enemies) {
            enemy.y += enemy.speed;

            if(enemy.y > screenY) {
                enemy.speed = randomSpeed();
                enemy.y = 0;
                enemy.x = random.nextInt(screenX - enemy.width);
            }

            if (Rect.intersects(enemy.getCollisionShape(), diver.getCollisionShape()))
            {
                if (proximitySensorValue == 0)
                {
                    enemy.y = screenY + 500;
                    score ++;
                    return;
                }
                else {
                    isGameOver = true;
                    return;
                }
            }
        }
    }

    private void manageCoins() {
        for (Coin coin : coins) {
            coin.y += coin.speed;

            if(coin.y > screenY) {
                coin.speed = randomSpeed();
                coin.y = 0;
                coin.x = random.nextInt(screenX - coin.width);
            }

            if (Rect.intersects(coin.getCollisionShape(), diver.getCollisionShape()))
            {
                coin.y = screenY + 500;
                score ++;
                return;
            }
        }
    }

    private int randomSpeed() {
        int speed = random.nextInt(maxSpeed);
        if (speed < 20) return 20;
        else return speed;
    }

    private void saveIfHighScore() {
        if(prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
    }

    private void waitBeforeExiting() {
        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getSensorsValues() {
        lightSensorValue = lightSensor.getValue();
        proximitySensorValue = proximitySensor.getValue();
        magnometerValue = magnometer.getValue();
        accelerometerValue = accelerometer.getValue();
    }
}
