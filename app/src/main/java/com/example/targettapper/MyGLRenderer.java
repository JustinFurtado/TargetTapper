package com.example.targettapper;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by jfurtado on 3/7/2017.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private ShaderProgram mProgram;
    private int vertBufID;
    private float widthP;
    private MyGLGame currentGame;
    private int positionAttribLoc;
    private int uvAttribLoc;
    private int texUniformLoc;
    private int posUniformLoc;
    private int scaleUniformLoc;
    private int aspectLoc;
    private int leftLoc;
    private int rightLoc;

    private final String vertexShaderCode =
                    "attribute vec3 vPosition;\n" +
                    "attribute vec2 UV;\n" +
                    "uniform vec3 offset;\n" +
                    "uniform float aspect;\n" +
                    "uniform float scale;\n" +
                    "varying vec2 fragUV;\n" +
                    "void main() {\n" +
                    "  vec3 xyz = vec3(vPosition.xy * scale, vPosition.z) + offset;" +
                    "  gl_Position = vec4(xyz.x * aspect, xyz.y, xyz.z, 1.0);\n" +
                    "  fragUV = UV;\n" +
                    "}\n";

    private final String fragmentShaderCode =
            "precision mediump float;\n" +
                            "uniform sampler2D textureSampler;\n" +
                            "uniform float leftCompare;\n" +
                            "uniform float rightCompare;\n" +
                            "varying vec2 fragUV;\n" +
                            "void main() {\n" +
                            "  if (gl_FragCoord.x < leftCompare || gl_FragCoord.x > rightCompare) {discard;}\n" +
                            "  gl_FragColor = texture2D(textureSampler, fragUV);\n" +
                            "}\n";


    public float  getWP() { return widthP; }

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mProgram = new ShaderProgram();
        mProgram.createProgram();
        mProgram.addVertShader(vertexShaderCode);
        mProgram.addFragShader(fragmentShaderCode);
        mProgram.linkShaderProgram();
        mProgram.useProgram();

        positionAttribLoc = GLES20.glGetAttribLocation(mProgram.getProgramID(), "vPosition");
        uvAttribLoc = GLES20.glGetAttribLocation(mProgram.getProgramID(), "UV");
        texUniformLoc = GLES20.glGetUniformLocation(mProgram.getProgramID(), "textureSampler");
        posUniformLoc = GLES20.glGetUniformLocation(mProgram.getProgramID(), "offset");
        scaleUniformLoc = GLES20.glGetUniformLocation(mProgram.getProgramID(), "scale");
        aspectLoc = GLES20.glGetUniformLocation(mProgram.getProgramID(), "aspect");
        leftLoc = GLES20.glGetUniformLocation(mProgram.getProgramID(), "leftCompare");
        rightLoc = GLES20.glGetUniformLocation(mProgram.getProgramID(), "rightCompare");

        int[] one = new int[1];
        GLES20.glGenBuffers(1, one, 0);
        vertBufID = one[0];

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertBufID);

        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, (new Square()).getVertexBuffer().capacity() * 4, (new Square()).getVertexBuffer(), GLES20.GL_STATIC_DRAW);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        TimeManager.initialize();

        // let the game do its initialization after we've done all of our initialization
        currentGame.initialize();
    }

    public void setGame(MyGLGame currentGame) {
        this.currentGame = currentGame;
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // clear screen, let the game do its thang
        float dt = TimeManager.getDeltaTimeOnlyCallOncePerFrame();
        currentGame.update(dt);
        currentGame.draw(this);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        mProgram.useProgram();
        widthP = height * 1.0f / width;
        GLES20.glUniform1f(aspectLoc, widthP);
        GLES20.glUniform1f(leftLoc, (width - height) / 2.0f);
        GLES20.glUniform1f(rightLoc, (width + height) / 2.0f);

    }

    public void drawObjectWithShader(Square quad){
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertBufID);
        mProgram.useProgram();

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionAttribLoc);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionAttribLoc, 3,
                GLES20.GL_FLOAT, false,
                quad.getStride(), 0);

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(uvAttribLoc);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(uvAttribLoc, 2,
                GLES20.GL_FLOAT, false,
                quad.getStride(), 3 * 4);

        // Set color for drawing the triangle
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, quad.getTexID());
        GLES20.glUniform1i(texUniformLoc, 0);

        // temporarily pass the positon of the quad in screen space
        GLES20.glUniform3fv(posUniformLoc, 1, quad.getXYZ(), 0);
        GLES20.glUniform1f(scaleUniformLoc, quad.getScale());


        //System.out.println("Drawin a thing:" + quad.getVertexCount());
        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, quad.getVertexCount());

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionAttribLoc);
        GLES20.glDisableVertexAttribArray(uvAttribLoc);

    }

    public void checkShapeTouch(float x, float y){
        currentGame.onTouch(x, y, widthP);
    }
}