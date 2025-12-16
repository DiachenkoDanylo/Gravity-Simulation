package com.diachenko.core.entity;


/*  Gravity-Simulation
    07.12.2025
    @author DiachenkoDanylo
*/

import com.diachenko.core.entity.basic.Model;
import com.diachenko.core.entity.basic.Texture;


public class SphereModel extends Model {
    public SphereModel(int id, int vertexCount, Texture texture) {
        super(id, vertexCount);
        setTexture(texture);
    }
}
