package com.watchthisspace.multic.renderer.gfx;

import android.opengl.GLES20;
import android.opengl.GLES30;

import org.joml.Vector4f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Mesh {

    private static final int DEFAULT_DRAW_MODE = GLES20.GL_TRIANGLES;

    private static final int POSITION_DATA_SIZE = 3;
    private static final int TEXCOORD_DATA_SIZE = 2;

    private static final Vector4f DEFAULT_COLOR = new Vector4f(1, 1, 1, 1);

    // VAO
    private int mVaos[];

    // VBOS
    // Position & Color
    private int mVbos[];

    // Texture coordinate VBO
    private int mVboTC[];

    // Color
    private Vector4f mColor = DEFAULT_COLOR;

    // Texture
    private Texture mTexture;

    // Vertex count
    private final int mVertexCount;

    // Draw mode
    private int mDrawMode = DEFAULT_DRAW_MODE;

    public Mesh(float[] positions, int[] indices) {
        mVertexCount = indices.length;

        mVaos = new int[1];
        mVbos = new int[2];

        // Generate VAO
        GLES30.glGenVertexArrays(1, mVaos, 0);

        // Bind VAO
        GLES30.glBindVertexArray(mVaos[0]);

        // Generate VBOs
        GLES20.glGenBuffers(2, mVbos, 0);

        // Position data
        FloatBuffer positionBuffer = (FloatBuffer) ByteBuffer.allocateDirect(positions.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().position(0);
        positionBuffer.put(positions).position(0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVbos[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, positionBuffer.capacity() * 4, positionBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glVertexAttribPointer(0, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, 0, 0);

        // Indices
        IntBuffer indexBuffer = (IntBuffer) ByteBuffer.allocateDirect(indices.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer().position(0);
        indexBuffer.put(indices).position(0);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, mVbos[1]);
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexBuffer.capacity() * 4, indexBuffer, GLES20.GL_STATIC_DRAW);

        // Unbind
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        GLES30.glBindVertexArray(0);
    }

    public void setTextureCoordinates(float[] texCoords) {
        mVboTC = new int[1];

        // Bind VAO
        GLES30.glBindVertexArray(mVaos[0]);

        // Generate VBO
        GLES20.glGenBuffers(1, mVboTC, 0);

        // Upload data
        FloatBuffer texCoordBuffer = (FloatBuffer) ByteBuffer.allocateDirect(texCoords.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer().position(0);
        texCoordBuffer.put(texCoords).position(0);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVboTC[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, texCoordBuffer.capacity() * 4, texCoordBuffer, GLES20.GL_STATIC_DRAW);
        GLES20.glVertexAttribPointer(1, TEXCOORD_DATA_SIZE, GLES20.GL_FLOAT, false, 0, 0);

        // Unbind VBO
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        // Unbind VAO
        GLES30.glBindVertexArray(0);
    }

    public void setTexture(Texture texture) {
        mTexture = texture;
    }

    private void begin() {
        GLES30.glBindVertexArray(mVaos[0]);

        GLES20.glEnableVertexAttribArray(0);

        if(hasTexture()) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            mTexture.bind();

            GLES20.glEnableVertexAttribArray(1);
        }
    }

    private void end() {
        if(hasTexture()) {
            mTexture.unbind();

            GLES20.glDisableVertexAttribArray(1);
        }

        GLES20.glDisableVertexAttribArray(0);

        GLES30.glBindVertexArray(0);
    }

    public void render() {
        begin();

        GLES20.glDrawElements(mDrawMode, mVertexCount, GLES20.GL_UNSIGNED_INT, 0);

        end();
    }

    public void dispose() {
        // Bind VAO
        GLES30.glBindVertexArray(mVaos[0]);

        // Unbind VBO
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        // Delete VBOs
        GLES20.glDeleteBuffers(mVbos.length, mVbos, 0);
        GLES20.glDeleteBuffers(1, mVboTC, 0);

        // Unbind VAO
        GLES30.glBindVertexArray(0);

        // Delete VAO
        GLES30.glDeleteVertexArrays(mVaos.length, mVaos, 0);

        // Delete texture
        // FIXME
        // Maybe dispose texture somewhere else, for shared resource usage
        if(hasTexture()) {
            mTexture.dispose();
        }
    }

    public void setColor(Vector4f color) {
        mColor = color;
    }

    public Vector4f getColor() {
        return mColor;
    }

    public void setDrawMode(int drawMode) {
        mDrawMode = drawMode;
    }

    public boolean hasTexture() {
        return mTexture != null;
    }
}
