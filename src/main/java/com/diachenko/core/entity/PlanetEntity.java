package com.diachenko.core.entity;


/*  Gravity-Simulation
    07.12.2025
    @author DiachenkoDanylo
*/


import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class PlanetEntity extends SphereEntity {

    private float mass;
    private Vector3f velocity;
    private final String name;
    private final int MAX_TRAIL = 5000;
    private final List<Vector3f> trail = new ArrayList<>();
    private int trailVboId;
    private int trailVaoId;
    private int trailStepCounter = 0;
    private final int TRAIL_STEP = 30;

    public void initTrailVbo() {
        trailVaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(trailVaoId);

        trailVboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, trailVboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, MAX_TRAIL * 3 * Float.BYTES, GL15.GL_DYNAMIC_DRAW);

        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);

        GL30.glBindVertexArray(0);
    }

    public void addTrailPoint(Vector3f pos) {
        trailStepCounter++;
        if (trailStepCounter % TRAIL_STEP != 0) return;

        trail.add(new Vector3f(pos));
        if (trail.size() > MAX_TRAIL) trail.remove(0);
    }

    public void updateTrailVbo() {

        FloatBuffer buffer = BufferUtils.createFloatBuffer(trail.size() * 3);
        for (Vector3f p : trail) buffer.put(p.x).put(p.y).put(p.z);
        buffer.flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, trailVboId);
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
    }

    public void renderTrail() {
        if (trail.isEmpty()) return;
        GL30.glBindVertexArray(trailVaoId);
        GL11.glDrawArrays(GL11.GL_LINE_STRIP, 0, trail.size());
        GL30.glBindVertexArray(0);
    }

    public PlanetEntity(
            SphereModel model,
            Vector3f position,
            Vector3f rotation,
            float scale,
            float mass,
            String name,
            Vector3f initialVelocity
    ) {
        super(model, position, rotation, scale);
        this.mass = mass;
        this.name = name;
        this.velocity = new Vector3f(initialVelocity);
        initTrailVbo();
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public String getName() {
        return name;
    }
}



