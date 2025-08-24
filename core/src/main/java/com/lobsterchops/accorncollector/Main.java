package com.lobsterchops.accorncollector;

import com.badlogic.gdx.Game;
import com.lobsterchops.accorncollector.screens.MainMenuScreen;
import com.lobsterchops.accorncollector.screens.ScreenManager;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private ScreenManager screenManager;
    
    @Override
    public void create() {
        screenManager = new ScreenManager(this);
        screenManager.setScreen(new MainMenuScreen(screenManager));
    }
    
    public ScreenManager getScreenManager() {
        return screenManager;
    }
    
    @Override
    public void dispose() {
        super.dispose();
        if (screenManager != null && screenManager.getCurrentScreen() != null) {
            screenManager.getCurrentScreen().dispose();
        }
    }
}