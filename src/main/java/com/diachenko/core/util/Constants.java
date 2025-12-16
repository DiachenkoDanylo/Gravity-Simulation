package com.diachenko.core.util;


/*  Gravity-Simulation
    06.12.2025
    @author DiachenkoDanylo
*/

import org.joml.Vector3f;

public class Constants {

    public static final String TITLE = "DEV ENGINE";
    public static final int WIDTH = 1600;
    public static final int HEIGHT = 900;

    public static final float FOV = (float) Math.toRadians(60);

    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000f;

    public static final float MOUSE_SENSITIVITY = 0.2f;
    public static final float CAMERA_STEP = 0.25f;

    //Dynamic Grid
    public static final Float CURVATURE = 1.75f;
    public static final Float MAXIMAL_DEPTH = 35f;

    // Engine works based on framerate
    public static final long FRAMERATE = 120;

    // Default texture name on textures package
    public static final String DEFAULT_TEXTURE = "checkerNew.png";
    public static final Float GRAVITY_FORCE = 0.005f;
    public static final Float BLACKHOLE_MASS = 10_000_000f;

    //
    public static final Vector3f DEFAULT_CAMERA_POS = new Vector3f(150, 30, 100);
    public static final Vector3f DEFAULT_CAMERA_ROT = new Vector3f(20, -35, 0);

    //Planets list
    // Name of object should have the same texture name
    public static final String [] DEFAULT_PLANET_ARR = {"Sun","Mercury","Venus", "Earth", "Moon","Mars","Jupiter"};


}
