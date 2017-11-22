package com.neet.gamestates;

import com.asteroids.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.neet.managers.GameKeys;
import com.neet.managers.GameStateManager;
import com.neet.managers.Save;

public class HighScoreState extends GameState {
	
	private SpriteBatch sb;
	
	private BitmapFont font;
	
	private long[] highScores;
	private String[] names;

	public HighScoreState(GameStateManager gsm) {
		super(gsm);
		
	}

	public void init() {
		
		sb = new SpriteBatch();
		
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"));
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		
		param.size = 20;
		
		font = gen.generateFont(param);
		
		highScores = Save.gd.getHighScores();
		
		//test();
		
		names = Save.gd.getNames();
	}
	
	public void test() {
		for(int i = 0; i < Save.gd.getHighScores().length; i++) {
			System.out.println(Save.gd.getHighScores()[i]);
		}
	}

	public void update(float dt) {
		handleInput();
		
	}

	public void draw() {
		
		sb.setProjectionMatrix(Game.cam.combined);
		
		GlyphLayout gt = new GlyphLayout();
		
		sb.begin();
		
		String s;
		
		s = "High Scores";
		
		gt.setText(font, s);
		
		font.draw(sb, s, (Game.WIDTH - gt.width) / 2, 300);
		
		for(int i = 0; i < highScores.length; i++) {
			s = String.format("%2d. %7s %s", i + 1, highScores[i], names[i]);
		
			gt.setText(font, s);
			
			font.draw(sb, s, (Game.WIDTH - gt.width) / 2, 270 - 20 * i);
			
			
		}
		
		sb.end();
		
	}

	public void handleInput() {
		if(GameKeys.isPressed(GameKeys.ENTER) || GameKeys.isPressed(GameKeys.ESCAPE)) {
			gsm.setState(GameStateManager.MENU);
		}
		
	}

	
	public void dispose() {
		sb.dispose();
		font.dispose();
		
	}

}
