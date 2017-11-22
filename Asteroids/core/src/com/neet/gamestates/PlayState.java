package com.neet.gamestates;

import java.util.ArrayList;

import com.asteroids.game.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.neet.entities.Asteroid;
import com.neet.entities.Bullet;
import com.neet.entities.FlyingSaucer;
import com.neet.entities.Particle;
import com.neet.entities.Player;
import com.neet.managers.GameKeys;
import com.neet.managers.GameStateManager;
import com.neet.managers.Jukebox;
import com.neet.managers.Save;

public class PlayState extends GameState{
	private SpriteBatch sb;
	private ShapeRenderer sr;
	
	//se usa para imprimir texto en pantalla por mapa de bits
	private BitmapFont font;
	private Player hudPlayer;
	
	private Player player;
	private ArrayList<Bullet> bullets;
	private ArrayList<Asteroid>asteroids;
	private ArrayList<Bullet> enemyBullets;
	
	private FlyingSaucer flyingSaucer;
	private float fsTimer;
	private float fsTime;
	
	private ArrayList<Particle> particles;
	
	private int level;
	private int totalAsteroids;
	private int numAsteroidsLeft;
	
	private float maxDelay;
	private float minDelay;
	private float currentDelay;
	private float bgTimer;
	private boolean playLowPulse;

	public PlayState(GameStateManager gsm) {
		super(gsm);
	}

	public void init() {
		
		sb = new SpriteBatch();//mapa de bits
		sr = new ShapeRenderer();
		
		//envio el font que se usara
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Hyperspace Bold.ttf"));
		FreeTypeFontParameter param = new FreeTypeFontParameter();//se le manda parametros
		
		param.size = 20;//tamaño de la letra
		
		font = gen.generateFont(param);//se le asigna valores a bit de letras
		
		
		bullets = new ArrayList<Bullet>();
		
		player = new Player(bullets);
		
		asteroids = new ArrayList<Asteroid>();
		
		particles = new ArrayList<Particle>();
		
		level = 1;
		spawnAsteroids();
		
		hudPlayer = new Player(null);
		
		//timer nave ovni
		fsTimer = 0;
		fsTime = 15;
		enemyBullets = new ArrayList<Bullet>();
		
		maxDelay = 1;
		minDelay = 0.25f;
		currentDelay = maxDelay;
		bgTimer = maxDelay;
		playLowPulse = true;
		
		
	}
	
	private void createParticles(float x, float y) {
		for(int i = 0; i < 6; i++) {
			particles.add(new Particle(x, y));
		}
	}
	
	private void splitAsteroids(Asteroid a) {
		createParticles(a.getX(), a.getY());
		numAsteroidsLeft--;
		//sonido de fondo mas rapido
		currentDelay = ((maxDelay - minDelay) * numAsteroidsLeft / totalAsteroids) + minDelay;
		
		if(a.getType() == Asteroid.LARGE) {
			asteroids.add(new Asteroid(a.getX(), a.getY(), Asteroid.MEDIUM));
			asteroids.add(new Asteroid(a.getX(), a.getY(), Asteroid.MEDIUM));
		}
		if(a.getType() == Asteroid.MEDIUM) {
			asteroids.add(new Asteroid(a.getX(), a.getY(), Asteroid.SMALL));
			asteroids.add(new Asteroid(a.getX(), a.getY(), Asteroid.SMALL));	
		}
	}
	
	private void spawnAsteroids() {
		asteroids.clear();
		
		int numToSpawn = 4 + level - 1;
		totalAsteroids = numToSpawn * 7;
		numAsteroidsLeft = totalAsteroids;
		currentDelay = maxDelay;
		
		for(int i = 0 ; i < numToSpawn; i++) {
			float x = MathUtils.random(Game.WIDTH);
			float y = MathUtils.random(Game.HEIGHT);
			
			float dx = x - player.getX();
			float dy = y - player.getY();
			float dist = (float) Math.sqrt(dx * dx + dy * dy);
			
			//no salir tan cerca del jugador
			while(dist < 100) {
				x = MathUtils.random(Game.WIDTH);
				y = MathUtils.random(Game.HEIGHT);
				
				dx = x - player.getX();
				dy = y - player.getY();
				dist = (float) Math.sqrt(dx * dx + dy * dy);
			}
			
			asteroids.add(new Asteroid(x, y, Asteroid.LARGE));
		}
	}

