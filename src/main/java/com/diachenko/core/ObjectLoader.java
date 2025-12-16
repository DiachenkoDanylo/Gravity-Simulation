package com.diachenko.core;


/*  Gravity-Simulation
    06.12.2025
    @author DiachenkoDanylo
*/

import com.diachenko.core.entity.SphereModel;
import com.diachenko.core.entity.basic.Model;
import com.diachenko.core.entity.basic.Texture;
import com.diachenko.core.util.Constants;
import com.diachenko.core.util.Utils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectLoader {

    //Vertex Array Object s
    private final List<Integer> vaos = new ArrayList<>();
    //Vertex Buffer Object s
    private final List<Integer> vbos = new ArrayList<>();

    private final List<Integer> textures = new ArrayList<>();

    public SphereModel loadModelSphere(float[] vertices, float[] textureCoords, int[] indices) {
        int id = createVAO();
        storeIndiciesBuffer(indices);
        storeDataInAttributeList(0, 3, vertices);
        storeDataInAttributeList(1, 2, textureCoords);
        unbind();
        Texture texture = new Texture(loadTexture("textures/" + Constants.DEFAULT_TEXTURE));
        return new SphereModel(id, indices.length, texture);
    }

    public HashMap<String,SphereModel> loadModelDefaultSphereList(float[] vertices, float[] textureCoords, int[] indices, String[] planetNames) {
        HashMap<String, SphereModel> map = new HashMap<>();
        for (int i = 0; i < planetNames.length; i++) {
            int id = createVAO();
            storeIndiciesBuffer(indices);
            storeDataInAttributeList(0, 3, vertices);
            storeDataInAttributeList(1, 2, textureCoords);
            unbind();
            Texture texture;
            try {
                texture = new Texture(loadTexture("textures/" + planetNames[i] +".png"));
            } catch (Exception e) {
                texture = new Texture(loadTexture("textures/" + Constants.DEFAULT_TEXTURE));
            }
            map.put(planetNames[i], new SphereModel(id, indices.length, texture));
        }
        return map;
    }

    public int loadTexture(String filename) {
        int width, height;
        ByteBuffer buffer;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);
            buffer = STBImage.stbi_load(filename, w, h, c, 4);
            if (buffer == null) {
                throw new RuntimeException("Failed to load texture: " + filename + " \n" + STBImage.stbi_failure_reason());
            }
            width = w.get();
            height = h.get();
        }

        int id = GL11.glGenTextures();
        textures.add(id);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        return id;
    }

    private int createVAO() {
        int id = GL30.glGenVertexArrays();
        vaos.add(id);
        GL30.glBindVertexArray(id);
        return id;
    }

    private void storeIndiciesBuffer(int[] indices) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = Utils.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private void storeDataInAttributeList(int attribute, int vertexCount, float[] data) {
        int vbo = GL15.glGenBuffers();
        vbos.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = Utils.storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attribute, vertexCount, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbind() {
        GL30.glBindVertexArray(0);
    }

    public void cleanup() {
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }
        for (int vbo : vbos) {
            GL30.glDeleteBuffers(vbo);
        }
        for (int texture : textures) {
            GL11.glDeleteTextures(texture);
        }
    }


}

