package com.neet.entities;

import java.util.ArrayList;

import com.asteroids.game.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.neet.managers.Jukebox;

public class FlyingSaucer extends SpaceObjects{
	
	private ArrayList<Bullet>bullets;
	
	private int type;
	public static final int LARGE = 0;
	public static final int SMALL = 1;
	
	private int score;
	
	private float fireTimer;
	private float fireTime;
	
	private Player player;
	
	private float pathTimer;
	private float pathTime1;
	private float pathTime2;
	
	private int direction;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
	private boolean remove;
	
	public FlyingSaucer(int type, int direction, Player player, ArrayList<Bullet> bullets) {
		this.direction = direction;
		this.type = type;
		this.player = player;
		this.bullets = bullets;
		
		speed = 70;
		
		if(direction == LEFT) {
			dx = -speed;
			x = Game.WIDTH;
		}
		
		if(direction == RIGHT) {
			dx = speed;
			x = 0;
		}
		
		y = MathUtils.random(Game.HEIGHT);
		
		shapeX = new float[6];
		shapeY = new float[6];
		setShape();
		
		if(type == LARGE) {
			score = 200;
			Jukebox.loop("largesaucer");
		}else if(type == SMALL) {
			score = 1000;
			Jukebox.loop("smallsaucer");
		}
		
		fireTimer = 0;
		fireTime = 1;
		
		pathTimer = 0;
		pathTime1 = 2;
		pathTime2 = pathTime1 + 2;
		
	}
	
	private void setShape() {
		if(type == LARGE) {
			
			shapeX[0] = x - 10;
			shapeY[0] = y;
			
			shapeX[1] = x - 3;
			shapeY[1] = y - 5;
			
			shapeX[2] = x + 3;
			shapeY[2] = y - 5;
			
			shapeX[3] = x + 10;
			shapeY[3] = y;
			
			shapeX[4] = x + 3;
			shapeY[4] = y + 5;
			
			shapeX[5] = x - 3;
			shapeY[5] = y + 5;
			
		}else if (type == SMALL) {
			
			shapeX[0] = x - 6;
			shapeY[0] = y;
			
			shapeX[1] = x - 2;
			shapeY[1] = y - 3;
			
			shapeX[2] = x + 2;
			shapeY[2] = y - 3;
			
			shapeX[3] = x + 6;
			shapeY[3] = y;
			
			shapeX[4] = x + 2;
			shapeY[4] = y + 3;
			
			shapeX[5] = x - 2;
			shapeY[5] = y + 3;
			
		}
	}
	
	
	public int getScore() {
		return score;
	}
	
	public boolean shouldRemove() {
		return remove;
	}
	
	public void update(float dt) {
		
		//fire
		if(!player.isHiT()) {
			fireTimer += dt;
			
			if(fireTimer > fireTime) {
				fireTimer = 0;
				
				if(type == LARGE) {
					radians = MathUtils.random(2 * 3.1415f);
					
				}else if(type == SMALL) {
					radians = MathUtils.atan2(player.getY() - y, player.getX() - x);
				
				}
				
				bullets.add(new Bullet(x, y, radians));
				Jukebox.play("saucershoot");
			}
		}
		
		//move 
		pathTimer += dt;
		
		if(pathTimer < pathTime1) {
			dy = 0;
		}
		
		if(pathTimer > pathTime1 && pathTimer < pathTime2) {
			dy = -speed;
		}
		
		if(pathTimer > pathTime1 + pathTime2) {
			dy = 0;
		}
		
		x += dx * dt;
		y += dy * dt;
		
		//wrap
		if(y < 0) {
			y = Game.HEIGHT;
		}
		
		//setshape
		setShape();
		
		//chech remove
		if((direction == RIGHT && x > Game.WIDTH) || (direction == LEFT && x < 0)) {
			remove = true;
		}
		
	}
	
	public void draw(ShapeRenderer sr) {
		
		sr.setColor(1, 1, 1, 1);
		
		sr.begin(ShapeType.Line);
		
		for(int i = 0, j = shapeX.length - 1; i < shapeX.length; j = i++) {
			
			sr.line(shapeX[i], shapeY[i], shapeX[j], shapeY[j]);
		}
		
		sr.line(shapeX[0], shapeY[0], shapeX[3], shapeY[3]);
		
		sr.end();
	}
	
}
