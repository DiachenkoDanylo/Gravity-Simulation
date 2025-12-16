package com.diachenko.core;


/*  Gravity-Simulation
    06.12.2025
    @author DiachenkoDanylo
*/

import com.diachenko.core.io.MouseInput;

public interface ILogic {

    void init() throws Exception;

    void input();

    void update(float interval, MouseInput mouseInput);

    void render();

    void cleanup();
}
