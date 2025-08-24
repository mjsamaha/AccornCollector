package com.lobsterchops.accorncollector.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class GameScreen implements Screen {
	
    private ScreenManager screenManager;
    
    FitViewport viewport;
    
    /* Fonts */
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout layout;
    private float gameTime;
    
    /* Graphics */
    Texture backgroundTexture;
    Texture playerTexture;
    Texture accornTexture;
    
    Sound collectSound;
    Sound menuInteractionSound;
    
    Music gameplayMusic;
   
    
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
    	
    	viewport = new FitViewport(8, 6);
    	
    	backgroundTexture = new Texture("background.png");
    	
        // Game screen is now active
        // Initialize your game state here
    	
    	// add in assets --> name = new Texture("filename");
    	
    	// sound = Gdx.audio.newSound(Gdx.files.internal("file"));
    	// music = Gdx.audio.newMusic(Gdx.files.internal("file"));
    	
    }

    @Override
    public void render(float delta) {
        // Handle input
        handleInput();
        // logic();
        draw();
        
        // Clear screen with black background
        // Gdx.gl.glClearColor(0, 0, 0, 1);
        // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        
        
       
    }
    
    private void logic() {
    	
    }
    
    private void draw() {
    	ScreenUtils.clear(Color.BLACK);
    	viewport.apply();
    	batch.setProjectionMatrix(viewport.getCamera().combined);
    	
    	batch.begin();
    	
    	float worldWidth = viewport.getWorldWidth();
    	float worldHeight = viewport.getWorldHeight();
    	
    	batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
    	
    	
    	
    	batch.end();
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
    	viewport.update(width, height, true);
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