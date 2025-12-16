package com.diachenko.core;


/*  Gravity-Simulation
    10.12.2025
    @author DiachenkoDanylo
*/

import com.diachenko.core.entity.ModelGenerator;
import com.diachenko.core.entity.PlanetEntity;
import com.diachenko.core.entity.SphereModel;
import com.diachenko.core.io.Camera;
import com.diachenko.core.util.Constants;
import com.diachenko.core.util.Transformation;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PlanetProcessor {

    public boolean activeBlackHole = Boolean.FALSE;
    Object[] sphereData = ModelGenerator.generateSphere(2f, 36, 18);
    float[] verticesSphere = (float[]) sphereData[0];
    int[] indicesSphere = (int[]) sphereData[1];
    float[] textureCoords = (float[]) sphereData[2];
    List<PlanetEntity> planetList = new ArrayList<>();
    ObjectLoader loader = new ObjectLoader();
    SphereModel defaultSphere = loader.loadModelSphere(verticesSphere, textureCoords, indicesSphere);
    String[] planetNames = Constants.DEFAULT_PLANET_ARR;
    Map<String, SphereModel> map = loader.loadModelDefaultSphereList(verticesSphere, textureCoords, indicesSphere,planetNames);
    public PlanetProcessor() {
        planetList = generateDefaultPlanetList();
    }

    public List<PlanetEntity> getPlanetList() {
        return planetList;
    }

    public List<PlanetEntity> generateDefaultPlanetList() {

        List<PlanetEntity> planets = new ArrayList<>();

        PlanetEntity sun = new PlanetEntity(
                map.get("Sun"),
                new Vector3f(0, 3, 0),
                new Vector3f(0, 0, 0),
                3.5f,
                13000f,
                "Sun",
                new Vector3f(0, 0, 0)
        );

        PlanetEntity mercury = new PlanetEntity(
                map.get("Mercury"),
                new Vector3f(40, 3, 0),
                new Vector3f(0, 0, 0),
                1.5f,
                500f,
                "Mercury",
                new Vector3f(0.5f, 0, 0.5f)
        );

        PlanetEntity venus = new PlanetEntity(
                map.get("Venus"),
                new Vector3f(60, 3, 0),
                new Vector3f(0, 0, 0),
                0.8f,
                400f,
                "Venus",
                new Vector3f(0.5f, 0, 0.5f)
        );

        PlanetEntity earth = new PlanetEntity(
                map.get("Earth"),
                new Vector3f(80, 3, 0),
                new Vector3f(0, 0, 0),
                1.2f,
                330f,
                "Earth",
                new Vector3f(0.5f, 0, 0.5f)
        );

        PlanetEntity moon = new PlanetEntity(
                map.get("Moon"),
                new Vector3f(85, 3, 0),
                new Vector3f(0, 0, 0),
                0.3f,
                30f,
                "Moon",
                new Vector3f(0.5f, 0, 0.5f)
        );

        PlanetEntity mars = new PlanetEntity(
                map.get("Mars"),
                new Vector3f(109, 3, 0),
                new Vector3f(0, 0, 0),
                0.9f,
                230f,
                "Mars",
                new Vector3f(0.5f, 0, 0.5f)
        );


        PlanetEntity jupiter = new PlanetEntity(
                map.get("Jupiter"),
                new Vector3f(165, 3, 0),
                new Vector3f(0, 0, 0),
                2.1f,
                450f,
                "Jupiter",
                new Vector3f(0.5f, 0, 0.5f)
        );

        planets.add(sun);
        planets.add(mercury);
        planets.add(venus);
        planets.add(earth);
        planets.add(moon);
        planets.add(mars);
        planets.add(jupiter);

        Physics.setInitialOrbitalVelocities(planets, sun);
        moon.setVelocity(Physics.orbitalVelocity(moon.getPosition(), sun, moon.getScale()));

        return planets;
    }


    public void createBlackHole(Camera camera) {
        PlanetEntity blackHole = new PlanetEntity(
                defaultSphere,
                Transformation.inFrontOfCamera(camera, 10f),
                new Vector3f(0, 0, 0),
                1f,
                Constants.BLACKHOLE_MASS,
                "BLACKHOLE",
                new Vector3f(0, 0, 0)
        );
        activeBlackHole = true;
        planetList.add(blackHole);
    }

}
