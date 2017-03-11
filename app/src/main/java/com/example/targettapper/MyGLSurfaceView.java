package com.example.targettapper;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by jfurtado on 3/7/2017.
 */

public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context, MyGLGame game){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();

        setEGLConfigChooser(false);

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
        mRenderer.setGame(game);

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        // accounts for magic math with aspect hard coded into shader...
        float x = (((e.getX() - ((getWidth() - getHeight()) / 2.0f)) / getHeight()) * 2.0f) - 1.0f;
        float y = (e.getY() / getHeight()) * 2.0f - 1.0f;

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if ((x >= -1.0f) && (x <= 1.0f) && (y >= -1.0f) && (y <= 1.0f)) {
                    mRenderer.checkShapeTouch(x, y);
                    requestRender();
                }

        }

        return true;
    }
}
