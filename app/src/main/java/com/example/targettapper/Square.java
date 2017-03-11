package com.example.targettapper;

import android.content.res.Resources;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by jfurtado on 3/7/2017.
 */

public class Square {

    private int texID;
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private float xPos;
    private float yPos;
    private float zPos;
    private float xVelocity;
    private float yVelocity;
    private float scale;
    private float gravity;
    private boolean enabled = true;
    private boolean ignoreClicks = false;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 5;
    static float squareCoords[] = {
            -0.5f,  0.5f, 0.0f, 0.0f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f, 0.0f, 1.0f,  // bottom left
            0.5f, -0.5f,  0.0f, 1.0f, 1.0f, // bottom right
            0.5f,  0.5f,  0.0f, 1.0f, 0.0f, // top right
            -0.5f,  0.5f, 0.0f, 0.0f, 0.0f,  // top left
            0.5f, -0.5f,  0.0f, 1.0f, 1.0f // bottom right
    };

    public boolean containsPoint(float x, float y, float widthP){
        if (ignoreClicks) { return false; }

        float left = getLeft(widthP);
        float right = getRight(widthP);
        float up = getUp();
        float down = getDown();

        //System.out.println("L: " + left + " R: " + right + " U: " + up + " D: " + down + " X: " + x + " Y: " + -y);
        return ((-y > down) && (-y < up) && (x < right) && (x > left));
    }

    public float getLeft(float widthP){
        return -0.5f * scale * widthP + xPos;
    }

    public float getRight(float widthP){
        return 0.5f * scale * widthP + xPos;
    }

    public float getDown(){
        return -0.5f * scale + yPos;
    }

    public float getUp(){
        return 0.5f * scale + yPos;
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    public void setIgnoreClicks(boolean ignoreClicks){
        this.ignoreClicks = ignoreClicks;
    }

    public boolean doesIgnoreClicks() { return ignoreClicks; }
    public boolean isEnabled() { return enabled; }

    public FloatBuffer getVertexBuffer(){
        return vertexBuffer;
    }

    public int getVertexCount() {
        return squareCoords.length / COORDS_PER_VERTEX;
    }

    public int getCoordsPerVertex(){
        return COORDS_PER_VERTEX;
    }

    public int getStride(){
        return COORDS_PER_VERTEX * 4;
    }

    public void setTexID(int texID)
    {
        this.texID = texID;
    }

    public int getTexID() {return texID;}

    public float[] getXYZ() { return new float[]{xPos, yPos, zPos}; }

    public float getScale() { return scale; }

    public void setScale(float newScale) { scale = newScale; }

    public void setPos(float xPos, float yPos, float zPos) { this.xPos = xPos; this.yPos = yPos; this.zPos = zPos;}

    public void setVelocity(float newXVelocity, float newYVelocity) {
        xVelocity = newXVelocity;
        yVelocity = newYVelocity;
    }

    public float[] getVelocity() { return new float[]{xVelocity, yVelocity}; }

    public void updatePositionBasedOnVelocityAndGravity(float deltaTime){
        xPos = xPos + (xVelocity * deltaTime);

        yVelocity += gravity * deltaTime;
        yPos = yPos + (yVelocity * deltaTime);
    }

    public void setGravity(float gravity){
        this.gravity = gravity;
    }

    public Square() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

    }
}