package com.diachenko.core.entity.basic;


import lombok.Data;

/*  Gravity-Simulation
    06.12.2025
    @author DiachenkoDanylo
*/
@Data
public class Model {

    private int id;
    private int vertexCount;
    private Texture texture;

    public Model() {
    }

    public Model(int id, int vertexCount) {
        this.id = id;
        this.vertexCount = vertexCount;
    }

    public Model(Model model, Texture texture) {
        this.id = model.id;
        this.vertexCount = model.vertexCount;
        this.texture = texture;
    }

    public Model(Model model) {
        this.id = model.id;
        this.vertexCount = model.vertexCount;
    }

}
