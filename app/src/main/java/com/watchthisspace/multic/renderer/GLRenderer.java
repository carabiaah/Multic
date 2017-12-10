package com.watchthisspace.multic.renderer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.watchthisspace.multic.R;
import com.watchthisspace.multic.config.Globals;
import com.watchthisspace.multic.renderer.gfx.Entity;
import com.watchthisspace.multic.renderer.gfx.Mesh;
import com.watchthisspace.multic.renderer.gfx.Texture;
import com.watchthisspace.multic.renderer.shaders.DefaultShader;
import com.watchthisspace.multic.renderer.utils.Camera;
import com.watchthisspace.multic.renderer.utils.MeshBuilder;
import com.watchthisspace.multic.utils.StringUtils;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {

    private Context mContext;

    private DefaultShader mShader;

    private Camera mCamera;

    private List<Entity> mEntities;

    private HashMap<Byte, Mesh> mMeshes;

    public GLRenderer(Context context, Camera camera) {
        mContext = context;

        mCamera = camera;

        mEntities = Collections.synchronizedList(new ArrayList<Entity>());
        mMeshes = new HashMap<>();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {
        GLES20.glViewport(0, 0, i, i1);

        mCamera.updateProjectionMatrix((float) i, (float) i1);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        update();

        prepare();

        mShader.bind();

        // Setup uniforms
        mShader.setUniform("u_projMatrix", mCamera.getProjectionMatrix());
        mShader.setUniform("u_viewMatrix", mCamera.getViewMatrix());

        // Render entities
        synchronized (mEntities) {
            Iterator i = mEntities.iterator();
            while(i.hasNext()) {
                render((Entity) i.next());
            }
        }

        mShader.unbind();
    }

    private void render(Entity entity) {
        Mesh mesh = entity.getMesh();

        mShader.setUniform("u_hasTexture", mesh.hasTexture() ? 1 : 0);
        mShader.setUniform("u_color", mesh.getColor());

        Vector3f position = entity.getPosition();
        Matrix4f modelMatrix = new Matrix4f();
        modelMatrix.translate(position);

        mShader.setUniform("u_modelMatrix", modelMatrix);

        mesh.render();
    }

    private void update() {
        mCamera.update();
    }

    private void prepare() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void setupShader() {
        String vs = StringUtils.readFile(mContext, "default.vs");
        String fs = StringUtils.readFile(mContext, "default.fs");

        mShader = new DefaultShader(vs, fs);
        mShader.createUniform("u_hasTexture");
        mShader.createUniform("u_projMatrix");
        mShader.createUniform("u_viewMatrix");
        mShader.createUniform("u_modelMatrix");
        mShader.createUniform("u_color");
    }

    private void init() {
        setupShader();

        GLES20.glClearColor(0, 0, 0, 1);

        // Build the background
        Entity background = new Entity(new Vector3f(), MeshBuilder.buildQuad(Globals.GAME_BOARD_SIZE, Globals.GAME_BOARD_SIZE, 10));
        background.getMesh().setTexture(new Texture(mContext, R.drawable.background));

        mEntities.add(background);

        Entity grid = new Entity(new Vector3f(), MeshBuilder.buildGridMesh());
        mEntities.add(grid);

        // Pre-create the quad meshes for game objects
        createMeshes();
    }

    private void createMeshes() {
        Mesh xMesh = MeshBuilder.buildQuad(1, 1, 1);
        xMesh.setTexture(new Texture(mContext, R.drawable.x));
        mMeshes.put(Globals.MESH_X, xMesh);

        Mesh oMesh = MeshBuilder.buildQuad(1, 1, 1);
        oMesh.setTexture(new Texture(mContext, R.drawable.o));
        mMeshes.put(Globals.MESH_O, oMesh);

        Mesh tMesh = MeshBuilder.buildQuad(1, 1, 1);
        tMesh.setTexture(new Texture(mContext, R.drawable.t));
        mMeshes.put(Globals.MESH_T, tMesh);

        Mesh sMesh = MeshBuilder.buildQuad(1, 1, 1);
        sMesh.setTexture(new Texture(mContext, R.drawable.s));
        mMeshes.put(Globals.MESH_S, sMesh);
    }

    public void addTile(float x, float y, byte m) {
        // Center for world coordinates
        float wx = (int) x;
        float wy = (int) y;

        if(x > 0) {
            wx += 0.5f;
        } else {
            wx -= 0.5f;
        }

        if(y > 0) {
            wy += 0.5f;
        } else {
            wy -= 0.5f;
        }

        mEntities.add(new Entity(new Vector3f(wx, wy, 0), mMeshes.get(m)));
    }

    public void dispose() {
        // TODO
        // Figure this one out!
    }
}
