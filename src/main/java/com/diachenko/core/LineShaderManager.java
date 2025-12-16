package com.diachenko.core;


/*  Gravity-Simulation
    07.12.2025
    @author DiachenkoDanylo
*/

import org.joml.Vector3f;

public class LineShaderManager extends ShaderManager {

    public void init(String vertexShaderSource, String fragmentShaderSource) throws Exception {
        createVortexShader(vertexShaderSource);
        createFragmentShader(fragmentShaderSource);
        link();
        createUniform("transformationMatrix");
        createUniform("projectionMatrix");
        createUniform("viewMatrix");
        createUniform("color");
    }

    public void setColor(Vector3f color) {
        setUniform("color", color);
    }
}

