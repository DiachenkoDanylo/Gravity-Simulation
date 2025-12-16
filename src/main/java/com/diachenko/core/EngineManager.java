package com.diachenko.core;


/*  Gravity-Simulation
    05.12.2025
    @author DiachenkoDanylo
*/

import com.diachenko.Launcher;
import com.diachenko.core.io.MouseInput;
import com.diachenko.core.io.WindowManager;
import com.diachenko.core.util.Constants;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

public class EngineManager {

    public static final long NANOSECOND = 1000000000L;
    private static int fps;
    private static final float frametime = 1.0f / Constants.FRAMERATE;

    private boolean isRunning;

    private WindowManager window;
    private MouseInput mouseInput;
    private GLFWErrorCallback errorCallback;

    private ILogic logic;

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }

    private void init() throws Exception {
        GLFW.glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        window = Launcher.getWindow();
        logic = Launcher.getLogic();
        mouseInput = new MouseInput();
        window.init();
        mouseInput.init();
        logic.init();
    }

    public void start() throws Exception {
        init();
        if (isRunning) return;
        run();
    }

    public void run() {
        this.isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning) {
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) NANOSECOND;
            frameCounter += passedTime;

            while (unprocessedTime > frametime) {
                render = true;
                unprocessedTime -= frametime;

                if (window.windowShouldClose()) {
                    stop();
                }
                if (frameCounter >= NANOSECOND) {
                    setFps(frames);
                    window.setTitle(Constants.TITLE + getFps());
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if (render) {
                input();
                update(frametime);
                render();
                frames++;
            }
        }
        cleanup();
    }

    private void stop() {
        if (!isRunning) return;
        isRunning = false;
    }

    private void input() {
        mouseInput.input();
        logic.input();
    }

    private void render() {
        logic.render();
        window.update();
    }

    private void update(float interval) {
        logic.update(interval, mouseInput);
    }

    private void cleanup() {
        logic.cleanup();
        window.cleanup();
        errorCallback.free();
        GLFW.glfwTerminate();
    }
}
