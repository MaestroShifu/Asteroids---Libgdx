package com.neet.entities;

import com.asteroids.game.Game;

public class SpaceObjects {
	//posicion
	protected float x;
	protected float y;
	
	//vector
	protected float dx;
	protected float dy;
	
	//angulo de orientacion
	protected float radians;
	protected float speed;
	protected float rotationSpeed;
	
	//tamaño del poligono
	protected int width;
	protected int height;
	
	//darle forma al poligono
	protected float[] shapeX;
	protected float[] shapeY;
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float[] getShapeX() {
		return shapeX;	
	}
	
	public float[] getShapeY() {
		return shapeY;
	}
	

	public boolean intersects(SpaceObjects other) {
		float[] sx = other.getShapeX();
		float[] sy = other.getShapeY();
		
		for(int i = 0; i < sx.length; i++) {
			if(contains(sx[i], sy[i])) {
				return true;
			}
		}
		return false;
	}
	
	public boolean contains(float x, float y) {
		boolean b = false;
		
		//recgla par - impar 
		for(int i = 0, j = shapeX.length - 1; i < shapeX.length; j = i++) {
			if((shapeY[i] > y) != (shapeY[j] > y) && (x < (shapeX[j] - shapeX[i]) * (y - shapeY[i]) / (shapeY[j] - shapeY[i]) + shapeX[i])) {
				b = !b;
			}
		}
		return b;
	}
	
	protected void wrap() {
		if(x < 0) {
			x = Game.WIDTH;
		}
		if(x > Game.WIDTH) {
			x = 0;
		}
		if(y < 0) {
			y = Game.HEIGHT;
		}
		if(y > Game.HEIGHT) {
			y = 0;
		}
	}
}