	public void update(float dt) {
		//obtener input del usuario
		handleInput();
		
		//siguiente nivel
		if(asteroids.size() == 0) {
			level++;
			spawnAsteroids();
		}
		
		//carga el jugador
		player.update(dt);
		
		if(player.isDead()) {
			
			if(player.getLives() == 0) {
				Jukebox.stopAll();
				
				Save.gd.setTentativeScore(player.getScore());
				gsm.setState(GameStateManager.GAMEOVER);
				
				return;
			}
			
			player.reset();
			player.loseLife();
			
			flyingSaucer = null;
			Jukebox.stop("smallsaucer");
			Jukebox.stop("largesaucer");
			return;
		}
		
		//carga balas del jugador
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update(dt);
			if(bullets.get(i).shouldRemove()) {
				bullets.remove(i);
				i--;
			}
		}
		
		//update onvi
		//cuando no existe
		if(flyingSaucer == null) {
			fsTimer += dt; 
			
			if(fsTimer >= fsTime) {
				fsTimer = 0;
				//fuerzo alguno de ls 2 valores
				int type = MathUtils.random() < 0.5 ? FlyingSaucer.SMALL : FlyingSaucer.LARGE;
				
				int direction = MathUtils.random() < 0.5 ? FlyingSaucer.RIGHT : FlyingSaucer.LEFT;
			
				flyingSaucer = new FlyingSaucer(type, direction, player, enemyBullets);
			}
		}else {
			flyingSaucer.update(dt);
			
			if(flyingSaucer.shouldRemove()) {
				flyingSaucer = null;
				Jukebox.stop("smallsaucer");
				Jukebox.stop("largesaucer");
				
			}
		}
		
		//update  fs bullets
		for(int i = 0; i < enemyBullets.size(); i++) {
			enemyBullets.get(i).update(dt);
			if(enemyBullets.get(i).shouldRemove()) {
				enemyBullets.remove(i);
				i--;
			}
		}
		
