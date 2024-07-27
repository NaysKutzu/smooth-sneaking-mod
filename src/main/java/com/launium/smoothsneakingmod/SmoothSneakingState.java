package com.launium.smoothsneakingmod;

import org.lwjgl.Sys;

public class SmoothSneakingState {
    private boolean lastState;
    private boolean isAnimationDone;
    private long lastOperationTime;
    private float lastX;

    private static float getY(float x) {
        // quadratic function
        x--;
        return 0.08F * x * x - 0.08F;
    }

    public float getSneakingHeightOffset(boolean isSneaking) {
        if (lastState == isSneaking) {
            if (isAnimationDone) {
                return isSneaking ? -0.08F : 0F;
            }
        } else {
            lastState = isSneaking;
            isAnimationDone = false;
        }
        long now = Sys.getTime();
        float timeDiff = now - lastOperationTime;
        if (lastOperationTime == 0F) timeDiff = 0F;
        lastOperationTime = now;
        if (isSneaking) {
            if (lastX < 1F) {
                lastX += (timeDiff / 150F);
                if (lastX > 1F) lastX = 1F;
                return getY(lastX);
            } else {
                lastX = 1F;
                isAnimationDone = true;
                lastOperationTime=0L;
                return -0.08F;
            }
        } else {
            if (lastX > 0) {
                lastX -= (timeDiff / 150F);
                if (lastX < 0F) lastX = 0F;
                return getY(lastX);
            } else {
                lastX = 0F;
                isAnimationDone = true;
                lastOperationTime=0L;
                return 0F;
            }
        }
    }
}
