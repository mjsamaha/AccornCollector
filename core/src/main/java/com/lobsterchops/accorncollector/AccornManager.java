package com.lobsterchops.accorncollector;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;


/**
* Manages spawning, moving and collisions for acorns.
*/
public class AccornManager {
	private final Texture texture;
	private final Sound collectSound;
	private final Array<Sprite> acorns = new Array<>();
	private float spawnTimer = 0f;


	public AccornManager(Texture texture, Sound collectSound) {
		this.texture = texture;
		this.collectSound = collectSound;
	}
	
	
	public void createAccorn(FitViewport viewport) {
		float acornWidth = 1f;
		float acornHeight = 1f;
		float worldWidth = viewport.getWorldWidth();
		float worldHeight = viewport.getWorldHeight();
	
	
		Sprite spr = new Sprite(texture);
		spr.setSize(acornWidth, acornHeight);
		spr.setX(MathUtils.random(0f, worldWidth - acornWidth));
		spr.setY(worldHeight);
		acorns.add(spr);
	}
	
	
	public void update(float delta, FitViewport viewport, Rectangle playerBounds) {
		// move existing acorns and handle collisions
		Rectangle acornRect = new Rectangle();
		for (int i = acorns.size - 1; i >= 0; i--) {
			Sprite a = acorns.get(i);
			a.translateY(-2f * delta);
			acornRect.set(a.getX(), a.getY(), a.getWidth(), a.getHeight());
		
		
			if (a.getY() < -a.getHeight()) {
				acorns.removeIndex(i);
			} 	else if (playerBounds.overlaps(acornRect)) {
					acorns.removeIndex(i);
				if (collectSound != null) 
					collectSound.play();
			}
		}	
	
	
		// spawn timer
		spawnTimer += delta;
		if (spawnTimer > 1f) {
			spawnTimer = 0f;
			createAccorn(viewport);
		}
	}
	
	
	public void draw(SpriteBatch batch) {
		for (Sprite s : acorns) s.draw(batch);
		}
	}
