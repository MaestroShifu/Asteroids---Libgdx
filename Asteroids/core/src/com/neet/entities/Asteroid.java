package com.neet.entities;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Asteroid extends SpaceObjects{
private int type;
	
	//CANTIDAD E PEDASOS QUE SE FRAGMENTA EL ASTEROIDE
	public static final int SMALL = 0;
	public static final int MEDIUM = 1;
	public static final int LARGE = 2;
	
	//para la generacion aletoria de la forma de los asteroides
	private int numPoints;
	private float[] dists;
	
	private int score;
	
	private boolean remove;
	
	public Asteroid(float x, float y, int type) {
		
		this.x = x;
		this.y = y;
		this.type =type;
		
		if(type == SMALL) {
			numPoints = 8;
			width = height = 12;
			speed = MathUtils.random(70, 100);
			score = 100;
		}else if(type == MEDIUM) {
			numPoints = 10;
			width = height = 20;
			speed = MathUtils.random(50, 60);
			score = 50;
		}else if(type == LARGE) {
			numPoints = 12;
			width = height = 40;
			speed = MathUtils.random(20, 30);
			score = 20;
		}
		
		rotationSpeed = MathUtils.random(-1, 1);
		
		//determina direccion
		radians = MathUtils.random(2 * 3.1415f);
		dx = MathUtils.cos(radians) * speed;
		dy = MathUtils.sin(radians) * speed;
		
		shapeX = new float[numPoints];
		shapeY = new float [numPoints];
		dists = new float [numPoints];
		
		int radius = width / 2;
		
		//distancias para los puntos
		for(int i = 0; i < numPoints; i++) {
			dists[i] = MathUtils.random(radius / 2, radius);
		}
		
		setShape();
	}
	
	private void setShape() {
		float angle = 0;
		
		for(int i = 0; i < numPoints; i++) {
			shapeX[i] = x + MathUtils.cos(angle + radians) * dists[i]; 
			shapeY[i] = y + MathUtils.sin(angle + radians) * dists[i];
			
			angle += 2 * 3.1415f / numPoints;
		}
	}
	
	public int getType() {
		return type;
	}
	
	public int getScore() {
		return score;
	}
	
	public boolean shouldRemove() {
		return remove;
	}
	
	public void update(float dt) {
		
		x += dx * dt;
		y += dy * dt;
		
		radians+= rotationSpeed * dt;
		setShape();
		
		wrap();
	}
	
	public void draw(ShapeRenderer sr) {
		sr.setColor(1, 1, 1, 1);
		sr.begin(ShapeType.Line);
		
		for(int i = 0, j = shapeX.length -1; i < shapeX.length; j = i++) {
			
			sr.line(shapeX[i], shapeY[i], shapeX[j], shapeY[j]);
			
		}
		
		sr.end();
	}
}
