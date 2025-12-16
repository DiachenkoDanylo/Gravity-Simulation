package com.diachenko;


/*  Default (Template) Project
    04.12.2025
    @author DiachenkoDanylo
*/


import com.diachenko.core.EngineManager;
import com.diachenko.core.io.WindowManager;
import com.diachenko.core.util.Constants;
import com.diachenko.test.TestGame;
import lombok.Getter;

public class Launcher {

    @Getter
    private static WindowManager window;

    @Getter
    private static TestGame logic;

    public static void main(String[] args) {
        window = new WindowManager(Constants.TITLE, Constants.WIDTH, Constants.HEIGHT, false);
        logic = new TestGame();
        EngineManager engineManager = new EngineManager();
        try {
            engineManager.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}