// This file is a part of Humanoid project.
// Copyright (C) 2020 Aleksander Gajewski <adiog@mindblow.io>.

package io.mindblow.humanoid.canvas.model;

import android.opengl.Matrix;

import java.util.Vector;

import io.mindblow.humanoid.context.MasterContext;
import io.mindblow.humanoid.canvas.mesh.Bone;


class BoneSequence {
    private Vector<Bone> sequence = new Vector<Bone>();

    private float[] position;
    private float[] direction;
    private float angle;

    void insert(Bone bone)
    {
        sequence.add(bone);
    }

    void setStartingPoint(float[] v3)
    {
        position = v3;
    }

    void setDirection(float angle, float[] v3)
    {
        this.angle = angle;
        direction = v3;
    }

    private float[] follow(float[] matrix)
    {
        float[] context = new float[16];
        Matrix.translateM(context, 0, matrix, 0, position[0], position[1], position[2]);
        Matrix.rotateM(context, 0, angle, direction[0], direction[1], direction[2]);
        return context;
    }

    void redraw(MasterContext masterContext, float[] matrix)
    {
        float[] context = follow(matrix);
        for (Bone bone : sequence)
        {
            bone.orient(masterContext, context);
            bone.redraw(masterContext, context);
            bone.follow(context);
            bone.reorient(masterContext, context);
        }
    }
}
