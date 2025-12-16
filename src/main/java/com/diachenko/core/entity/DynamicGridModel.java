package com.diachenko.core.entity;


/*  Gravity-Simulation
    12.12.2025
    @author DiachenkoDanylo
*/

import com.diachenko.core.util.Constants;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.List;


public class DynamicGridModel {

    private final int gridSize;
    private final float spacing;
    private int vaoId;
    private int vboId;
    private int vertexCount;


    private Vector3f[][] points;
    private float[] vertices;

    public DynamicGridModel(int gridSize, float spacing) {
        this.gridSize = gridSize;
        this.spacing = spacing;

        createGridPoints();
        buildVertices();
        createVAO();
    }

    private void createGridPoints() {
        points = new Vector3f[gridSize][gridSize];
        float half = (gridSize - 1) * spacing * 0.5f;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                float x = -half + i * spacing;
                float z = -half + j * spacing;
                points[i][j] = new Vector3f(x, 0f, z);
            }
        }
    }

    private void buildVertices() {

        vertexCount = (gridSize * (gridSize - 1)) * 5;
        vertices = new float[vertexCount * 4];

        int idx = 0;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize - 1; j++) {
                // X lines
                Vector3f a = points[i][j];
                Vector3f b = points[i][j + 1];
                vertices[idx++] = a.x;
                vertices[idx++] = a.y;
                vertices[idx++] = a.z;
                vertices[idx++] = b.x;
                vertices[idx++] = b.y;
                vertices[idx++] = b.z;

                // Z lines
                Vector3f c = points[j][i];
                Vector3f d = points[j + 1][i];
                vertices[idx++] = c.x;
                vertices[idx++] = c.y;
                vertices[idx++] = c.z;
                vertices[idx++] = d.x;
                vertices[idx++] = d.y;
                vertices[idx++] = d.z;
            }
        }
    }

    private void createVAO() {
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);

        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices).flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_DYNAMIC_DRAW);

        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * Float.BYTES, 0);
        GL20.glEnableVertexAttribArray(0);

        GL30.glBindVertexArray(0);
    }


    public void applyGravity(List<PlanetEntity> planets) {
        float maxY = -Constants.MAXIMAL_DEPTH;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {

                float y = 0f;
                Vector3f point = points[i][j];
                for (PlanetEntity planet : planets) {
                    Vector3f pos = planet.getPosition();
                    float mass = planet.getMass();
                    float dx = point.x - pos.x;
                    float dz = point.z - pos.z;
                    float distSq = dx * dx + dz * dz + 0.01f;
                    y -= mass / distSq * Constants.CURVATURE; // curvature value
                    float r = (float) Math.sqrt(distSq);
//                    float sigma = Constants.CURVATURE_RADIUS; // 30–80
                    float sigma = planet.getMass() / 5000;
                    sigma *= sigma;
                    float influence = (float) Math.exp(-(r * r) / (2f * sigma * sigma));
                    y -= mass * influence * Constants.CURVATURE;

                }

                //MAX DEPTH
                if (y < maxY) y = maxY;

                point.y = y;
            }
        }

        for (int i = 1; i < gridSize - 1; i++) {
            for (int j = 1; j < gridSize - 1; j++) {
                points[i][j].y = (points[i - 1][j].y + points[i + 1][j].y +
                        points[i][j - 1].y + points[i][j + 1].y +
                        points[i][j].y) / 20f;
            }
        }

        buildVertices();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices).flip();
        GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
    }

    public void render() {
        GL30.glBindVertexArray(vaoId);
        GL11.glDrawArrays(GL11.GL_LINES, 0, vertexCount);
        GL30.glBindVertexArray(0);
    }
}
