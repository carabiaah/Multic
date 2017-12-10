package com.watchthisspace.multic.renderer.shaders;

import android.opengl.GLES20;
import android.util.Log;

import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

// Possibly remove the abstract qualifier
// Instantiate all shaders of the same class
public abstract class ShaderProgram {

    // Testing, debug
    private static final String SHADER_LOG_TAG = "TTTM_SHADER";

    private int mProgramId;
    private int mVertexShaderId;
    private int mFragmentShaderId;

    // Matrix upload buffer
    private FloatBuffer buffer = (FloatBuffer) ByteBuffer.allocateDirect(16 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();

    private final Map<String, Integer> mUniforms;

    public ShaderProgram(String vs, String fs) {
        mProgramId = GLES20.glCreateProgram();

        if(mProgramId == 0) {
            Log.w(SHADER_LOG_TAG, "Could not create shader program!");
        }

        mVertexShaderId = loadShader(vs, GLES20.GL_VERTEX_SHADER);
        mFragmentShaderId = loadShader(fs, GLES20.GL_FRAGMENT_SHADER);

        GLES20.glAttachShader(mProgramId, mVertexShaderId);
        GLES20.glAttachShader(mProgramId, mFragmentShaderId);

        bindAttributes();

        GLES20.glLinkProgram(mProgramId);
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(mProgramId, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if(linkStatus[0] == 0) {
            Log.w(SHADER_LOG_TAG, "Shader program linking failed!");
        }

        GLES20.glDetachShader(mProgramId, mVertexShaderId);
        GLES20.glDetachShader(mProgramId, mFragmentShaderId);

        GLES20.glValidateProgram(mProgramId);
        final int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(mProgramId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        if(validateStatus[0] == 0) {
            Log.w(SHADER_LOG_TAG, "Shader program validation failed!");
        }

        mUniforms = new HashMap<>();
    }

    public void bind() {
        GLES20.glUseProgram(mProgramId);
    }

    public void unbind() {
        GLES20.glUseProgram(0);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String name) {
        GLES20.glBindAttribLocation(mProgramId, attribute, name);
    }

    public void createUniform(String name) {
        int location = GLES20.glGetUniformLocation(mProgramId, name);

        mUniforms.put(name, location);
    }

    public void setUniform(String uniform, int value) {
        GLES20.glUniform1i(mUniforms.get(uniform), value);
    }

    public void setUniform(String uniform, Vector4f value) {
        GLES20.glUniform4f(mUniforms.get(uniform), value.x, value.y, value.z, value.w);
    }

    public void setUniform(String uniform, Matrix4f value) {
        buffer.position(0);
        value.get(buffer).position(0);

        GLES20.glUniformMatrix4fv(mUniforms.get(uniform), 1, false, buffer);
    }

    private static int loadShader(String file, int type) {
        int id = GLES20.glCreateShader(type);
        if(id == 0) {
            Log.w(SHADER_LOG_TAG, "Error: Could not create new shader!");
        }

        GLES20.glShaderSource(id, file);
        GLES20.glCompileShader(id);

        // Debug
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(id, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        Log.v(SHADER_LOG_TAG, "Compile status: " + compileStatus[0] + " : " + GLES20.glGetShaderInfoLog(id));

        return id;
    }

    public void dispose() {
        unbind();

        if(mProgramId != 0) {
            GLES20.glDeleteProgram(mProgramId);
        }
    }
}