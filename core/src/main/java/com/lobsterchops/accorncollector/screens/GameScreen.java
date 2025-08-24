package com.lobsterchops.accorncollector.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class GameScreen implements Screen {
    private ScreenManager screenManager;
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout layout;
    private float gameTime;
    
    public GameScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();
        this.gameTime = 0f;
        
        // Make font bigger and white
        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);
    }

    @Override
    public void show() {
        // Game screen is now active
        // Initialize your game state here
    }

    @Override
    public void render(float delta) {
        // Handle input
        handleInput();
        
        // Clear screen with black background
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // TODO: Add your game logic here
        // This is where you'll render your game objects, handle updates, etc.
        
        // Uncomment the lines below if you want to show the ESC instruction on screen
        /*
        batch.begin();
        String backText = "Press ESC to return to menu";
        font.getData().setScale(1.0f);
        layout.setText(font, backText);
        float backX = (Gdx.graphics.getWidth() - layout.width) / 2;
        float backY = 30f; // Bottom of screen
        font.draw(batch, backText, backX, backY);
        batch.end();
        */
    }
    
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            // Return to main menu with fade transition
            screenManager.setScreen(
                new MainMenuScreen(screenManager), 
                ScreenManager.TransitionType.FADE, 
                0.5f
            );
        }
    }

    @Override
    public void resize(int width, int height) {
        // Handle screen resize
    }

    @Override
    public void pause() {
        // Handle pause
    }

    @Override
    public void resume() {
        // Handle resume
    }

    @Override
    public void hide() {
        // Screen is no longer active
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (font != null) font.dispose();
    }
}