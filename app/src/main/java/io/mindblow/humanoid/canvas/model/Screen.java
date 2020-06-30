// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.canvas.model;

import android.opengl.Matrix;

import io.mindblow.humanoid.context.MasterContext;


public class Screen {
    private final int zeroOffset = 0;

    private Skeleton skeleton;
    private MasterContext masterContext;

    public Screen(MasterContext masterContext) {
        this.skeleton = new Skeleton();
        this.masterContext = masterContext;
    }

    private float[] getOrbitCameraMatrix(float yaw, float pitch, float zoom) {
        float[] orbitCameraMatrix = new float[16];

        float[] cameraPositionInit = new float[]{1, 0, 0, 1};
        float[] originPosition = new float[]{0, 0, 0, 1};
        float[] cameratopPositionInit = new float[]{0, 0, 1, 1};

        float[] cameraPosition = new float[4];
        float[] cameraTopPosition = new float[4];

        float[] rotationMatrix = new float[16];
        float[] rotationMatrixYaw = new float[16];
        float[] rotationMatrixPitch = new float[16];
        Matrix.setRotateEulerM(rotationMatrixPitch, zeroOffset, 0, pitch, 0);
        Matrix.setRotateEulerM(rotationMatrixYaw, zeroOffset, 0, 0, yaw);
        Matrix.multiplyMM(rotationMatrix, zeroOffset, rotationMatrixYaw, zeroOffset, rotationMatrixPitch, zeroOffset);

        Matrix.multiplyMV(cameraPosition, zeroOffset, rotationMatrix, zeroOffset, cameraPositionInit, zeroOffset);
        Matrix.multiplyMV(cameraTopPosition, zeroOffset, rotationMatrix, zeroOffset, cameratopPositionInit, zeroOffset);

        Matrix.setLookAtM(orbitCameraMatrix, zeroOffset,
                cameraPosition[0] * zoom, cameraPosition[1] * zoom, cameraPosition[2] * zoom,
                originPosition[0], originPosition[1], originPosition[2],
                cameraTopPosition[0], cameraTopPosition[1], cameraTopPosition[2]);

        return orbitCameraMatrix;
    }



    public void redraw(MasterContext masterContext, float[] projectionMatrix) {
        float[] mMVPMatrix = new float[16];
        float[] orbitCameraMatrixMatrix = getOrbitCameraMatrix(masterContext.pitch, masterContext.yaw, 2);

        Matrix.multiplyMM(mMVPMatrix, 0, projectionMatrix, 0, orbitCameraMatrixMatrix, 0);

        Matrix.rotateM(mMVPMatrix, 0, 90, 1, 0, 0);
        Matrix.rotateM(mMVPMatrix, 0, -90, 0, 1, 0);
        Matrix.translateM(mMVPMatrix, 0, 0, -1, 0);

        skeleton.redraw(masterContext, mMVPMatrix);

        try {
            //humanContext.eventLock.lock();
        } catch (Exception e) {
            // handle the exception
        } finally {
            //humanContext.eventLock.unlock();
        }
    }
}
