// This file is a part of Humanoid project.
// Copyright (C) 2018 Aleksander Gajewski <adiog@brainfuck.pl>.

package pl.brainfuck.humanoid.canvas.model;

import android.opengl.Matrix;

import java.util.HashMap;
import java.util.Map;

import pl.brainfuck.humanoid.HumanContext;
import pl.brainfuck.humanoid.canvas.mesh.Bone;


public class Skeleton {
    Bone pelvis;
    BoneSequence spine = new BoneSequence();
    Map<Sequence, BoneSequence> limbs = new HashMap<Sequence, BoneSequence>();

    public Skeleton(){
        pelvis = new Bone(Segment.Pelvis, 0.3F, 0.5F);

        Bone chest = new Bone(Segment.Chest, 0.60F, 0.7F);

        spine.insert(chest);
        Bone head = new Bone(Segment.Head, 0.30F, 0.5F);
        head.setThicknessRatio(0.23F);
        spine.insert(head);

        spine.setStartingPoint(new float[]{0.0F, 1.0F + pelvis.getThickness(), 0.0F});
        spine.setDirection(-90, new float[]{1, 0, 0});

        BoneSequence leftLeg = new BoneSequence();
        leftLeg.insert(new Bone(Segment.UpperLegLeft, 0.45F, 0.3F));
        leftLeg.insert(new Bone(Segment.LowerLegLeft, 0.45F, 0.3F));
        leftLeg.insert(new Bone(Segment.FootLeft, 0.20F, 0.3F));

        leftLeg.setStartingPoint(new float[]{-0.15F, 1.0F, 0.0F});
        leftLeg.setDirection(90, new float[]{1, 0, 0});

        limbs.put(Sequence.LegLeft, leftLeg);

        BoneSequence rightLeg = new BoneSequence();
        rightLeg.insert(new Bone(Segment.UpperLegRight, 0.45F, 0.3F));
        rightLeg.insert(new Bone(Segment.LowerLegRight, 0.45F, 0.3F));
        rightLeg.insert(new Bone(Segment.FootRight, 0.20F, 0.3F));

        rightLeg.setStartingPoint(new float[]{0.15F, 1.0F, 0.0F});
        rightLeg.setDirection(90, new float[]{1, 0, 0});

        limbs.put(Sequence.LegRight, rightLeg);

        BoneSequence leftArm = new BoneSequence();
        leftArm.insert(new Bone(Segment.UpperArmLeft, 0.30F, 0.3F));
        leftArm.insert(new Bone(Segment.ForearmLeft, 0.30F, 0.3F));
        leftArm.insert(new Bone(Segment.HandLeft, 0.15F, 0.3F));

        leftArm.setStartingPoint(new float[]{-chest.getThickness(), 1.0F + pelvis.getThickness() + chest.getThicknessOffset(), 0});
        leftArm.setDirection(-90, new float[]{0, 1, 0});

        limbs.put(Sequence.ArmLeft, leftArm);

        BoneSequence rightArm = new BoneSequence();
        rightArm.insert(new Bone(Segment.UpperArmRight, 0.30F, 0.3F));
        rightArm.insert(new Bone(Segment.ForearmRight, 0.30F, 0.3F));
        rightArm.insert(new Bone(Segment.HandRight, 0.15F, 0.3F));

        rightArm.setStartingPoint(new float[]{chest.getThickness(), 1.0F + pelvis.getThickness() + chest.getThicknessOffset(), 0});
        rightArm.setDirection(90, new float[]{0, 1, 0});

        limbs.put(Sequence.ArmRight, rightArm);
    }

    void redraw(HumanContext humanContext, float[] matrix)
    {
        {
            float[] context = new float[16];
            Matrix.translateM(context, 0, matrix, 0, -0.15F, 1.0F, 0.0F);
            Matrix.rotateM(context, 0, 90, 0, 1, 0);
            pelvis.redraw(humanContext, context);
        }

        {
            float[] context = new float[16];
            Matrix.translateM(context, 0, matrix, 0, 0, 0.0F, 0.0F);
            spine.redraw(humanContext, context);
        }

        for (BoneSequence boneSequence : limbs.values())
        {
            float[] context = new float[16];
            Matrix.translateM(context, 0, matrix, 0, 0, 0.0F, 0.0F);
            boneSequence.redraw(humanContext, context);
        }
    }
}
