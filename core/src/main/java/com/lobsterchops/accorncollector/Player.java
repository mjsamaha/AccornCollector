package com.lobsterchops.accorncollector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;


/**
* Encapsulates the player sprite, input handling and bounds.
*/
public class Player {
	private final Sprite sprite;
	private final Rectangle bounds = new Rectangle();
	private float speed = 5f;
	
	
	public Player(Texture texture, float startX, float startY) {
		sprite = new Sprite(texture);
		sprite.setSize(1f, 1f);
		sprite.setPosition(startX, startY);
	}
	
	
	public void update(float delta, FitViewport viewport) {
		handleLocalInput(delta);
		clampToWorld(viewport);
		bounds.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
	}
	
	
	private void handleLocalInput(float delta) {
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			sprite.translateX(speed * delta);
		} else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			sprite.translateX(-speed * delta);
		}
	}
	
	
	private void clampToWorld(FitViewport viewport) {
		float worldWidth = viewport.getWorldWidth();
		float playerWidth = sprite.getWidth();
		sprite.setX(MathUtils.clamp(sprite.getX(), 0f, worldWidth - playerWidth));
	}
	
	
	public void draw(SpriteBatch batch) {
		sprite.draw(batch);
	}
	
	
	public Rectangle getBounds() {
		return bounds;
	}
}
