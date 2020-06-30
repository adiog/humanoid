// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.canvas.mesh;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import io.mindblow.humanoid.canvas.core.Renderer;


public class Mesh {
    private final int mProgram;

    private final FloatBuffer vertexCoordBuffer;
    private final FloatBuffer vertexColorBuffer;
    private final ShortBuffer verterOrderBuffer;

    private static final int COORDS_PER_VERTEX = 3;
    private static final int COLORS_PER_VERTEX = 4;
    private final int bytesPerCoord = 4;
    private final int bytesPerColor = 4;
    private final int vertexStride = COORDS_PER_VERTEX * bytesPerCoord;
    private final int colorStride = COLORS_PER_VERTEX * bytesPerColor;

    float vertexCoordLocal[];
    float vertexColorLocal[];
    short vertexOrderLocal[];

    public Mesh(float[] vertexCoord, short[] vertexOrder, float[] vertexColor) {
        vertexCoordLocal = vertexCoord;
        vertexOrderLocal = vertexOrder;
        vertexColorLocal = vertexColor;

        int bytesPerFloat = 4;
        int bytesPerShort = 2;

        ByteBuffer bb = ByteBuffer.allocateDirect(vertexCoordLocal.length * bytesPerFloat);
        bb.order(ByteOrder.nativeOrder());
        vertexCoordBuffer = bb.asFloatBuffer();
        vertexCoordBuffer.put(vertexCoordLocal);
        vertexCoordBuffer.position(0);

        ByteBuffer bbColors = ByteBuffer.allocateDirect(vertexColor.length * bytesPerFloat);
        bbColors.order(ByteOrder.nativeOrder());
        vertexColorBuffer = bbColors.asFloatBuffer();
        vertexColorBuffer.put(vertexColorLocal);
        vertexColorBuffer.position(0);

        ByteBuffer dlb = ByteBuffer.allocateDirect(vertexOrderLocal.length * bytesPerShort);
        dlb.order(ByteOrder.nativeOrder());
        verterOrderBuffer = dlb.asShortBuffer();
        verterOrderBuffer.put(vertexOrderLocal);
        verterOrderBuffer.position(0);

        mProgram = GLES20.glCreateProgram();
        compile();
    }

    private void compile() {
        String vertexShaderCode = "uniform mat4 uMVPMatrix;" +
                "attribute vec4 vPosition;" +
                "attribute vec4 vColor;" +
                "varying vec4 vColorVarying;" +
                "void main() {" +
                "  gl_Position = uMVPMatrix * vPosition;" +
                "  vColorVarying = vColor;" +
                "}";

        String fragmentShaderCode = "precision mediump float;" +
                "varying vec4 vColorVarying;" +
                "void main() {" +
                "  gl_FragColor = vColorVarying;" +
                "}";

        int vertexShader = Renderer.loadShader(
                GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);

        int fragmentShader = Renderer.loadShader(
                GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        Renderer.checkGlError("glCreateProgram");
        GLES20.glAttachShader(mProgram, vertexShader);
        Renderer.checkGlError("glAttachShader");
        GLES20.glAttachShader(mProgram, fragmentShader);
        Renderer.checkGlError("glAttachShader");
        GLES20.glLinkProgram(mProgram);
        Renderer.checkGlError("glLinkProgram");
    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgram);
        Renderer.checkGlError("glUseProgram");

        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(
                mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexCoordBuffer);

        int mColorHandle = GLES20.glGetAttribLocation(mProgram, "vColor");
        GLES20.glEnableVertexAttribArray(mColorHandle);
        GLES20.glVertexAttribPointer(
                mColorHandle, 4,
                GLES20.GL_FLOAT, false,
                colorStride, vertexColorBuffer);

        int mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, vertexOrderLocal.length,
                GLES20.GL_UNSIGNED_SHORT, verterOrderBuffer);

        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mColorHandle);
    }

}