package com.watchthisspace.multic.config;

public class Globals {

    // Viewport configuration
    public static final float V_HEIGHT = 15;    // Viewport scales on aspect ratio, fixed-height
    public static final float V_ZNEAR = -1;
    public static final float V_ZFAR = 1;

    // Camera
    public static final float C_MAXZOOM = 2.75f;
    public static final float C_MINZOOM = 0.75f;

    // Mesh identifiers
    public static final byte MESH_X = 0;    // X
    public static final byte MESH_O = 1;    // o
    public static final byte MESH_T = 2;    // Triangle
    public static final byte MESH_S = 3;    // Square

    // Game configuration
    public static final int GAME_BOARD_SIZE = 32;
    public static final int GAME_WIN_N = 5; // Amount of consecutive items needed to win

    // Board config
    public static final byte BOARD_EMPTY = -1;
}
