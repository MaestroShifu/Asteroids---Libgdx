package com.neet.gamestates;

import com.asteroids.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.neet.managers.GameKeys;
import com.neet.managers.GameStateManager;
import com.neet.managers.Save;

public class GameOverState extends GameState{
	
	private SpriteBatch sb;
	private ShapeRenderer sr;
	
	private boolean newHighScore;
	private char[] newName;
	private int currentChar;
	
	private BitmapFont gameOverFont;
	private BitmapFont font;
	
	public GameOverState(GameStateManager gsm) {
		super(gsm);
		
	}

	public void init() {
		
		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		
		newHighScore = Save.gd.isHighScore(Save.gd.getTentativeScore());
		
		if(newHighScore) {
			newName = new char[] {'A','A','A'};
			currentChar = 0;
		}
		
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"));
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		
		param.size = 32;
		
		gameOverFont = gen.generateFont(param);
		
		param.size = 20;
		font = gen.generateFont(param);
	}

	public void update(float dt) {
		handleInput();
		
	}

	public void draw() {
		sb.setProjectionMatrix(Game.cam.combined);
		//sr.setProjectionMatrix(Game.cam.combined);
		
		sb.begin();
		
		String s;
		
		s = "Game Over";
		
		GlyphLayout gt = new GlyphLayout();
		gt.setText(gameOverFont, s);
		
		gameOverFont.draw(sb, s, (Game.WIDTH - gt.width) / 2, 220);
		
		if(!newHighScore) {
			sb.end();
			
			return;
		}
		
		s = "New High Score: " + Save.gd.getTentativeScore();
		gt.setText(font, s);
		
		font.draw(sb, s, (Game.WIDTH - gt.width) / 2, 180);	
		
		for(int i = 0; i < newName.length; i++) {
			font.draw(sb, Character.toString(newName[i]), 230 + 14 * i, 120);
		}
		
		sb.end();
		
		sr.begin(ShapeType.Line);
		
		sr.line(230 + 14 * currentChar, 100, 244 + 14 * currentChar, 100);
		
		sr.end();
		
	}

	public void handleInput() {
		if(GameKeys.isPressed(GameKeys.ENTER)) {
			if(newHighScore) {
				Save.gd.addHighScore(Save.gd.getTentativeScore(), new String(newName));
				
				Save.save();
			}
			gsm.setState(GameStateManager.MENU);
		}
		
		if(GameKeys.isPressed(GameKeys.UP)) {
			if(newName[currentChar] == ' ') {
				newName[currentChar] = 'Z';
			}else {
				newName[currentChar]--;
				
				if(newName[currentChar] < 'A') {
					newName[currentChar] = ' ';
				}
			}
		}
		
		if(GameKeys.isPressed(GameKeys.DOWN)){
			if(newName[currentChar] == ' ') {
				newName[currentChar] = 'A';
			}else {
				newName[currentChar]++;
				
				if(newName[currentChar] > 'Z') {
					newName[currentChar] = ' ';
				}
			}
		}
		
		if(GameKeys.isPressed(GameKeys.RIGHT)) {
			if (currentChar < newName.length - 1) {
				currentChar++;
			}
		}
		
		if(GameKeys.isPressed(GameKeys.LEFT)) {
			if (currentChar > 0) {
				currentChar--;
			}
		}
		//min 15
	}

	public void dispose() {
		sb.dispose();
		sr.dispose();
		gameOverFont.dispose();
		font.dispose();
		
	}

}
