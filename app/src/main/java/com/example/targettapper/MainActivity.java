package com.example.targettapper;

import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity implements MyGLGame {
    private GLSurfaceView mGLView;
    private Square background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new MyGLSurfaceView(this, this);
        setContentView(R.layout.activity_main);

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.activity_main);

        layout.addView(mGLView);

//        ((TextView)findViewById(R.id.helloWorld)).bringToFront();
    }

    public void initialize(){
        int bgImageId = BitmapLoader.loadBitmap(getResources(), R.drawable.titlebackground);

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


    public void goToGame(View view){
        Intent game = new Intent(this, GameActivity.class);
        startActivity(game);
    }
}
