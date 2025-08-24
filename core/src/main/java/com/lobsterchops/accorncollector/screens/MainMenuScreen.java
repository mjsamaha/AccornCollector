package com.lobsterchops.accorncollector.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class MainMenuScreen implements Screen {
	
	private FitViewport viewport;
	
    private ScreenManager screenManager;
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout layout;
    private float blinkTimer;
    private boolean showText;
    
    Texture menuBackgroundTexture;
    
    Music menuMusic;
    
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
    	viewport = new FitViewport(800, 600);
    	
    	menuBackgroundTexture = new Texture("background.png");
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
        
        // Actually draw everything!
        draw();
    }
    
    private void draw() {
    	ScreenUtils.clear(Color.BLACK);
    	viewport.apply();
    	batch.setProjectionMatrix(viewport.getCamera().combined);
    	
    	batch.begin();
    	
    	float worldWidth = viewport.getWorldWidth();
    	float worldHeight = viewport.getWorldHeight();
    	
    	// Draw background first
    	batch.draw(menuBackgroundTexture, 0, 0, worldWidth, worldHeight);
    	    	
    	// Draw UI text using screen coordinates for better control
    	
    	// Draw title
    	String title = "Acorn Collector";
    	layout.setText(font, title);
    	float titleX = (Gdx.graphics.getWidth() - layout.width) / 2;
    	float titleY = Gdx.graphics.getHeight() * 0.7f;
    	font.draw(batch, title, titleX, titleY);
    	
    	// Draw blinking prompt text
    	if (showText) {
    		String prompt = "Press ENTER to Play";
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
        viewport.update(width, height, true); // without true, there is a bug
        /*
         * Important Note: Don't forget the 'true' parameter in viewport.update(width, height, true)!
         * 
         * This third parameter — `centerCamera` — controls whether the camera is automatically centered
         * when the viewport is updated (typically on screen resize). It's essential for ensuring that
         * the visual content appears correctly aligned on the screen.
         * 
         * Without 'true' (default is false):
         * - The camera stays in its current position after resizing.
         * - If the aspect ratio changes, the visible area may shift, showing the wrong part of your world.
         * - Backgrounds and UI elements might appear offset, since the camera is no longer centered.
         * 
         * With 'true':
         * - The camera is automatically centered in the world after resizing.
         * - Ensures the world coordinates (0,0) to (worldWidth, worldHeight) map correctly to the screen.
         * - Backgrounds drawn from (0,0) with size (worldWidth, worldHeight) will properly fill the screen.
         * 
         * In this specific case:
         * - The background is drawn starting at (0,0).
         * - UI/text elements are positioned relative to worldWidth and worldHeight.
         * - Therefore, centering the camera ensures that these coordinates align with what's actually visible.
         * 
         * This is especially important for menus and UI screens where consistent layout is expected.
         * For gameplay screens, you might skip centering (use false) if you have a custom camera position
         * you want to preserve during resizing.
         * 
         * This behavior isn't always obvious from LibGDX documentation, and it's a common source of bugs.
         * In most cases, 'centerCamera' should probably default to true to avoid confusion.
         */
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
        if (menuBackgroundTexture != null) menuBackgroundTexture.dispose();
    }
}