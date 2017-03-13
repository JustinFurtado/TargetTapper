package com.example.targettapper;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements MyGLGame {
    public static String EXTRA_INT = "com.example.targettapper.score";
    private GLSurfaceView mGLView;
    private ArrayList<Square> mSquares;
    private TextView scoreText;
    private TextView lifeText;
    private int life = 3;
    private int score = 0;
    private int bgImageId;
    private int targetImageId;
    private int hazardImageId;
    private Random rand;
    private float wp;
    private boolean isGameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new MyGLSurfaceView(this, this);
        setContentView(R.layout.activity_game);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.activity_game);

        layout.addView(mGLView);

        scoreText = (TextView)findViewById(R.id.score);
        lifeText = (TextView)findViewById(R.id.life);

        scoreText.bringToFront();
        lifeText.bringToFront();

        setScoreText();
        setLifeText();

        rand = new Random();
    }

    private void setLifeText(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lifeText.setText("Life: " + life);
            }
        });
    }

    private void setScoreText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scoreText.setText("Score: " + score);
            }
        });
    }

    public void initialize(){
        targetImageId = BitmapLoader.loadBitmap(getResources(), R.drawable.target);
        hazardImageId = BitmapLoader.loadBitmap(getResources(), R.drawable.hazard);
        bgImageId = BitmapLoader.loadBitmap(getResources(), R.drawable.background);

        mSquares = new ArrayList<Square>();

        float t = 0.3f;
        float g = -0.1f;
        Square s = new Square();
        s.setScale(2.0f);
        s.setPos(0.0f, 0.0f, -0.1f);
        s.setTexID(bgImageId);
        s.setIgnoreClicks(true);
        mSquares.add(s);

        for (int i = 0; i < 5; ++i) {
            Square q = new Square();
            mSquares.add(q);
            generateNewQuad(q, 0.5f);
        }
    }

    public void update(float dt){
        if (isGameOver) {return;}
        for (Square quad : mSquares) {
            if (!quad.isEnabled()) { continue; }
            quad.updatePositionBasedOnVelocityAndGravity(dt);

            if (!quad.doesIgnoreClicks())
            {
                if (quad.getUp() < -1.0f && quad.getVelocity()[1] < 0.0f){
                    if (quad.getTexID() == targetImageId) {life--; setLifeText();}
                    generateNewQuad(quad);
                }
            }
        }

        if (life < 0){
            isGameOver = true;
            goToGameOver();
        }
    }

    private void goToGameOver(){
        Intent intent = new Intent(this, GameOverActivity.class);
        intent.putExtra(EXTRA_INT, score);
        startActivity(intent);
    }

    private void generateNewQuad(Square quad) {
        generateNewQuad(quad, wp);
    }

    private void generateNewQuad(Square quad, float w){
        // random x on + or - far half of screen
        float x = ((rand.nextFloat() * 2.0f * w - 1.0f) * 0.25f + 0.75f) * (rand.nextBoolean() ? 1.0f : -1.0f);

        // start barely off/onscreen
        float y = -0.95f;

        // scale between 0.1f-0.5f
        float s = rand.nextFloat() * 0.3f + 0.2f;

        float vy = rand.nextFloat() * 0.4f + 0.4f + (0.02f * (score/10));

        // x velocity towards center of screen
        float vx = (x > 0.0f ? -1.0f : 1.0f) * (rand.nextFloat() * 0.5f + 0.25f) * (0.5f * vy);

        float g = -0.15f - (0.01f * (score/10));

        boolean hazard = rand.nextFloat() < 0.2f;
        quad.setPos(x, y, rand.nextFloat() * 0.5f - 0.5f);
        quad.setVelocity(vx, vy);
        quad.setScale(s);
        quad.setGravity(g);
        quad.setTexID(hazard ? hazardImageId : targetImageId);


    }

    public void draw(MyGLRenderer renderer){
        wp = renderer.getWP();

        for (Square quad : mSquares) {
            if (!quad.isEnabled()) { continue; }
            renderer.drawObjectWithShader(quad);
        }
    }

    public void shutdown(){

    }

    public void onTouch(float ndcX, float ndcY, float widthP){
        if (isGameOver || mSquares == null) {return;}
        wp = widthP;
        int hitIndex = -1;
        for (int i = 0; i < mSquares.size(); ++i){
            Square quad = mSquares.get(i);
            if (!quad.isEnabled()) { continue; }

            float thisZ = quad.getXYZ()[2];
            if (quad.containsPoint(ndcX, ndcY, widthP)) {
                hitIndex = i;
            }
        }

        if (hitIndex > -1 && hitIndex < mSquares.size()){
            handleTap(mSquares.get(hitIndex));
        }

    }

    private void handleTap(Square quad){
        if (quad.getTexID() == hazardImageId) {life = -1; setLifeText();} // lose one life per hazard tap
        else if (quad.getTexID() == targetImageId) {score += (int)(5.0f / quad.getScale()); setScoreText(); } // get more score for smaller targets
        generateNewQuad(quad);
    }
}
