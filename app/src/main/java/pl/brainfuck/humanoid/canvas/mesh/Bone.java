// This file is a part of Humanoid project.
// Copyright (C) 2018 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid.canvas.mesh;

import android.opengl.Matrix;

import pl.brainfuck.humanoid.HumanContext;
import pl.brainfuck.humanoid.canvas.model.Segment;
import pl.brainfuck.humanoid.sensor.FusionData;


public class Bone {
    private Mesh meshLower;
    private Mesh meshUpper;

    float thicknessRatio = 0.13F;
    float thickness;
    float lengthPart;
    float lengthRemain;

    float length;
    float weight;

    Segment segment;

    public void setThicknessRatio(float newThicknessRatio)
    {
        thicknessRatio = newThicknessRatio;
        init();
    }

    float colors[][] = new float[][]{
            {
                    ((float) 0xFF) / 255.0F, ((float) 0x74) / 255.0F, ((float) 0x00) / 255.0F
            },
            {
                    ((float) 0xFC) / 255.0F, ((float) 0x85) / 255.0F, ((float) 0x23) / 255.0F
            },
            {
                    ((float) 0x58) / 255.0F, ((float) 0x42) / 255.0F, ((float) 0x2F) / 255.0F}
    };

    int meshLowerColorOrder[] = new int[]{0, 1, 2};
    int meshUpperColorOrder[] = new int[]{1, 2, 0};

    float[] getMeshCoords(float root, float depth, float radius) {
        return new float[]{
                0, 0, root,
                -radius, -radius, depth,
                -radius, radius, depth,
                0, 0, root,
                -radius, radius, depth,
                radius, radius, depth,
                0, 0, root,
                radius, radius, depth,
                radius, -radius, depth,
                0, 0, root,
                radius, -radius, depth,
                -radius, -radius, depth
        };
    }

    short[] getMeshOrder() {
        return new short[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    }

    float[] getMeshColors(int[] colorIndex) {
        return new float[]{
                colors[colorIndex[0]][0], colors[colorIndex[0]][1], colors[colorIndex[0]][2], 1.0F,
                colors[colorIndex[1]][0], colors[colorIndex[1]][1], colors[colorIndex[1]][2], 1.0F,
                colors[colorIndex[2]][0], colors[colorIndex[2]][1], colors[colorIndex[2]][2], 1.0F,
                colors[colorIndex[0]][0], colors[colorIndex[0]][1], colors[colorIndex[0]][2], 1.0F,
                colors[colorIndex[1]][0], colors[colorIndex[1]][1], colors[colorIndex[1]][2], 1.0F,
                colors[colorIndex[2]][0], colors[colorIndex[2]][1], colors[colorIndex[2]][2], 1.0F,
                colors[colorIndex[0]][0], colors[colorIndex[0]][1], colors[colorIndex[0]][2], 1.0F,
                colors[colorIndex[1]][0], colors[colorIndex[1]][1], colors[colorIndex[1]][2], 1.0F,
                colors[colorIndex[2]][0], colors[colorIndex[2]][1], colors[colorIndex[2]][2], 1.0F,
                colors[colorIndex[0]][0], colors[colorIndex[0]][1], colors[colorIndex[0]][2], 1.0F,
                colors[colorIndex[1]][0], colors[colorIndex[1]][1], colors[colorIndex[1]][2], 1.0F,
                colors[colorIndex[2]][0], colors[colorIndex[2]][1], colors[colorIndex[2]][2], 1.0F
        };
    }

    private void init()
    {
        thickness = length * thicknessRatio;
        lengthPart = length * weight;
        lengthRemain = length * (1.0F - weight);

        meshLower = new Mesh(getMeshCoords(0, lengthPart, thickness), getMeshOrder(), getMeshColors(meshLowerColorOrder));
        meshUpper = new Mesh(getMeshCoords(length, lengthPart, thickness), getMeshOrder(), getMeshColors(meshUpperColorOrder));
    }

    public Bone(Segment segment, float length, float weight) {
        this.segment = segment;

        this.length = length;
        this.weight = weight;

        init();
    }

    public void orient(HumanContext humanContext, float[] matrix) {
        FusionData fusionData = humanContext.get(this.segment);
        if (fusionData != null) {
            Matrix.rotateM(matrix, 0, fusionData.roll, 0, 1, 0);
            Matrix.rotateM(matrix, 0, fusionData.pitch, 1, 0, 0);
        }
    }

    public void reorient(HumanContext humanContext, float[] matrix) {
        FusionData fusionData = humanContext.get(this.segment);
        if (fusionData != null) {
            Matrix.rotateM(matrix, 0, -fusionData.pitch, 1, 0, 0);
            Matrix.rotateM(matrix, 0, -fusionData.roll, 0, 1, 0);
        }
    }

    public void redraw(HumanContext humanContext, float[] mvpMatrix) {
        meshLower.draw(mvpMatrix);
        meshUpper.draw(mvpMatrix);
    }

    public void follow(float[] matrix) {
        Matrix.translateM(matrix, 0, 0, 0, length);
    }

    public float getThickness() {
        return thickness;
    }

    public float getThicknessOffset() {
        return lengthPart;
    }
}
