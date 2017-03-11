package com.example.targettapper;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameOverActivity extends AppCompatActivity implements MyGLGame {
    private TextView scoreText;
    private GLSurfaceView mGLView;
    private Square background;

    public void goToTitle(View view){
        Intent game = new Intent(this, MainActivity.class);
        startActivity(game);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new MyGLSurfaceView(this, this);
        setContentView(R.layout.activity_game_over);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.activity_game_over);

        layout.addView(mGLView);

        int score = getIntent().getIntExtra(GameActivity.EXTRA_INT, 0);
        scoreText = (TextView)findViewById(R.id.score);
        scoreText.setText("Your Score was: " + score);
        scoreText.bringToFront();
    }

    public void initialize(){
        int bgImageId = BitmapLoader.loadBitmap(getResources(), R.drawable.scoresbackground);

        background  = new Square();
        background.setScale(2.0f);
        background.setPos(0.0f, 0.0f, -0.1f);
        background.setTexID(bgImageId);
        background.setIgnoreClicks(true);
    }

    public void update(float dt){
        /* do nothing */
    }

    public void draw(MyGLRenderer renderer){
        renderer.drawObjectWithShader(background);
    }

    public void shutdown(){ /* do nothing */ }

    public void onTouch(float ndcX, float ndcY, float widthP){ /* do nothing */ }

}
