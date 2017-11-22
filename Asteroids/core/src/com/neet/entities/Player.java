package com.neet.entities;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.asteroids.game.Game;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.neet.managers.Jukebox;

public class Player extends SpaceObjects{
	private final int MAX_BULLETS = 4;
	private ArrayList<Bullet> bullets;
	
	private float flameX[];
	private float flameY[];
	
	//para las teclas
	private boolean left;
	private boolean right;
	private boolean up;
	
	//para cuestiones de velocidad
	private float maxSpeed;
	private float acceleration;
	private float deceleration;
	private float acceleratingTimer;
	
	private boolean hit;
	private boolean dead;
	
	private float hitTimer;
	private float hitTime;
	
	private Line2D.Float[] hitLines;//Requiere 2 puntos para graficar ineas
	private Point2D.Float[] hitLinesVector;//equivale a una coordenada en el plano
	
	//mecanicas de premiacion del juego
	private int score;
	private int extraLives; 
	private long requiredScore;
	
	public Player(ArrayList<Bullet> Bullets) {
		//inicializamos posicion
		this.bullets = Bullets;
		
		x = Game.WIDTH / 2;
		y = Game.HEIGHT / 2;
		
		maxSpeed = 300;
		acceleration = 200;
		deceleration = 10;
		
		shapeX = new float[4];
		shapeY = new float[4];
		flameX = new float[3];
		flameY = new float[3];
		
		radians = 3.1415f / 2;
		rotationSpeed = 3;
		
		hit = false;
		hitTimer = 0;
		hitTime = 2;
		
		score = 0;
		extraLives = 3;
		requiredScore = 10000;
		
	}
	
	private void setShape() {
		shapeX[0] = x + MathUtils.cos(radians) * 8;
		shapeY[0] = y + MathUtils.sin(radians) * 8;
		
		shapeX[1] = x + MathUtils.cos(radians - 4 * 3.1415f / 5) * 8;
		shapeY[1] = y + MathUtils.sin(radians - 4 * 3.1415f / 5) * 8;
		
		shapeX[2] = x + MathUtils.cos(radians + 3.1415f) * 5;
		shapeY[2] = y + MathUtils.sin(radians + 3.1415f) * 5;
		
		shapeX[3] = x + MathUtils.cos(radians + 4 * 3.1415f / 5) * 8;
		shapeY[3] = y + MathUtils.sin(radians + 4 * 3.1415f / 5) * 8;
	}
	
