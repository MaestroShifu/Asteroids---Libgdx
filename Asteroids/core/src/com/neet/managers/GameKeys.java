package com.neet.managers;

public class GameKeys {
	//determinar el estado de las teclas
		private static boolean[] keys;
		private static boolean[] pkeys;
		
		private static final int NUM_KEYS = 8;
		
		public static final int UP = 0;
		public static final int LEFT = 1;
		public static final int DOWN = 2;
		public static final int RIGHT = 3;
		public static final int ENTER = 4;
		public static final int ESCAPE = 5;
		public static final int SPACE = 6;
		public static final int SHIFT = 7;
		
		static {
			keys = new boolean [NUM_KEYS];
			pkeys = new boolean [NUM_KEYS];
		}
		
		//refrescar el stado de las teclas, en este caso en el vector de boleanos
		public static void update() {
			for(int i = 0; i < NUM_KEYS; i++) {
				pkeys[i] = keys[i];
			}
		}
		
		//darle valor al vector booleano directamente con el contructor, ya que se usa ara aplicar los metodos de llamar listener de teclado
		public static void setKey(int k, boolean b) {
			keys[k] = b;
		}
		
		//cuando la tecla esta presionada, ejecuta todo lo que se mantenga la tecla oprimida
		public static boolean isDown(int k) {
			return keys[k];
		}
		
		//cuando se mantiene presionado 1 disparo ya que no permite ejecutar muchas veces lo mismo
		public static boolean isPressed(int k) {
			return keys[k] && !pkeys[k];
		}
}
