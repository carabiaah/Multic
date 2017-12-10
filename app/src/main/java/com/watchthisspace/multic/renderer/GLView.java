package com.watchthisspace.multic.renderer;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.watchthisspace.multic.config.Globals;
import com.watchthisspace.multic.renderer.utils.Camera;
import com.watchthisspace.multic.utils.GameManager;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class GLView extends GLSurfaceView implements View.OnTouchListener {

    private Context mContext;

    private GLRenderer mRenderer;

    private Camera mCamera;

    private Vector2f mTouch;

    private GameManager mGameManager;

    private GestureDetector mGestureDetector;

    private ScaleGestureDetector mScaleDetector;

    private boolean mZooming = false;

    public GLView(Context context) {
        super(context);

        mContext = context;
    }

    public GLView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;
    }

    public void init(GameManager gameManager) {
        mGameManager = gameManager;

        setEGLContextClientVersion(2);
        setPreserveEGLContextOnPause(true);

        mCamera = new Camera(new Vector3f());

        mRenderer = new GLRenderer(mContext, mCamera);
        setRenderer(mRenderer);

        mTouch = new Vector2f();

        mGestureDetector = new GestureDetector(mContext, new GListener());
        mScaleDetector = new ScaleGestureDetector(mContext, new SListener());

        setOnTouchListener(this);
    }

    public void select(Vector2f pos) {
        // Get tile
        // Board coordinates
        int x = (int) (pos.x + Globals.GAME_BOARD_SIZE / 2);
        int y = (int) (-pos.y + Globals.GAME_BOARD_SIZE / 2);

        byte player = mGameManager.getTurn();

        // Tile isn't free?
        // I.e. illegal move
        if(!mGameManager.set(x, y, player)) {
            return;
        }

        mGameManager.checkMove(x, y, player);

        mRenderer.addTile(pos.x, pos.y, player);
    }

    // Input handlers below
    // Touch
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);
        mScaleDetector.onTouchEvent(motionEvent);

        if(mZooming) {
            return false;
        }

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouch = mCamera.unproject(motionEvent.getX(), motionEvent.getY());

                return true;
            case MotionEvent.ACTION_MOVE:
                Vector2f v = mCamera.unproject(motionEvent.getX(), motionEvent.getY());
                v.sub(mTouch, v);

                mCamera.getPosition().add(v.x, v.y, 0);

                return true;
        }

        return true;
    }

    // Gestures
    class GListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            mZooming = false;
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Vector2f pos = mCamera.unproject(e.getX(), e.getY());

            select(pos);

            return true;
        }
    }

    // Scale gesture
    class SListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mCamera.zoom(detector.getScaleFactor());

            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mZooming = true;

            return true;
        }
    }
}