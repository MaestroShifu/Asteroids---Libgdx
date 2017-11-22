package com.neet.gamestates;

import com.neet.managers.GameStateManager;

public abstract class GameState {
	//el gestor de maquina de estaos es el unico que tiene el control para el cambio de estados de la maquina
	protected GameStateManager gsm;
	
	protected GameState (GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	//es la primera ves que se actuaiza el juego
	public abstract void init();
	//se estaran llamando para el manejo del estado del juego
	public abstract void update(float dt);
	public abstract void draw();
	//manejo de entradas
	public abstract void handleInput();
	//liberar memoria
	public abstract void dispose();
}
