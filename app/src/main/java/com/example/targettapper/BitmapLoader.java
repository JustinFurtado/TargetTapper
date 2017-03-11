package com.example.targettapper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import static android.opengl.GLES11Ext.GL_BGRA;
import static android.opengl.GLES20.GL_RGB;
import static android.opengl.GLES20.GL_RGBA;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;
import static android.opengl.GLES30.GL_RGB8;
import static android.opengl.GLES30.GL_RGBA8;

/**
 * Created by jfurtado on 3/8/2017.
 */

public class BitmapLoader {
    static int loadBitmap(Resources res, int id)
    {
        Bitmap bitmap = BitmapFactory.decodeResource(res, id);

        // create the opengl texture
        int[] textureID = new int[1];
        GLES20.glGenTextures(1, textureID, 0);

        // check if the texture was created
        if (textureID[0] == 0)
        {
            System.out.println("Bitmaploader failed to gen textures!");
            return -1;
        }

        // bind the newly created texture
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID[0]);

        // set texture parameters
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);

        // send the texture data to opengl
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();

        // success
        return textureID[0];
    }
}
