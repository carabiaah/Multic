package com.watchthisspace.multic.objects;

import com.watchthisspace.multic.config.Globals;

import org.joml.Vector2i;

public class Board {

    private int mSize;

    private byte[][] mTiles;

    private Vector2i mLastMove;

    public Board(int size) {
        mSize = size;

        initTiles();

        mLastMove = new Vector2i(0, 0);
    }

    private void initTiles() {
        mTiles = new byte[mSize][mSize];

        // Initialize the board tiles to -1 (empty)
        for(int i = 0; i < mSize; i++) {
            for(int j = 0; j < mSize; j++) {
                mTiles[i][j] = Globals.BOARD_EMPTY;
            }
        }
    }

    public void set(int x, int y, byte value) {
        // Out of bounds check
        if(x < 0 || x > mSize - 1 || y < 0 || y > mSize - 1) {
            return;
        }

        mTiles[y][x] = value;
    }

    public byte get(int x, int y) {
        // Out of bounds check
        if(x < 0 || x > mSize - 1 || y < 0 || y > mSize - 1) {
            return (byte) -1;
        }

        return mTiles[y][x];
    }

    public Vector2i getLastMove() {
        return mLastMove;
    }

    public void setLastMove(int x, int y) {
        mLastMove.set(x, y);
    }

    public byte[][] getTiles() {
        return mTiles;
    }
}
