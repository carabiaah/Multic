package com.watchthisspace.multic.objects;

// Test dummy class for offline rendering
public class Player {

    private byte mId;

    private String mName;

    public Player(byte id, String name) {
        mId = id;
        mName = name;
    }

    public byte getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
}
