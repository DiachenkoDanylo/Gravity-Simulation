package com.diachenko.core;


/*  Gravity-Simulation
    07.12.2025
    @author DiachenkoDanylo
*/

import com.diachenko.core.entity.PlanetEntity;
import com.diachenko.core.util.Constants;
import org.joml.Vector3f;

import java.util.List;
import java.util.Objects;

public class Physics {

    private static final float G = Constants.GRAVITY_FORCE;

    public static void updatePlanetsVervlet(List<PlanetEntity> planets, float dt) {
        Vector3f[] accelerations = new Vector3f[planets.size()];

        // 1. current
        for (int i = 0; i < planets.size(); i++) {
            PlanetEntity pi = planets.get(i);
            Vector3f acc = new Vector3f(0, 0, 0);

            for (int j = 0; j < planets.size(); j++) {
                if (i == j) continue;
                PlanetEntity pj = planets.get(j);

                Vector3f dir = new Vector3f(pj.getPosition()).sub(pi.getPosition());
                float distanceSq = dir.lengthSquared();

                float minDist = pi.getScale() + pj.getScale();
                if (distanceSq < minDist * minDist) distanceSq = minDist * minDist;

                float force = G * pj.getMass() / distanceSq;
                acc.fma(force, dir.normalize());
                pj.addTrailPoint(pj.getPosition());
                pj.updateTrailVbo();
            }
            accelerations[i] = acc;
        }

        for (int i = 0; i < planets.size(); i++) {
            PlanetEntity p = planets.get(i);
            Vector3f oldAcc = accelerations[i];

            Vector3f deltaPos = new Vector3f(p.getVelocity()).mul(dt).add(new Vector3f(oldAcc).mul(0.5f * dt * dt));
            p.incPos(deltaPos);

            Vector3f newAcc = new Vector3f(0, 0, 0);
            for (int j = 0; j < planets.size(); j++) {
                if (i == j) continue;
                PlanetEntity pj = planets.get(j);
                Vector3f dir = new Vector3f(pj.getPosition()).sub(p.getPosition());
                float distanceSq = dir.lengthSquared();
                float minDist = p.getScale() + pj.getScale();
                if (distanceSq < minDist * minDist) distanceSq = minDist * minDist;

                float force = G * pj.getMass() / distanceSq;
                newAcc.fma(force, dir.normalize());
            }

            // Exclude Sun
            if (!p.getName().equals("Sun")) {
                p.getVelocity().fma(0.5f * dt, oldAcc.add(newAcc));
            }
        }
    }

    public static void setInitialOrbitalVelocities(List<PlanetEntity> planets, PlanetEntity central) {
        for (PlanetEntity planet : planets) {
            if (Objects.equals(planet.getName(), central.getName()) || Objects.equals(planet.getName(), "Moon"))
                continue; // exclude Sun and moon
            Vector3f dir = new Vector3f(planet.getPosition()).sub(central.getPosition());
            float r = dir.length();
            //v = sqrt(G * M / r)
            float v = (float) Math.sqrt(G * central.getMass() / r);
            Vector3f tangent = new Vector3f(-dir.z, 0, dir.x).normalize();
            tangent.mul(v);
            planet.setVelocity(tangent);
        }
    }

    public static Vector3f orbitalVelocity(Vector3f planetPos, PlanetEntity entity, float planetRadius) {
        Vector3f dir = new Vector3f(planetPos).sub(entity.getPosition());
        float r = dir.length();
        float minR = entity.getScale() + planetRadius;
        if (r < minR) r = minR;
        float v = (float) Math.sqrt(G * entity.getMass() / r);
        Vector3f tangent = new Vector3f(-dir.z, 0, dir.x).normalize();
        return tangent.mul(v);
    }

}

