package com.neet.managers;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class Jukebox {

	//tipo de array que posee una key para ingresar al dato, no posee orden como tal
	private static HashMap<String, Sound> sounds;
	
	//nota:
	// los sonidos en mp3 a la hora de hacer loop no son tan buenos, ya que 
	// al reiniciar el reproductor se escuchara un tiempo sin sonido, encambio el
	// wav y el ogg no poseen este tipo de inconvenientes
	
	static {
		sounds = new HashMap<String, Sound>();
	}
	
	public static void load(String path, String name) {
		Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
		
		sounds.put(name, sound);
	}
	
	public static void play(String name) {
		sounds.get(name).play();
	}
	
	//repetir un sonido
	public static void loop(String name) {
		sounds.get(name).loop();
	}
	
	public static void stop(String name) {
		sounds.get(name).stop();
	}
	
	public static void stopAll() {
		for(Sound s : sounds.values()) {
			s.stop();
		}
	}
}
