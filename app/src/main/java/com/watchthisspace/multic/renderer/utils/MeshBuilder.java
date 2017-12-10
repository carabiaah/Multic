package com.watchthisspace.multic.renderer.utils;

import android.opengl.GLES20;

import com.watchthisspace.multic.config.Globals;
import com.watchthisspace.multic.renderer.gfx.Mesh;

import org.joml.Vector4f;

public class MeshBuilder {

    public static Mesh buildGridMesh() {
        int lines = Globals.GAME_BOARD_SIZE + 1;

        int size = lines * 4;

        float[] vertices = new float[size * 3];
        int[] indices = new int[size];

        // Vertical lines
        int vi = 0;
        for(int i = 0; i < Globals.GAME_BOARD_SIZE + 1; i++) {
            float x = -Globals.GAME_BOARD_SIZE / 2 + i;

            vertices[vi++] = x;
            vertices[vi++] = Globals.GAME_BOARD_SIZE / 2;
            vertices[vi++] = 0;

            vertices[vi++] = x;
            vertices[vi++] = -Globals.GAME_BOARD_SIZE / 2;
            vertices[vi++] = 0;
        }

        // Horizontal lines
        for(int i = 0; i < Globals.GAME_BOARD_SIZE + 1; i++) {
            float y = -Globals.GAME_BOARD_SIZE / 2 + i;

            vertices[vi++] = Globals.GAME_BOARD_SIZE / 2;
            vertices[vi++] = y;
            vertices[vi++] = 0;

            vertices[vi++] = -Globals.GAME_BOARD_SIZE / 2;
            vertices[vi++] = y;
            vertices[vi++] = 0;
        }

        // Indices
        for(int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }

        Mesh mesh = new Mesh(vertices, indices);
        mesh.setColor(new Vector4f(0, 0, 0, 1));
        mesh.setDrawMode(GLES20.GL_LINES);

        return mesh;
    }

    public static Mesh buildQuad(float w, float h, float tr) {
        float hw = w / 2f;
        float hh = h / 2f;

        float[] vertices = {
                -hw, hh, 0,
                -hw, -hh, 0,
                hw, -hh, 0,
                hw, hh, 0
        };

        int[] indices = {
                0, 1, 3,
                3, 1, 2
        };

        float[] texCoords = {
                0, 0,
                0, tr,
                tr, tr,
                tr, 0
        };

        Mesh mesh = new Mesh(vertices, indices);
        mesh.setTextureCoordinates(texCoords);

        return mesh;
    }
}
