package me.Goldensang.gamelogic.utils;

import me.Goldensang.gameobject.Bird;

public class GravityUtil {

    private final int gravityUpperThreshold = 15;
    private static GravityUtil gravityUtil = null;

    private GravityUtil() {}

    public static GravityUtil getInstance() {
        if (gravityUtil == null) {
            gravityUtil = new GravityUtil();
        }
        return gravityUtil;
    }

    public void updateBirdGravityScale(Bird b, int currentTickRate) {
        if (currentTickRate % 2 == 0 && b.getGravityScale() <  gravityUpperThreshold) {
            b.setGravityScale(b.getGravityScale() + 2);
        }
    }
}
