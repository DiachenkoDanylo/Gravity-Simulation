package com.diachenko.core;


/*  Gravity-Simulation
    06.12.2025
    @author DiachenkoDanylo
*/

import com.diachenko.Launcher;
import com.diachenko.core.entity.DynamicGridModel;
import com.diachenko.core.entity.PlanetEntity;
import com.diachenko.core.io.Camera;
import com.diachenko.core.io.WindowManager;
import com.diachenko.core.util.Transformation;
import com.diachenko.core.util.Utils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static org.lwjgl.opengl.ARBInternalformatQuery2.GL_TEXTURE_2D;
import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

public class RenderManager {

    private final WindowManager window;
    private ShaderManager shaderManager;
    private LineShaderManager gridShader;
    private DynamicGridModel grid;

    public RenderManager() {
        window = Launcher.getWindow();
    }

    public void init() throws Exception {

        grid = new DynamicGridModel(250, 1.5f);
        gridShader = new LineShaderManager();
        gridShader.init(Utils.loadResource("/shaders/grid.vs"),
                Utils.loadResource("/shaders/grid.fs"));

        shaderManager = new ShaderManager();
        shaderManager.createVortexShader(Utils.loadResource("/shaders/vertex.vs"));
        shaderManager.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
        shaderManager.link();

        shaderManager.createUniform("textureSampler");
        shaderManager.createUniform("transformationMatrix");
        shaderManager.createUniform("projectionMatrix");
        shaderManager.createUniform("viewMatrix");
    }

    public void renderAll(List<PlanetEntity> entities, Camera camera) {
        clear();
        renderGridDynamically(entities, camera);
        renderTail(entities, camera);
        renderModels(entities, camera);

    }

    public void renderTail(List<PlanetEntity> entities, Camera camera) {
        gridShader.bind();
        gridShader.setUniform("projectionMatrix", window.updateProjectionMatrix());
        gridShader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
        gridShader.setColor(new Vector3f(1f, 0f, 0f)); //RED
        for (PlanetEntity e : entities) {
            e.renderTrail();
        }
        gridShader.unbind();
    }

    public void renderModels(List<PlanetEntity> entities, Camera camera) {
        shaderManager.bind();
        shaderManager.setUniform("projectionMatrix", window.updateProjectionMatrix());
        shaderManager.setUniform("viewMatrix", Transformation.getViewMatrix(camera));


        for (PlanetEntity entity : entities) {
            if (entity.getModel().getTexture() != null) {
                glActiveTexture(GL_TEXTURE0);
                glBindTexture(GL_TEXTURE_2D, entity.getModel().getTexture().id());
                shaderManager.setUniform("textureSampler", 0);
            }

            shaderManager.setUniform("transformationMatrix",
                    Transformation.createTransformationMatrix(entity));

            glBindVertexArray(entity.getModel().getId());
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);

            glDrawElements(GL_TRIANGLES, entity.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glBindVertexArray(0);
        }
        shaderManager.unbind();
    }

    private void renderGridDynamically(List<PlanetEntity> entities, Camera camera) {
        grid.applyGravity(entities);

        gridShader.bind();
        gridShader.setUniform("projectionMatrix", window.updateProjectionMatrix());
        gridShader.setUniform("transformationMatrix", new Matrix4f().identity());
        gridShader.setUniform("viewMatrix", Transformation.getViewMatrix(camera));
        gridShader.setColor(new Vector3f(0.3f, 0.3f, 0.3f));

        grid.render();

        gridShader.unbind();
    }

    public void clear() {
        GL11.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        shaderManager.cleanup();
    }

}

