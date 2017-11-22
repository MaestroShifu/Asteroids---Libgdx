package com.asteroids.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.asteroids.game.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		//Se pueda ejecutar en pc
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		
		//cponer titulo
		cfg.title = "Asteroids";
		//dimensiones del canvas
		cfg.width = 500;
		cfg.height = 400;
		//para el uso del opengl 3.0
		cfg.useGL30 = false;
		//para no cambiar dimesiones de la pestaña
		cfg.resizable = false;
		
		new LwjglApplication(new Game(), cfg);//con esto se inicialisa para ejecutar en pc
	}
}
