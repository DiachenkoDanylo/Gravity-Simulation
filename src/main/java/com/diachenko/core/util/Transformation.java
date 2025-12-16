package com.diachenko.core.util;


/*  Gravity-Simulation
    06.12.2025
    @author DiachenkoDanylo
*/

import com.diachenko.core.entity.basic.Entity;
import com.diachenko.core.io.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {

    public static Matrix4f createTransformationMatrix(Entity entity) {
        Matrix4f transformationMatrix = new Matrix4f();
        transformationMatrix.identity().translate(entity.getPosition()).
                rotateX((float) Math.toRadians(entity.getRotation().x)).
                rotateY((float) Math.toRadians(entity.getRotation().y)).
                rotateZ((float) Math.toRadians(entity.getRotation().z)).
                scale(entity.getScale());
        return transformationMatrix;
    }

    public static Matrix4f getViewMatrix(Camera camera) {
        Vector3f position = camera.getPosition();
        Vector3f rotation = camera.getRotation();
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0))
                .rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0))
                .rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1));
        viewMatrix.translate(-position.x, -position.y, -position.z);
        return viewMatrix;
    }

    public static Vector3f inFrontOfCamera(Camera cam, float dist) {
        Matrix4f view = Transformation.getViewMatrix(cam);

        Vector3f forward = new Vector3f();
        view.positiveZ(forward).negate().normalize(); // camera forward

        return new Vector3f(cam.getPosition()).add(forward.mul(dist));
    }
}
