package com.lobsterchops.accorncollector.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;

public class ScreenManager {
    
    public enum TransitionType {
        FADE,
        SLIDE_LEFT,
        SLIDE_RIGHT,
        SLIDE_UP,
        SLIDE_DOWN
    }
    
    private Game game;
    private Screen currentScreen;
    private Screen nextScreen;
    private TransitionScreen transitionScreen;
    
    public ScreenManager(Game game) {
        this.game = game;
    }
    
    public void setScreen(Screen screen) {
        setScreen(screen, TransitionType.FADE, 0.5f);
    }
    
    public void setScreen(Screen screen, TransitionType transitionType, float duration) {
        if (currentScreen == null) {
            // First screen, no transition needed
            currentScreen = screen;
            game.setScreen(screen);
            return;
        }
        
        nextScreen = screen;
        transitionScreen = new TransitionScreen(currentScreen, nextScreen, transitionType, duration);
        game.setScreen(transitionScreen);
    }
    
    public Screen getCurrentScreen() {
        return currentScreen;
    }
    
    private class TransitionScreen implements Screen {
        private Screen fromScreen;
        private Screen toScreen;
        private TransitionType transitionType;
        private float duration;
        private float elapsed;
        private SpriteBatch batch;
        private ShapeRenderer shapeRenderer;
        private boolean isComplete;
        
        public TransitionScreen(Screen fromScreen, Screen toScreen, TransitionType transitionType, float duration) {
            this.fromScreen = fromScreen;
            this.toScreen = toScreen;
            this.transitionType = transitionType;
            this.duration = duration;
            this.elapsed = 0f;
            this.batch = new SpriteBatch();
            this.shapeRenderer = new ShapeRenderer();
            this.isComplete = false;
            
            // Initialize the new screen
            toScreen.show();
        }
        
        @Override
        public void show() {
            // Already handled in constructor
        }
        
        @Override
        public void render(float delta) {
            elapsed += delta;
            float progress = Math.min(elapsed / duration, 1f);
            float interpolatedProgress = Interpolation.smooth.apply(progress);
            
            // Clear screen
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            
            switch (transitionType) {
                case FADE:
                    renderFadeTransition(interpolatedProgress);
                    break;
                case SLIDE_LEFT:
                    renderSlideTransition(interpolatedProgress, -1, 0);
                    break;
                case SLIDE_RIGHT:
                    renderSlideTransition(interpolatedProgress, 1, 0);
                    break;
                case SLIDE_UP:
                    renderSlideTransition(interpolatedProgress, 0, 1);
                    break;
                case SLIDE_DOWN:
                    renderSlideTransition(interpolatedProgress, 0, -1);
                    break;
            }
            
            if (progress >= 1f && !isComplete) {
                completeTransition();
            }
        }
        
        private void renderFadeTransition(float progress) {
            // Render old screen with decreasing alpha
            if (progress < 1f) {
                batch.setColor(1, 1, 1, 1 - progress);
                batch.begin();
                fromScreen.render(0); // Don't update, just render
                batch.end();
            }
            
            // Render new screen with increasing alpha
            batch.setColor(1, 1, 1, progress);
            batch.begin();
            toScreen.render(Gdx.graphics.getDeltaTime());
            batch.end();
            
            // Reset batch color
            batch.setColor(Color.WHITE);
        }
        
        private void renderSlideTransition(float progress, int dirX, int dirY) {
            int screenWidth = Gdx.graphics.getWidth();
            int screenHeight = Gdx.graphics.getHeight();
            
            // Calculate offsets
            float offsetX = dirX * screenWidth * progress;
            float offsetY = dirY * screenHeight * progress;
            
            // Render old screen moving out
            batch.getProjectionMatrix().setToOrtho2D(-offsetX, -offsetY, screenWidth, screenHeight);
            batch.begin();
            fromScreen.render(0);
            batch.end();
            
            // Render new screen moving in
            batch.getProjectionMatrix().setToOrtho2D(
                dirX * screenWidth - offsetX, 
                dirY * screenHeight - offsetY, 
                screenWidth, 
                screenHeight
            );
            batch.begin();
            toScreen.render(Gdx.graphics.getDeltaTime());
            batch.end();
            
            // Reset projection matrix
            batch.getProjectionMatrix().setToOrtho2D(0, 0, screenWidth, screenHeight);
        }
        
        private void completeTransition() {
            isComplete = true;
            currentScreen = toScreen;
            fromScreen.hide();
            game.setScreen(toScreen);
        }
        
        @Override
        public void resize(int width, int height) {
            fromScreen.resize(width, height);
            toScreen.resize(width, height);
        }
        
        @Override
        public void pause() {
            fromScreen.pause();
            toScreen.pause();
        }
        
        @Override
        public void resume() {
            fromScreen.resume();
            toScreen.resume();
        }
        
        @Override
        public void hide() {
            // Handled in completeTransition
        }
        
        @Override
        public void dispose() {
            if (batch != null) batch.dispose();
            if (shapeRenderer != null) shapeRenderer.dispose();
        }
    }
}