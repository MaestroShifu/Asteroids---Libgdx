package com.asteroids.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.neet.managers.GameInputProcessor;
import com.neet.managers.GameKeys;
import com.neet.managers.GameStateManager;
import com.neet.managers.Jukebox;

public class Game extends ApplicationAdapter {
	public static int WIDTH;
	public static int HEIGHT;
	
	//demos notar la diferencia de tamaño independientemente de la distancia entre los objetos
	public static OrthographicCamera cam;//solo se usa en juegos donde se se mostrara una parte dell ecenario

	private GameStateManager gsm;
	
	//inicializa el juego
	public void create() {
		
		//llama al tamaño que se le agrego en ejecutar desktop
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
	
		//tamaño de loq ue se quiere visualizar
		cam = new OrthographicCamera(WIDTH, HEIGHT);
		//mover el foco de la camra para dejarlo en coordenadads 0, 0 en el plano
		cam.translate(WIDTH / 2, HEIGHT / 2);
		//recalcula la proyeccion de la camara
		cam.update();
		
		//cargar sonidos
		Jukebox.load("sounds/explode.ogg", "explode");
		Jukebox.load("sounds/extralife.ogg", "extralife");
		Jukebox.load("sounds/largesaucer.ogg", "largesaucer");
		Jukebox.load("sounds/pulsehigh.ogg", "pulsehigh");
		Jukebox.load("sounds/pulselow.ogg", "pulselow");
		Jukebox.load("sounds/saucershoot.ogg", "saucershoot");
		Jukebox.load("sounds/shoot.ogg", "shoot");
		Jukebox.load("sounds/smallsaucer.ogg", "smallsaucer");
		Jukebox.load("sounds/thruster.ogg", "thruster");
		
		//resive los eventos de entrada
		Gdx.input.setInputProcessor(new GameInputProcessor());
		
		//inicializamos la maquina de estados
		gsm = new GameStateManager();
	}

	//es para dimensionar el juego
	public void resize(int width, int height) {
		
	}

	//es donde imprime en pantalla graficos y logica
	public void render() {
		//usamos el openGL
		//limpiar pantalla en negro
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		gsm.update(Gdx.graphics.getDeltaTime());//me trae como valor los frames que se estan ejecutando
		gsm.draw();
		
		//refresco del vector de boleanos
		GameKeys.update();

	}

	//es cuando el juego no esta abierto o no se peude ver
	public void pause() {
		
	}

	//vuleve a dar focus y reinicia despues del pausa
	public void resume() {
		
	}

	//para limpiar memoria y datos del juego al cerrarce
	public void dispose() {
		
	}
}
