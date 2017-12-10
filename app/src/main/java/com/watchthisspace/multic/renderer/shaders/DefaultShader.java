package com.watchthisspace.multic.renderer.shaders;

public class DefaultShader extends ShaderProgram {

    public DefaultShader(String vs, String fs) {
        super(vs, fs);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "a_position");
        super.bindAttribute(1, "a_texCoord");
    }
}
