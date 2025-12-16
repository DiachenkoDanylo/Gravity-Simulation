package com.diachenko.core.entity;


/*  Gravity-Simulation
    07.12.2025
    @author DiachenkoDanylo
*/

import java.util.ArrayList;
import java.util.List;

public class ModelGenerator {

    public static Object[] generateSphere(float radius, int sectorCount, int stackCount) {
        List<Float> vertices = new ArrayList<>();
        List<Float> texCoords = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        float PI = (float) Math.PI;

        // ===== Vertices + UV =====
        for (int i = 0; i <= stackCount; i++) {
            float stackAngle = PI / 2 - i * PI / stackCount; // +90 -> -90
            float xy = radius * (float) Math.cos(stackAngle);
            float y = radius * (float) Math.sin(stackAngle);

            for (int j = 0; j <= sectorCount; j++) {
                float sectorAngle = j * 2 * PI / sectorCount;

                float x = xy * (float) Math.cos(sectorAngle);
                float z = xy * (float) Math.sin(sectorAngle);

                // position
                vertices.add(x);
                vertices.add(y);
                vertices.add(z);

                // texture coords
                float u = (float) j / sectorCount;
                float v = (float) i / stackCount;
                texCoords.add(u);
                texCoords.add(1.0f - v); // flip V
            }
        }

        // ===== Indices =====
        for (int i = 0; i < stackCount; i++) {
            int k1 = i * (sectorCount + 1);
            int k2 = k1 + sectorCount + 1;

            for (int j = 0; j < sectorCount; j++, k1++, k2++) {
                if (i != 0) {
                    indices.add(k1);
                    indices.add(k2);
                    indices.add(k1 + 1);
                }
                if (i != stackCount - 1) {
                    indices.add(k1 + 1);
                    indices.add(k2);
                    indices.add(k2 + 1);
                }
            }
        }

        // ===== Convert to arrays =====
        float[] vertArr = new float[vertices.size()];
        for (int i = 0; i < vertices.size(); i++) vertArr[i] = vertices.get(i);

        float[] texArr = new float[texCoords.size()];
        for (int i = 0; i < texCoords.size(); i++) texArr[i] = texCoords.get(i);

        int[] indArr = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) indArr[i] = indices.get(i);

        return new Object[]{vertArr, indArr, texArr};
    }

}

