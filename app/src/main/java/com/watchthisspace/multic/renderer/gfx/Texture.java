package com.watchthisspace.multic.renderer.gfx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class Texture {

    private int[] id;

    public Texture(Context context, int resId) {
        id = new int[1];

        GLES20.glGenTextures(1, id, 0);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;   // No prescaling

        // Get the bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);

        // Bind the texture
        bind();

        // Filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        // Free memory
        bitmap.recycle();

        // Unbind
        unbind();
    }

    public void bind() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, id[0]);
    }

    public void unbind() {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
    }

    public void dispose() {
        unbind();
        GLES20.glDeleteTextures(1, id, 0);
    }
}