	private void setFlame() {
		flameX[0] = x + MathUtils.cos(radians - 5 * 3.1415f / 6) * 5;
		flameY[0] = y + MathUtils.sin(radians - 5 * 3.1415f / 6) * 5; 
		
		flameX[1] = x + MathUtils.cos(radians - 3.1415f) * (6 + acceleratingTimer * 50);
		flameY[1] = y + MathUtils.sin(radians - 3.1415f) * (6 + acceleratingTimer * 50);
	
		flameX[2] = x + MathUtils.cos(radians + 5 * 3.1415f / 6) * 5;
		flameY[2] = y + MathUtils.sin(radians + 5 * 3.1415f / 6) * 5; 
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setUp(boolean b) {
		
		if(b && !up && !hit) {
			Jukebox.loop("thruster");
		}
		else if(!b) {
			Jukebox.stop("thruster");
		}
		
		up = b;
	}
	
	public boolean isHiT() {
		return hit;
	}
	
	public boolean isDead() {
		return dead;
	}
	
	public int getScore() {
		return score;
	}
	
	public int getLives() {
		return extraLives;
	}
	
	public void loseLife() {
		extraLives--;
	}
	
	public void incrementScore(int l) {
		score += l;
	}
	
	public void setPosition(float x, float y) {
		super.setPosition(x, y);
		setShape();
	}
	
	public void reset() {
		x = Game.WIDTH / 2;
		y = Game.HEIGHT / 2;
		setShape();
		hit = dead = false;
	}
	
	public void shoot() {
		if(bullets.size() == MAX_BULLETS) {
			return;
		}
		
		bullets.add(new Bullet(x, y, radians));
		Jukebox.play("shoot");
	}
	
	public void hit() {
		if(hit) {
			return;
		}
		
		//detener al jugador
		hit = true;
		dx = dy = 0;
		left = right = up = false;
		
		hitLines = new Line2D.Float[4];
		
		for(int i = 0, j = hitLines.length - 1; i < hitLines.length; j = i++) {
			hitLines[i] = new Line2D.Float(shapeX[i], shapeY[i], shapeX[j], shapeY[j]);
		}
		
		hitLinesVector = new Point2D.Float[4];
		
		hitLinesVector[0] = new Point2D.Float(MathUtils.cos(radians + 1.5f), MathUtils.sin(radians + 1.5f)); 
		hitLinesVector[1] = new Point2D.Float(MathUtils.cos(radians - 1.5f), MathUtils.sin(radians - 1.5f)); 
		hitLinesVector[2] = new Point2D.Float(MathUtils.cos(radians - 2.8f), MathUtils.sin(radians - 2.8f)); 
		hitLinesVector[3] = new Point2D.Float(MathUtils.cos(radians + 2.8f), MathUtils.sin(radians + 2.8f)); 
	}
	
	public void update(float dt) {
		
		//revisar hit
		if(hit) {
			hitTimer += dt;
			if(hitTimer > hitTime) {
				dead = true;
				hitTimer = 0;
			}
			
			for(int i = 0; i < hitLines.length; i++) {
				hitLines[i].setLine(hitLines[i].x1 + hitLinesVector[i].x * 10 * dt, 
						hitLines[i].y1 + hitLinesVector[i].y * 10 * dt,
						hitLines[i].x2 + hitLinesVector[i].x * 10 * dt,
						hitLines[i].y2 + hitLinesVector[i].y * 10 * dt);
			}
			return;
			
		}
		
		//extra lives
		if(score >= requiredScore) {
			extraLives++;
			requiredScore += 10000;
			Jukebox.play("extralife");
		}
		
		//movimiento
		if(left) {
			radians += rotationSpeed * dt;
		}else if(right) {
			radians -= rotationSpeed * dt;
		}
		
		//aceleracion
		if(up) {
			dx += MathUtils.cos(radians) * acceleration * dt;
			dy += MathUtils.sin(radians) * acceleration * dt;
			acceleratingTimer += dt;
			
			if(acceleratingTimer > 0.1f) {
				acceleratingTimer = 0;
			}
		}else {
			acceleratingTimer = 0;
		}
		
		//desaceleracion
		float vec = (float) Math.sqrt(dx * dx + dy * dy);
		if(vec > 0) {
			dx -= (dx / vec) * deceleration * dt;
			dy -= (dy / vec) * deceleration * dt;
		}
		if(vec > maxSpeed) {
			dx = (dx / vec) * maxSpeed;
			dy = (dy / vec) * maxSpeed;
		}
		
		//posicion
		x += dx * dt;
		y += dy * dt;
		
		//establecer el shape
		setShape();
		
		//estableces flame
		if(up) {
			setFlame();
		}
		
		//screen wrap
		wrap();
	}
	
	//ShapeRenderer sirve para dibujar figuras gometricas
	public void draw(ShapeRenderer sr) {
		
		sr.setColor(1, 1, 1, 1);
		
		//llama el metodo para dibujar
		sr.begin(ShapeType.Line);
		
		//pintar la explosion
		if(hit) {
			for(int i = 0; i < hitLines.length; i++) {
				sr.line(hitLines[i].x1, hitLines[i].y2, hitLines[i].x2, hitLines[i].y2);
			}
			sr.end();
			return;
		}
		
		for(int i = 0, j = shapeX.length -1; i < shapeX.length; j = i++) {
			
			sr.line(shapeX[i], shapeY[i], shapeX[j], shapeY[j]);
			
		}
		
		//pintar flames
		if(up) {
			for(int i = 0, j = flameX.length -1; i < flameX.length; j = i++) {
				
				sr.line(flameX[i], flameY[i], flameX[j], flameY[j]);
				
			}
		}
		
		sr.end();
	}
}
