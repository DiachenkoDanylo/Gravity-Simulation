package com.diachenko.test;


/*  Gravity-Simulation
    06.12.2025
    @author DiachenkoDanylo
*/

import com.diachenko.Launcher;
import com.diachenko.core.*;
import com.diachenko.core.entity.PlanetEntity;
import com.diachenko.core.io.Camera;
import com.diachenko.core.io.MouseInput;
import com.diachenko.core.io.WindowManager;
import com.diachenko.core.util.Constants;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class TestGame implements ILogic {

    private final RenderManager renderManager;
    private final ObjectLoader loader;
    private final WindowManager window;
    Vector3f cameraInc;
    private PlanetProcessor planetProcessor;
    private final Camera camera;
    private List<PlanetEntity> planets;

    public TestGame() {
        renderManager = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
        cameraInc = new Vector3f(0f, 0f, 0f);
    }

    @Override
    public void init() throws Exception {
        renderManager.init();
        planetProcessor = new PlanetProcessor();
        planets = planetProcessor.getPlanetList();
    }

    @Override
    public void input() {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
            cameraInc.z = -1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            cameraInc.x = -1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_Z)) {
            cameraInc.y = -1;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
    }

    @Override
    public void update(float interval, MouseInput input) {
        if (window.isKeyPressed(GLFW.GLFW_KEY_Q) && input.isLeftButtonPress() && !planetProcessor.activeBlackHole) {
            planetProcessor.createBlackHole(camera);
            planets = planetProcessor.getPlanetList();
        }

        camera.movePosition(cameraInc.x * Constants.CAMERA_STEP,
                cameraInc.y * Constants.CAMERA_STEP,
                cameraInc.z * Constants.CAMERA_STEP);

        if (input.isRightButtonPress()) {
            Vector2f rotVec = input.getDisplVec();
            camera.moveRotation(rotVec.x * Constants.MOUSE_SENSITIVITY,
                    rotVec.y * Constants.MOUSE_SENSITIVITY,
                    0);
        }

        Physics.updatePlanetsVervlet(planets, interval);

    }

    @Override
    public void render() {
        if (window.isResize()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(false);
        }
        window.setClearColour(0.1f, 0.1f, 0.1f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        renderManager.renderAll(planets, camera);
    }

    @Override
    public void cleanup() {
        renderManager.cleanup();
        loader.cleanup();
    }
}
