package com.example.targettapper;

import android.opengl.GLES20;

/**
 * Created by jfurtado on 3/7/2017.
 */

public class ShaderProgram {
    private int mProgramID;
    private int mVertID;
    private int mFragID;

    public void createProgram(){
        mProgramID = GLES20.glCreateProgram();
    }

    public void addVertShader(String shaderCode){
        int vertShaderId = loadShader(GLES20.GL_VERTEX_SHADER, shaderCode);
        GLES20.glAttachShader(mProgramID, vertShaderId);
    }

    public void addFragShader(String shaderCode){
        int fragShaderID = loadShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
        GLES20.glAttachShader(mProgramID, fragShaderID);
    }

    public void linkShaderProgram(){
        GLES20.glLinkProgram(mProgramID);
    }

    public void useProgram(){
        GLES20.glUseProgram(mProgramID);
    }

    public int getProgramID(){
        return mProgramID;
    }

    private int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}
