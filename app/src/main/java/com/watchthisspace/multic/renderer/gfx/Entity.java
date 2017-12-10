package com.watchthisspace.multic.renderer.gfx;

import org.joml.Vector3f;

public class Entity {

    private Vector3f mPosition;

    private Mesh mMesh;

    public Entity(Vector3f position, Mesh mesh) {
        mPosition = position;
        mMesh = mesh;
    }

    public Mesh getMesh() {
        return mMesh;
    }

    public Vector3f getPosition() {
        return mPosition;
    }

    public void setPosition(Vector3f position) {
        mPosition = position;
    }
}
