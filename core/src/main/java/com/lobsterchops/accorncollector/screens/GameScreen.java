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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;

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
    Rectangle playerRectangle;
    
    Texture accornTexture;
    Rectangle accornRectangle;
    
    Sprite playerSprite;
    
    Array<Sprite> accornSprites;
   
    
    float accornTimer;
    
    Sound collectSound;    
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
    	
    	playerTexture = new Texture("player.png");
    	playerSprite = new Sprite(playerTexture);
    	playerSprite.setSize(1, 1);
    	playerRectangle = new Rectangle();
    	
    	accornTexture = new Texture("accorn.png"); // TODO: create pixel art for accorn
    	accornRectangle = new Rectangle();
    	
    	accornSprites = new Array<>();
    	
    	createAccornDroppings();
    	
    	collectSound = Gdx.audio.newSound(Gdx.files.internal("collect.wav"));
    	gameplayMusic = Gdx.audio.newMusic(Gdx.files.internal("Amber Forest.mp3"));
    	
    	gameplayMusic.setLooping(true);
    	gameplayMusic.setVolume(.5f);
    	gameplayMusic.play();

        // Game screen is now active
        // Initialize your game state here
    	
    	// add in assets --> name = new Texture("filename");
    	
    	// sound = Gdx.audio.newSound(Gdx.files.internal("file"));
    	// music = Gdx.audio.newMusic(Gdx.files.internal("file"));
    	
    }

    @Override
    public void render(float delta) {
        handleInput();
        playerInput();
        logic();
        draw();
    }
    
    private void createAccornDroppings() {
    	float accornWidth = 1;
    	float accornHeight = 1;
    	float worldWidth = viewport.getWorldWidth();
    	float worldHeight = viewport.getWorldHeight();
    	
    	Sprite accornSprite = new Sprite(accornTexture);
    	accornSprite.setSize(accornWidth, accornHeight);
    	accornSprite.setX(MathUtils.random(0f, worldWidth - accornWidth));
    	accornSprite.setY(worldHeight);
    	accornSprites.add(accornSprite);
    	
    }
    
    private void logic() {
    	float worldWidth = viewport.getWorldWidth();
    	float worldHeight = viewport.getWorldHeight();
    	
    	float playerWidth = playerSprite.getWidth();
    	float playerHeight = playerSprite.getHeight();
    	
    	playerSprite.setX(MathUtils.clamp(playerSprite.getX(), 0, worldWidth - playerWidth));
    	
    	float delta = Gdx.graphics.getDeltaTime();
    	playerRectangle.set(playerSprite.getX(), playerSprite.getY(), playerWidth, playerHeight);
    	
    	
    	// Loop through the sprites backwards to prevent out of bounds errors
        for (int i = accornSprites.size - 1; i >= 0; i--) {
            Sprite accornSprite = accornSprites.get(i); // Get the sprite from the list
            float accornWidth = accornSprite.getWidth();
            float accornHeight = accornSprite.getHeight();
            
            accornSprite.translateY(-2f * delta);
            accornRectangle.set(accornSprite.getX(), accornSprite.getY(), accornWidth, accornHeight);

            // if the top of the drop goes below the bottom of the view, remove it
            if (accornSprite.getY() < -accornHeight) accornSprites.removeIndex(i);
            else if (playerRectangle.overlaps(accornRectangle)) {
            	accornSprites.removeIndex(i);
            	collectSound.play();
            }
        }
        
    	
    	accornTimer += delta;
    	if (accornTimer > 1f) {
    		accornTimer = 0;
    		createAccornDroppings();
    	}
    }
    
    private void draw() {
    	ScreenUtils.clear(Color.BLACK);
    	viewport.apply();
    	batch.setProjectionMatrix(viewport.getCamera().combined);
    	
    	batch.begin();
    	
    	
    	float worldWidth = viewport.getWorldWidth();
    	float worldHeight = viewport.getWorldHeight();
    	
    	batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
    	
    	// batch.draw(playerTexture, 0, 0, 1, 1);
    	playerSprite.draw(batch);
    	
    	// draw ea accorn sprite
    	for (Sprite accornSprite : accornSprites) {
    		accornSprite.draw(batch);
    	}
    	
    	
    	
    	batch.end();
    }
    
    private void playerInput() {
    	float speed = 5f;
    	float delta = Gdx.graphics.getDeltaTime();
    	if (Gdx.input.isKeyPressed(Input.Keys.D)) {
    		playerSprite.translateX(speed * delta);
    	} else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
    		playerSprite.translateX(-speed * delta);
    	}
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