		//cargar asteroides
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).update(dt);
			if(asteroids.get(i).shouldRemove()) {
				asteroids.remove(i);
				i--;
			}
		}
		
		//cargar particulas
		for(int i = 0; i < particles.size(); i++) {
			particles.get(i).update(dt);
			if(particles.get(i).shouldRemove()) {
				particles.remove(i);
				i--;
			}
		}
		
		//check colision
		checkCollisions();
		
		//play musics 
		bgTimer += dt;
		
		//intercalar sonidos entre duro y suave
		if(!player.isHiT() && bgTimer >= currentDelay) {
			if(playLowPulse) {
				Jukebox.play("pulselow");
			}else {
				Jukebox.play("pulsehigh");
			}
			
			playLowPulse = !playLowPulse;
			bgTimer = 0;
		}
	}
	
	private void checkCollisions() {
		
		//player - asteroid collision
		if(!player.isHiT()) {
			for(int i = 0; i < asteroids.size(); i++) {
				Asteroid a = asteroids.get(i);
				//intersects player asteroid
				if(a.intersects(player)) {
					player.hit();
					asteroids.remove(i);
					i--;
					splitAsteroids(a);
					Jukebox.play("explode");
					break;
				}
			}
		}
		// bullet - asteroid collision
		for(int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			
			for(int j = 0; j < asteroids.size(); j++) {
				Asteroid a = asteroids.get(j);
				
				//superposicion a y b
				if(a.contains(b.getX(), b.getY())) {
					bullets.remove(i);
					i--;
					asteroids.remove(j);
					j--;
					splitAsteroids(a);
					
					//incremento de puntaje jugador
					player.incrementScore(a.getScore());
					Jukebox.play("explode");
					break;
				}
			}
		}
		
		//player ovni collision
		if(flyingSaucer != null) {
			if(player.intersects(flyingSaucer)) {
				player.hit();
				createParticles(player.getX(), player.getY());
				createParticles(flyingSaucer.getX(), flyingSaucer.getY());
				flyingSaucer = null;
				Jukebox.stop("smallsaucer");
				Jukebox.stop("largesaucer");
				Jukebox.play("explode");
			}
		}
		
		//bullet ovni collision
		if(flyingSaucer != null) {
			
			for(int i = 0; i < bullets.size(); i++) {
				Bullet b = bullets.get(i);
				
				if(flyingSaucer.contains(b.getX(), b.getY())) {
					bullets.remove(i);
					i--;
					createParticles(flyingSaucer.getX(), flyingSaucer.getY());
					player.incrementScore(flyingSaucer.getScore());
					
					flyingSaucer = null;
					Jukebox.stop("smallsaucer");
					Jukebox.stop("largesaucer");
					Jukebox.play("explode");
					break;
				}
			}
		}
		
		//player enemyBullets collision
		if(!player.isHiT()) {
			for(int i = 0; i < enemyBullets.size(); i++) {
				Bullet b = enemyBullets.get(i);
				
				if(player.contains(b.getX(), b.getY())) {
					player.hit();
					enemyBullets.remove(i);
					i--;
					Jukebox.play("explode");
				}
			}
		}
		
		//ovni asteroid collision
		if(flyingSaucer != null) {
			for(int i = 0; i < asteroids.size(); i++) {
				Asteroid a = asteroids.get(i);
				
				if(a.intersects(flyingSaucer)) {
					asteroids.remove(i);
					i--;
					splitAsteroids(a);
					createParticles(a.getX(), a.getY());
					createParticles(flyingSaucer.getX(), flyingSaucer.getY());
					
					flyingSaucer = null;
					Jukebox.stop("smallsaucer");
					Jukebox.stop("largesaucer");
					Jukebox.play("explode");
					break;
				}
			}
		}
		
		//asteroid bullets enemy collison
		for(int i = 0; i < enemyBullets.size(); i++) {
			Bullet b = enemyBullets.get(i);
			for(int j = 0; j < asteroids.size(); j++) {
				Asteroid a = asteroids.get(i);
				
				if(a.contains(b.getX(), b.getY())) {
					asteroids.remove(j);
					j--;
					splitAsteroids(a);
					
					enemyBullets.remove(i);
					i--;
					createParticles(a.getX(), a.getY());
					Jukebox.play("explode");
					break;
				}
			}
		}
		
	}

	public void draw() {
		sb.setProjectionMatrix(Game.cam.combined);
		sr.setProjectionMatrix(Game.cam.combined);
		
		//pintar jugador
		player.draw(sr);
		
		//pintar balas
		for(int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(sr);
		}
		
		//pintar balas
		for(int i = 0; i < asteroids.size(); i++) {
			asteroids.get(i).draw(sr);
		}
		
		//pintar ovni
		if(flyingSaucer != null) {
			flyingSaucer.draw(sr);
		}
		
		//pintar ovni balas
		for(int i = 0; i < enemyBullets.size(); i++) {
			enemyBullets.get(i).draw(sr);
		}
		
		//pintar particulas
		for(int i = 0; i < particles.size(); i++) {
			particles.get(i).draw(sr);
		}
		
		//pintar score
		sb.setColor(1, 1, 1, 1);
		sb.begin();
		
		font.draw(sb, Long.toString(player.getScore()), 40, 390);
		
		sb.end();
		
		//pintar lvida
		
		for(int i = 0; i < player.getLives(); i++) {
			hudPlayer.setPosition(40 + i * 10, 360);
			hudPlayer.draw(sr);
		}
		
	}

	public void handleInput() {
		
		if(!player.isHiT()) {
			player.setLeft(GameKeys.isDown(GameKeys.LEFT));
			player.setRight(GameKeys.isDown(GameKeys.RIGHT));
			player.setUp(GameKeys.isDown(GameKeys.UP));
			if(GameKeys.isPressed(GameKeys.SPACE)) {
				player.shoot();
			}	
		}
	}

	public void dispose() {
		sb.dispose();
		sr.dispose();
		font.dispose();
	}
}
