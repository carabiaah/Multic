package com.watchthisspace.multic.renderer.utils;

import com.watchthisspace.multic.config.Globals;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class Camera {

    private Matrix4f mProjectionMatrix;

    private Matrix4f mViewMatrix;

    private Matrix4f mIvp;

    private Vector3f mPosition;

    private Vector2f mViewport;

    private Vector2f mScreen;

    private float mZoom = 1;

    public Camera(Vector3f position) {
        mPosition = position;

        mViewMatrix = new Matrix4f();
        mProjectionMatrix = new Matrix4f();
        mIvp = new Matrix4f();

        mViewport = new Vector2f();
        mScreen = new Vector2f();
    }

    private void updateViewMatrix() {
        mViewMatrix.identity();
        mViewMatrix.translate(mPosition);

        mIvp.identity();
        mProjectionMatrix.mul(mViewMatrix, mIvp);
        mIvp.invert();
    }

    public void updateProjectionMatrix(float w, float h) {
        mScreen.x = w;
        mScreen.y = h;

        float aspectRatio = w / h;

        float height = Globals.V_HEIGHT;
        float width = Globals.V_HEIGHT * aspectRatio;

        mProjectionMatrix.identity();
        mProjectionMatrix.ortho(-width / 2 * mZoom, width / 2 * mZoom, -height / 2 * mZoom, height / 2 * mZoom, Globals.V_ZNEAR, Globals.V_ZFAR);

        setViewport(width, height);
    }

    public void update() {
        float wh = mViewport.x / 2f;
        float hh = mViewport.y / 2f;

        float bsh = Globals.GAME_BOARD_SIZE / 2f;

        // Keep camera within boundaries
        // X-axis
        if(mPosition.x - wh < -bsh) {        // Left
            mPosition.x = -bsh + wh;
        } else if(mPosition.x + wh > bsh) {  // Right
            mPosition.x = bsh - wh;
        }

        // Y-axis
        if(mPosition.y - hh < -bsh) {        // Bottom
            mPosition.y = -bsh + hh;
        } else if(mPosition.y + hh > bsh) {  // Top
            mPosition.y = bsh - hh;
        }

        updateViewMatrix();
    }

    public Matrix4f getProjectionMatrix() {
        return mProjectionMatrix;
    }

    public Matrix4f getViewMatrix() {
        return mViewMatrix;
    }

    public Vector3f getPosition() {
        return mPosition;
    }

    public void setPosition(Vector3f position) {
        mPosition = position;
    }

    public void setViewport(float w, float h) {
        mViewport.x = w;
        mViewport.y = h;
    }

    public Vector2f unproject(float sx, float sy) {
        float x = (sx / mScreen.x) * 2f - 1f;
        float y = 1f - (sy / mScreen.y) * 2f;

        Vector4f v = new Vector4f(x, y, 0, 1);

        v.mul(mIvp);

        return new Vector2f(v.x, v.y);
    }

    public void zoom(float scale) {
        mZoom /= scale;

        if(mZoom < Globals.C_MINZOOM) {
            mZoom = Globals.C_MINZOOM;
        } else if(mZoom > Globals.C_MAXZOOM) {
            mZoom = Globals.C_MAXZOOM;
        }

        updateProjectionMatrix(mScreen.x, mScreen.y);
    }
}
