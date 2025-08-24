package com.lobsterchops.accorncollector.util;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Disposable;

/*
 * asset management
 * - queue assets
 * - update loading calls
 * - getting assets from assets/
 * - disposing
 */

public class GameAssetManager implements Disposable {
	
	private final AssetManager assetManager;
	
	public static class Assets {
		/* Example
		 * 	public static final String PLAYER_TEXTURE = "sprites/player.png";
        	public static final String BACKGROUND = "sprites/background.png";
        	
        	public static final String JUMP_SOUND = "audio/jump.wav";
        	public static final String BACKGROUND_MUSIC = "audio/background.ogg";
        	
        	public static final String UI_FONT = "fonts/ui.fnt";
        	public static final String GAME_FONT_TTF = "fonts/game.ttf";
        	
        	
        	
		 * 
		 */
	}
	
	public GameAssetManager() {
		assetManager = new AssetManager();
		
		
	}
	

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	

}
