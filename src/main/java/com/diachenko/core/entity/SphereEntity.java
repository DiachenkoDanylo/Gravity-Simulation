package com.diachenko.core.entity;


/*  Gravity-Simulation
    07.12.2025
    @author DiachenkoDanylo
*/

import com.diachenko.core.entity.basic.Entity;
import com.diachenko.core.entity.basic.Model;
import org.joml.Vector3f;

public class SphereEntity extends Entity {

    public SphereEntity(Model model, Vector3f position, Vector3f rotation, float scale) {
        super(model, position, rotation, scale);
    }
}
