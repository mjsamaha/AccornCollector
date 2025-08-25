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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.lobsterchops.accorncollector.AccornManager;
import com.lobsterchops.accorncollector.Player;

public class GameScreen implements Screen {
	private ScreenManager screenManager;
	
	
	private FitViewport viewport;
	
	
	/* rendering */
	private SpriteBatch batch;
	private BitmapFont font;
	private GlyphLayout layout;
	
	
	/* assets */
	private Texture backgroundTexture;
	private Texture playerTexture;
	private Texture acornTexture; // note: original file name can remain "accorn.png"
	private Sound collectSound;
	private Music gameplayMusic;
	
	
	/* game objects */
	private Player player;
	private AccornManager accornManager;
	
	
	public GameScreen(ScreenManager screenManager) {
		this.screenManager = screenManager;
	}
	
	@Override
	public void show() {
		setupRendering();
		setupViewport();
		loadAssets();
		createGameObjects();
		setupAudio();
	}
	
	private void setupRendering() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		layout = new GlyphLayout();
		font.getData().setScale(1.5f);
		font.setColor(Color.WHITE);
	}
	
	private void setupViewport() {
		viewport = new FitViewport(8, 6);
	}
	
	private void loadAssets() {
		backgroundTexture = new Texture("background.png");
		playerTexture = new Texture("player.png");
		acornTexture = new Texture("accorn.png");


		collectSound = Gdx.audio.newSound(Gdx.files.internal("collect.wav"));
		gameplayMusic = Gdx.audio.newMusic(Gdx.files.internal("Amber Forest.mp3"));
	}
	
	private void createGameObjects() {
		// place player at bottom center
		float startX = viewport.getWorldWidth() / 2f - 0.5f;
		float startY = 0f;
		player = new Player(playerTexture, startX, startY);


		accornManager = new AccornManager(acornTexture, collectSound);
		// create the first acorn using the viewport so it can spawn at the top of the world
		accornManager.createAccorn(viewport);
	}
	
	private void setupAudio() {
		gameplayMusic.setLooping(true);
		gameplayMusic.setVolume(.5f);
		gameplayMusic.play();
	}
	
	@Override
	public void render(float delta) {
		handleInput();
		update(delta);
		draw();
	}
	
	private void handleInput() {
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			screenManager.setScreen(new MainMenuScreen(screenManager), ScreenManager.TransitionType.FADE, 0.5f);
		}
	}
	
	private void update(float delta) {
		// update player (handles its own input + bounds)
		player.update(delta, viewport);


		// update acorns and check collisions
		accornManager.update(delta, viewport, player.getBounds());
	}
	
	private void draw() {
		ScreenUtils.clear(Color.BLACK);
		viewport.apply();
		batch.setProjectionMatrix(viewport.getCamera().combined);


		batch.begin();


		float worldWidth = viewport.getWorldWidth();
		float worldHeight = viewport.getWorldHeight();


		batch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);


		player.draw(batch);
		accornManager.draw(batch);


		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}
	
	@Override
	public void pause() {
		
	}


	@Override
	public void resume() {
		
	}


	@Override
	public void hide() {
		
	}
	
	@Override
	public void dispose() {
		// dispose everything this screen created
		if (batch != null) batch.dispose();
		if (font != null) font.dispose();
	
	
		if (backgroundTexture != null) backgroundTexture.dispose();
		if (playerTexture != null) playerTexture.dispose();
		if (acornTexture != null) acornTexture.dispose();
	
	
		if (collectSound != null) collectSound.dispose();
		if (gameplayMusic != null) gameplayMusic.dispose();

	// player and acornManager do not own textures/sounds so no double-dispose
	}
}
	