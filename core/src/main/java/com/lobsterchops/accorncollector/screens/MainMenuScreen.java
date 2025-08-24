package com.lobsterchops.accorncollector.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class MainMenuScreen implements Screen {
    private ScreenManager screenManager;
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout layout;
    private float blinkTimer;
    private boolean showText;
    
    public MainMenuScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();
        this.blinkTimer = 0f;
        this.showText = true;
        
        // Make font bigger and white
        font.getData().setScale(2.0f);
        font.setColor(Color.WHITE);
    }

    @Override
    public void show() {
        // Screen is now active
    }

    @Override
    public void render(float delta) {
        // Handle input
        handleInput();
        
        // Update blink timer
        blinkTimer += delta;
        if (blinkTimer >= 0.8f) {
            showText = !showText;
            blinkTimer = 0f;
        }
        
        // Clear screen with dark blue background
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Render UI
        batch.begin();
        
        // Title
        String title = "Acorn Collector";
        layout.setText(font, title);
        float titleX = (Gdx.graphics.getWidth() - layout.width) / 2;
        float titleY = Gdx.graphics.getHeight() * 0.7f;
        font.draw(batch, title, titleX, titleY);
        
        // Blinking "Press Enter to Play" text
        if (showText) {
            String prompt = "Press Enter to Play";
            layout.setText(font, prompt);
            float promptX = (Gdx.graphics.getWidth() - layout.width) / 2;
            float promptY = Gdx.graphics.getHeight() * 0.4f;
            font.draw(batch, prompt, promptX, promptY);
        }
        
        batch.end();
    }
    
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            // Transition to game screen with slide effect
            screenManager.setScreen(
                new GameScreen(screenManager), 
                ScreenManager.TransitionType.SLIDE_LEFT, 
                0.7f
            );
        }
        
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
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